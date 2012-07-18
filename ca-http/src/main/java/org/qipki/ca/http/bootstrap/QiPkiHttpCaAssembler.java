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

import org.qi4j.api.structure.Application.Mode;
import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.LayerAssembly;
import org.qi4j.bootstrap.ModuleAssembly;

import org.qipki.ca.bootstrap.QiPkiEmbeddedCaAssembler;
import static org.qipki.ca.http.bootstrap.HttpCaAssemblyNames.*;

public class QiPkiHttpCaAssembler
        extends QiPkiEmbeddedCaAssembler
{

    private static final int DEFAULT_PORT = 8443;
    private String iface;
    private Integer port;
    private String docRoot;
    private Assembler webClientAssembler;

    public QiPkiHttpCaAssembler( String appName, String appVersion, Mode appMode )
    {
        super( appName, appVersion, appMode );
    }

    public final QiPkiHttpCaAssembler withHttpConfiguration( String iface, Integer port, String docRoot )
    {
        this.iface = iface;
        if ( port != null ) {
            this.port = port;
        } else {
            this.port = DEFAULT_PORT;
        }
        this.docRoot = docRoot;
        return this;
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
        ModuleAssembly config = app.layer( LAYER_CONFIGURATION ).module( MODULE_CONFIGURATION );

        LayerAssembly presentation = app.layer( LAYER_PRESENTATION );

        HttpModuleAssembler httpAssembler = new HttpModuleAssembler().withInterface( iface ).withPort( port ).withDocRoot( docRoot );
        httpAssembler.withConfigModule( config );
        httpAssembler.assemble( presentation.module( MODULE_HTTP ) );

        RestApiModuleAssembler restApiAssembler = new RestApiModuleAssembler();
        restApiAssembler.withConfigModule( config );
        restApiAssembler.assemble( presentation.module( MODULE_REST_API ) );

        if ( webClientAssembler != null ) {
            webClientAssembler.assemble( presentation.module( MODULE_WEB_CLIENT ) );
        }
    }

}
