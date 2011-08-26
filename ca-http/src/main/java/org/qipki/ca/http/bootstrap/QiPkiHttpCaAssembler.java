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

import org.qi4j.api.common.Visibility;
import org.qi4j.api.structure.Application.Mode;
import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.LayerAssembly;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.library.http.JettyConfiguration;

import org.qipki.ca.bootstrap.QiPkiEmbeddedCaAssembler;
import static org.qipki.ca.http.bootstrap.HttpCaAssemblyNames.*;
import org.qipki.ca.http.presentation.rest.api.RestApiConfiguration;

public class QiPkiHttpCaAssembler
        extends QiPkiEmbeddedCaAssembler
{

    private Assembler webClientAssembler;

    public QiPkiHttpCaAssembler( String appName, String appVersion, Mode appMode )
    {
        super( appName, appVersion, appMode );
    }

    public final QiPkiHttpCaAssembler withWebClientAssembler( Assembler assembler )
    {
        this.webClientAssembler = assembler;
        return this;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    protected final void onAssemble( ApplicationAssembly app )
            throws AssemblyException
    {
        LayerAssembly presentation = app.layer( LAYER_PRESENTATION );

        new HttpModuleAssembler().assemble( presentation.module( MODULE_HTTP ) );
        new RestApiModuleAssembler().assemble( presentation.module( MODULE_REST_API ) );
        if ( webClientAssembler != null ) {
            webClientAssembler.assemble( presentation.module( MODULE_WEB_CLIENT ) );
        }

        ModuleAssembly config = app.layer( LAYER_CONFIGURATION ).module( MODULE_CONFIGURATION );
        config.entities( RestApiConfiguration.class, JettyConfiguration.class ).visibleIn( Visibility.application );
    }

}
