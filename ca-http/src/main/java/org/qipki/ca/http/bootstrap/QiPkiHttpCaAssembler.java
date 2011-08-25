/*
 * Copyright (c) 2010, Paul Merlin. All Rights Reserved.
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
package org.qipki.ca.http.bootstrap;

import java.io.IOException;

import org.codeartisans.java.toolbox.network.FreePortFinder;

import org.qi4j.api.common.InvalidApplicationException;
import org.qi4j.api.common.Visibility;
import org.qi4j.api.structure.Application.Mode;
import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.ApplicationAssemblyFactory;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.AssemblyVisitorAdapter;
import org.qi4j.bootstrap.LayerAssembly;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.library.http.JettyConfiguration;
import org.qi4j.library.jmx.JMXAssembler;
import org.qi4j.library.jmx.JMXConnectorConfiguration;
import org.qi4j.library.jmx.JMXConnectorService;

import org.qipki.ca.bootstrap.CaAssemblyNames;
import org.qipki.ca.bootstrap.QiPkiEmbeddedCaAssembler;
import org.qipki.ca.http.presentation.rest.api.RestApiConfiguration;
import org.qipki.core.bootstrap.persistence.DerbySesamePersistenceAssembler;

public class QiPkiHttpCaAssembler
        extends QiPkiEmbeddedCaAssembler
{

    private final Integer jmxPort;
    private Assembler webClientAssembler;

    public QiPkiHttpCaAssembler( String appName, Mode appMode, String connectionString, Integer jmxPort )
    {
        super( appName, appMode );
        withPersistenceAssembler( new DerbySesamePersistenceAssembler( connectionString ) );
        this.jmxPort = jmxPort;
    }

    public QiPkiHttpCaAssembler withWebClientAssembler( Assembler assembler )
    {
        this.webClientAssembler = assembler;
        return this;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final ApplicationAssembly assemble( ApplicationAssemblyFactory applicationFactory )
            throws AssemblyException
    {

        ApplicationAssembly app = super.assemble( applicationFactory );

        final LayerAssembly management = app.layer( HttpCaAssemblyNames.LAYER_MANAGEMENT );
        {
            ModuleAssembly jmx = management.module( HttpCaAssemblyNames.MODULE_JMX );
            new JMXAssembler().assemble( jmx );
            jmx.services( JMXConnectorService.class ).instantiateOnStartup();
        }

        LayerAssembly presentation = app.layer( HttpCaAssemblyNames.LAYER_PRESENTATION );
        {
            new HttpModuleAssembler().assemble( presentation.module( HttpCaAssemblyNames.MODULE_HTTP ) );

            new RestApiModuleAssembler().assemble( presentation.module( HttpCaAssemblyNames.MODULE_REST_API ) );

            if ( webClientAssembler != null ) {
                webClientAssembler.assemble( presentation.module( HttpCaAssemblyNames.MODULE_WEB_CLIENT ) );
            }

            assembleDevTestModule( presentation.module( HttpCaAssemblyNames.MODULE_TESTS_IN_PRESENTATION ) );
        }

        // Add Configuration entities to the configuration module
        ModuleAssembly config = app.layer( CaAssemblyNames.LAYER_CONFIGURATION ).module( CaAssemblyNames.MODULE_CONFIGURATION );
        {
            config.entities( RestApiConfiguration.class,
                             JettyConfiguration.class,
                             JMXConnectorConfiguration.class ).
                    visibleIn( Visibility.application );

            JMXConnectorConfiguration jmxConfigDefaults = config.forMixin( JMXConnectorConfiguration.class ).declareDefaults();
            jmxConfigDefaults.enabled().set( Boolean.TRUE );
            if ( jmxPort != null && jmxPort != -1 ) {
                jmxConfigDefaults.port().set( jmxPort );
            } else {
                try {
                    jmxConfigDefaults.port().set( FreePortFinder.findRandom() );
                } catch ( IOException ex ) {
                    throw new AssemblyException( "No default JMX port provided and unable to dynamicaly find a free one", ex );
                }
            }
        }

        // Management Layer uses all application layers
        app.visit( new AssemblyVisitorAdapter<InvalidApplicationException>()
        {

            @Override
            public void visitLayer( LayerAssembly eachLayer )
                    throws InvalidApplicationException
            {
                if ( !management.name().equals( eachLayer.name() ) ) {
                    management.uses( eachLayer );
                }
            }

        } );

        presentation.uses( app.layer( CaAssemblyNames.LAYER_APPLICATION ),
                           app.layer( CaAssemblyNames.LAYER_CRYPTO ),
                           app.layer( CaAssemblyNames.LAYER_CONFIGURATION ) );

        return app;
    }

}
