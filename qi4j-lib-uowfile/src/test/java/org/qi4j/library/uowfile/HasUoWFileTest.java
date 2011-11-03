/*
 * Copyright (c) 2011, Paul Merlin. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.qi4j.library.uowfile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.qi4j.api.concern.Concerns;
import org.qi4j.api.entity.EntityBuilder;
import org.qi4j.api.entity.EntityComposite;
import org.qi4j.api.entity.Identity;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.injection.scope.This;
import org.qi4j.api.io.Inputs;
import org.qi4j.api.io.Outputs;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.property.Property;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWork;
import org.qi4j.api.unitofwork.UnitOfWorkCompletionException;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;
import org.qi4j.api.unitofwork.UnitOfWorkConcern;
import org.qi4j.api.unitofwork.UnitOfWorkPropagation;
import org.qi4j.api.unitofwork.UnitOfWorkRetry;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.core.testsupport.AbstractQi4jTest;
import org.qi4j.library.uowfile.internal.ConcurrentUoWFileModificationException;
import org.qi4j.library.uowfile.singular.HasUoWFileLifecycle;
import org.qi4j.library.uowfile.singular.UoWFileLocator;
import org.qi4j.test.EntityTestAssembler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HasUoWFileTest
        extends AbstractQi4jTest
{

    private static final Logger LOGGER = LoggerFactory.getLogger( HasUoWFileTest.class );
    private static final URL CREATION_CONTENT_URL = HasUoWFileTest.class.getResource( "creation.txt" );
    private static final URL MODIFICATION_CONTENT_URL = HasUoWFileTest.class.getResource( "modification.txt" );
    private static File baseTestDir;

    @BeforeClass
    public static void beforeClass()
            throws IOException
    {
        File testDir = new File( "target/" + System.getProperty( "test" ) );
        if ( !testDir.exists() ) {
            if ( !testDir.mkdirs() ) {
                throw new IOException( "Unable to create directory: " + testDir );
            }
        }
        baseTestDir = testDir;
    }

    @AfterClass
    public static void afterClass()
    {
        // TODO Delete baseTestDir
    }

    @Mixins( HasUoWFileTest.TestedEntityMixin.class )
    public interface TestedEntity
            extends HasUoWFileLifecycle, EntityComposite
    {

        Property<String> name();

    }

    public static abstract class TestedEntityMixin
            implements TestedEntity, UoWFileLocator
    {

        @This
        private Identity meAsIdentity;

        @Override
        public File locateAttachedFile()
        {
            return new File( baseTestDir, meAsIdentity.identity().get() );
        }

    }

    @Mixins( HasUoWFileTest.TestServiceMixin.class )
    @Concerns( UnitOfWorkConcern.class )
    public interface TestService
            extends ServiceComposite
    {

        void modifyFile( String entityId )
                throws IOException;

        @UnitOfWorkPropagation
        @UnitOfWorkRetry
        void modifyFileWithRetry( String entityId, long sleepBefore, long sleepAfter )
                throws IOException;

    }

    public static abstract class TestServiceMixin
            implements TestService
    {

        @Structure
        private UnitOfWorkFactory uowf;

        @Override
        public void modifyFile( String entityId )
                throws IOException
        {
            modifyFileImmediatly( entityId );
        }

        @Override
        public void modifyFileWithRetry( String entityId, long sleepBefore, long sleepAfter )
                throws IOException
        {
            LOGGER.info( "Waiting " + sleepBefore + "ms before file modification" );
            if ( sleepBefore > 0 ) {
                try {
                    Thread.sleep( sleepBefore );
                } catch ( InterruptedException ex ) {
                    throw new RuntimeException( ex );
                }
            }
            modifyFileImmediatly( entityId );
            LOGGER.info( "Waiting " + sleepAfter + "ms after file modification" );
            if ( sleepAfter > 0 ) {
                try {
                    Thread.sleep( sleepAfter );
                } catch ( InterruptedException ex ) {
                    throw new RuntimeException( ex );
                }
            }
        }

        private void modifyFileImmediatly( String entityId )
                throws IOException
        {
            TestedEntity entity = uowf.currentUnitOfWork().get( TestedEntity.class, entityId );
            Inputs.text( MODIFICATION_CONTENT_URL ).transferTo( Outputs.text( entity.managedFile() ) );
        }

    }

    @Override
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        module.entities( TestedEntity.class );
        module.services( TestService.class );
        new UoWFileAssembler().assemble( module );
        new EntityTestAssembler().assemble( module );
    }

    private TestService testService;

    @Before
    public void beforeTest()
    {
        testService = serviceLocator.<TestService>findService( TestService.class ).get();
    }

    @Test
    public void testCreation()
            throws UnitOfWorkCompletionException, IOException
    {
        LOGGER.info( "# Test Creation ##############################################################################" );

        // Test discarded creation
        UnitOfWork uow = unitOfWorkFactory.newUnitOfWork();
        TestedEntity entity = createTestedEntity( uow, "Testing Creation Rollback" );
        File attachedFile = entity.attachedFile();
        uow.discard();
        assertFalse( "File still exists after discarded creation UoW", attachedFile.exists() );

        // Test completed creation
        uow = unitOfWorkFactory.newUnitOfWork();
        entity = createTestedEntity( uow, "Testing Creation" );
        attachedFile = entity.attachedFile();
        uow.complete();
        assertTrue( "File content was not the good one", isFileFirstLineEqualsTo( attachedFile, "Creation" ) );
    }

    @Test
    public void testModification()
            throws UnitOfWorkCompletionException, IOException
    {
        LOGGER.info( "# Test Modification ##########################################################################" );

        // Create new
        UnitOfWork uow = unitOfWorkFactory.newUnitOfWork();
        TestedEntity entity = createTestedEntity( uow, "Testing Modification" );
        String entityId = entity.identity().get();
        File attachedFile = entity.attachedFile();
        uow.complete();

        // Testing discarded modification
        uow = unitOfWorkFactory.newUnitOfWork();
        testService.modifyFile( entityId );
        uow.discard();
        assertTrue( "File content after discarded modification was not the good one", isFileFirstLineEqualsTo( attachedFile, "Creation" ) );

        // Testing completed modification
        uow = unitOfWorkFactory.newUnitOfWork();
        testService.modifyFile( entityId );
        uow.complete();
        assertTrue( "Modified file content was not the good one", isFileFirstLineEqualsTo( attachedFile, "Modification" ) );
    }

    @Test
    public void testDeletion()
            throws UnitOfWorkCompletionException, IOException
    {
        LOGGER.info( "# Test Deletion ##############################################################################" );

        // Create new
        UnitOfWork uow = unitOfWorkFactory.newUnitOfWork();
        TestedEntity entity = createTestedEntity( uow, "Testing Deletion" );
        String entityId = entity.identity().get();
        File attachedFile = entity.attachedFile();
        uow.complete();

        // Testing discarded deletion
        uow = unitOfWorkFactory.newUnitOfWork();
        entity = uow.get( TestedEntity.class, entityId );
        uow.remove( entity );
        uow.discard();
        assertTrue( "File do not exists after discarded deletion", attachedFile.exists() );

        // Testing completed deletion
        uow = unitOfWorkFactory.newUnitOfWork();
        entity = uow.get( TestedEntity.class, entityId );
        uow.remove( entity );
        uow.complete();
        assertFalse( "File still exists after deletion", attachedFile.exists() );
    }

    @Test
    public void testConcurrentModification()
            throws IOException, UnitOfWorkCompletionException
    {
        LOGGER.info( "# Test Concurrent Modification ###############################################################" );

        // Create new
        UnitOfWork uow = unitOfWorkFactory.newUnitOfWork();
        TestedEntity entity = createTestedEntity( uow, "Testing Concurrent Modification" );
        String entityId = entity.identity().get();
        uow.complete();

        // Testing concurrent modification
        uow = unitOfWorkFactory.newUnitOfWork();
        entity = uow.get( TestedEntity.class, entityId );
        Inputs.text( MODIFICATION_CONTENT_URL ).transferTo( Outputs.text( entity.managedFile() ) );
        UnitOfWork uow2 = unitOfWorkFactory.newUnitOfWork();
        entity = uow2.get( TestedEntity.class, entityId );
        Inputs.text( MODIFICATION_CONTENT_URL ).transferTo( Outputs.text( entity.managedFile() ) );
        uow.complete();
        try {
            uow2.complete();
            fail( "A ConcurrentUoWFileModificationException should have been raised" );
        } catch ( ConcurrentUoWFileModificationException expected ) {
            uow2.discard();
        }
    }

    @Test
    // @Ignore // DO NOT WORK
    public void testRetry()
            throws IOException, UnitOfWorkCompletionException, InterruptedException
    {
        LOGGER.info( "# Test Retry #################################################################################" );

        // Create new
        UnitOfWork uow = unitOfWorkFactory.newUnitOfWork();
        TestedEntity entity = createTestedEntity( uow, "Testing Concurrent Modification" );
        final String entityId = entity.identity().get();
        File attachedFile = entity.attachedFile();
        uow.complete();

        final List<Exception> ex = new ArrayList<Exception>();
        Thread t1 = new Thread( new Runnable()
        {

            @Override
            public void run()
            {
                try {
                    testService.modifyFileWithRetry( entityId, 0, 10000 );
                } catch ( Exception ex1 ) {
                    ex.add( ex1 );
                }
            }

        }, "job1" );
        Thread t2 = new Thread( new Runnable()
        {

            @Override
            public void run()
            {
                try {
                    testService.modifyFileWithRetry( entityId, 5000, 0 );
                } catch ( Exception ex1 ) {
                    ex.add( ex1 );
                }
            }

        }, "job2" );

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        for ( Exception eachEx : ex ) {
            eachEx.printStackTrace();
        }

        assertTrue( "There were errors during TestRetry", ex.isEmpty() );
        assertTrue( "Modified file content was not the good one", isFileFirstLineEqualsTo( attachedFile, "Modification" ) );
    }

    private TestedEntity createTestedEntity( UnitOfWork uow, String name )
            throws IOException
    {
        EntityBuilder<TestedEntity> builder = uow.newEntityBuilder( TestedEntity.class );
        TestedEntity entity = builder.instance();
        entity.name().set( name );
        entity = builder.newInstance();
        Inputs.text( CREATION_CONTENT_URL ).transferTo( Outputs.text( entity.managedFile() ) );
        return entity;
    }

    private boolean isFileFirstLineEqualsTo( File file, String start )
            throws IOException
    {
        List<String> lines = new ArrayList<String>();
        // This load the full file but used test resources are single line files
        Inputs.text( file ).transferTo( Outputs.collection( lines ) );
        return lines.get( 0 ).trim().startsWith( start );
    }

}
