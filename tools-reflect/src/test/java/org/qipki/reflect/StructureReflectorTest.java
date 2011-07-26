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
package org.qipki.reflect;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import org.junit.Test;
import org.qi4j.api.composite.TransientComposite;
import org.qi4j.api.entity.EntityComposite;
import org.qi4j.api.io.Inputs;
import org.qi4j.api.io.Outputs;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.value.ValueComposite;
import org.qi4j.bootstrap.ApplicationAssembler;
import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.ApplicationAssemblyFactory;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.Energy4Java;
import org.qi4j.bootstrap.LayerAssembly;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.spi.structure.ApplicationModelSPI;
import org.qi4j.test.AbstractQi4jTest;

public class StructureReflectorTest
        extends AbstractQi4jTest
{

    @Override
    public void assemble( ModuleAssembly ma )
            throws AssemblyException
    {
        new StructureReflectorAssembler().assemble( ma );
    }

    @Test
    public void testAscii()
            throws AssemblyException
    {
        ApplicationModelSPI appModel = new Energy4Java().newApplicationModel( new PanCakesAppAssembler() );

        StructureReflector reflector = serviceLocator.<StructureReflector>findService( StructureReflector.class ).get();

        StringWriter sw = new StringWriter();
        reflector.writePlainTextStructure( appModel, sw );

        System.out.println();
        System.out.println( sw.toString() );
        System.out.println();
    }

    @Test
    public void testHtml()
            throws AssemblyException, UnsupportedEncodingException, IOException
    {
        ApplicationModelSPI appModel = new Energy4Java().newApplicationModel( new PanCakesAppAssembler() );

        StructureReflector reflector = serviceLocator.<StructureReflector>findService( StructureReflector.class ).get();

        StringWriter sw = new StringWriter();
        reflector.writeHtmlStructure( appModel, sw );

        System.out.println();
        System.out.println( sw.toString() );
        System.out.println();

        Inputs.byteBuffer( new ByteArrayInputStream( sw.toString().getBytes( "UTF-8" ) ), 64 ).
                transferTo( Outputs.byteBuffer( new File( "target/application.html" ) ) );
    }

    public static class PanCakesAppAssembler
            implements ApplicationAssembler
    {

        @Override
        public ApplicationAssembly assemble( ApplicationAssemblyFactory aaf )
                throws AssemblyException
        {
            ApplicationAssembly app = aaf.newApplicationAssembly();

            LayerAssembly presentation = app.layer( "Presentation" );
            {
                ModuleAssembly http = presentation.module( "HTTP" );
                http.addServices( HttpService.class ).instantiateOnStartup();
            }

            LayerAssembly application = app.layer( "Application" );
            {
                ModuleAssembly contexts = application.module( "Contexts" );
            }

            LayerAssembly domain = app.layer( "Domain" );
            {
                ModuleAssembly model = domain.module( "Model" );
                model.entities( Thing.class );
                model.values( ThingPart.class );
                model.transients( VolatileThing.class );
                model.addServices( ThingFactory.class );
            }

            LayerAssembly infra = app.layer( "Infrastructure" );
            {
                ModuleAssembly persistence = infra.module( "Persistence" );
                ModuleAssembly messaging = infra.module( "Messaging" );
                messaging.addServices( MailerService.class );
            }

            presentation.uses( application, infra );
            application.uses( domain );
            domain.uses( infra );

            return app;
        }

    }

    public static interface Thing
            extends EntityComposite
    {
    }

    public static interface ThingPart
            extends ValueComposite
    {
    }

    public static interface VolatileThing
            extends TransientComposite
    {
    }

    public static interface ThingFactory
            extends ServiceComposite
    {
    }

    public static interface HttpService
            extends ServiceComposite
    {
    }

    public static interface MailerService
            extends ServiceComposite
    {
    }

}
