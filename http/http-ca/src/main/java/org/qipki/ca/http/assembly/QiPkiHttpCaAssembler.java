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
package org.qipki.ca.http.assembly;

import org.qipki.ca.assembly.CaAssemblyNames;
import org.qipki.ca.assembly.QiPkiPersistentEmbeddedCaAssembler;
import org.qipki.core.assembly.AssemblyUtil;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.ApplicationAssemblyFactory;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.LayerAssembly;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.library.http.JettyConfiguration;

public class QiPkiHttpCaAssembler
        extends QiPkiPersistentEmbeddedCaAssembler
{

    public QiPkiHttpCaAssembler( String connectionString, String finderDataDirectory )
    {
        super( connectionString, finderDataDirectory );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final ApplicationAssembly assemble( ApplicationAssemblyFactory applicationFactory )
            throws AssemblyException
    {

        ApplicationAssembly app = super.assemble( applicationFactory );

        LayerAssembly presentation = app.layerAssembly( HttpCaAssemblyNames.LAYER_PRESENTATION );
        {
            assembleDevTestModule( presentation.moduleAssembly( HttpCaAssemblyNames.MODULE_TESTS_IN_PRESENTATION ) );

            new RestApiModuleAssembler().assemble(
                    presentation.moduleAssembly( HttpCaAssemblyNames.MODULE_REST_API ) );

            new HttpModuleAssembler().assemble(
                    presentation.moduleAssembly( HttpCaAssemblyNames.MODULE_HTTP ) );
        }

        // Add JettyConfiguration to the Configuration module
        ModuleAssembly config = AssemblyUtil.getModuleAssembly( app, CaAssemblyNames.LAYER_CONFIGURATION, CaAssemblyNames.MODULE_CONFIGURATION );
        config.addEntities( JettyConfiguration.class ).visibleIn( Visibility.application );

        presentation.uses( AssemblyUtil.getLayerAssembly( app, CaAssemblyNames.LAYER_APPLICATION ),
                           AssemblyUtil.getLayerAssembly( app, CaAssemblyNames.LAYER_CRYPTO ),
                           AssemblyUtil.getLayerAssembly( app, CaAssemblyNames.LAYER_CONFIGURATION ) );

        return app;
    }

}
