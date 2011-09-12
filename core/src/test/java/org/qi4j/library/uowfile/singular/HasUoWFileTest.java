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
package org.qi4j.library.uowfile.singular;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import org.qi4j.api.entity.EntityBuilder;
import org.qi4j.api.entity.EntityComposite;
import org.qi4j.api.entity.Identity;
import org.qi4j.api.injection.scope.This;
import org.qi4j.api.io.Inputs;
import org.qi4j.api.io.Outputs;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.property.Property;
import org.qi4j.api.unitofwork.UnitOfWork;
import org.qi4j.api.unitofwork.UnitOfWorkCompletionException;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.core.testsupport.AbstractQi4jTest;
import org.qi4j.library.uowfile.UoWFileAssembler;
import org.qi4j.test.EntityTestAssembler;

public class HasUoWFileTest
        extends AbstractQi4jTest
{

    @Mixins( HasUoWFileTest.TestedEntityMixin.class )
    public static interface TestedEntity
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

    @Override
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        module.entities( TestedEntity.class );
        new UoWFileAssembler().assemble( module );
        new EntityTestAssembler().assemble( module );
    }

    @Test
    public void testCreation()
            throws UnitOfWorkCompletionException, IOException
    {
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
        // Create new
        UnitOfWork uow = unitOfWorkFactory.newUnitOfWork();
        TestedEntity entity = createTestedEntity( uow, "Testing Modification" );
        String entityId = entity.identity().get();
        File attachedFile = entity.attachedFile();
        uow.complete();

        // Testing discarded modification
        uow = unitOfWorkFactory.newUnitOfWork();
        entity = uow.get( TestedEntity.class, entityId );
        Inputs.text( getClass().getResource( "../modification.txt" ) ).transferTo( Outputs.text( entity.managedFile() ) );
        uow.discard();
        assertTrue( "File content after discarded modification was not the good one", isFileFirstLineEqualsTo( attachedFile, "Creation" ) );

        // Testing completed modification
        uow = unitOfWorkFactory.newUnitOfWork();
        entity = uow.get( TestedEntity.class, entityId );
        Inputs.text( getClass().getResource( "../modification.txt" ) ).transferTo( Outputs.text( entity.managedFile() ) );
        uow.complete();
        assertTrue( "Modified file content was not the good one", isFileFirstLineEqualsTo( attachedFile, "Modification" ) );
    }

    @Test
    public void testDeletion()
            throws UnitOfWorkCompletionException, IOException
    {
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
    public void testConcurrency()
    {
        // TODO
    }

    @Test
    public void testRetry()
    {
        // TODO
    }

    private TestedEntity createTestedEntity( UnitOfWork uow, String name )
            throws IOException
    {
        EntityBuilder<TestedEntity> builder = uow.newEntityBuilder( TestedEntity.class );
        TestedEntity entity = builder.instance();
        entity.name().set( name );
        entity = builder.newInstance();
        Inputs.text( getClass().getResource( "../creation.txt" ) ).transferTo( Outputs.text( entity.managedFile() ) );
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
