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
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.library.http.JettyConfiguration;
import org.qi4j.library.http.JettyServiceAssembler;

public class HttpModuleAssembler
        implements Assembler
{

    private String iface;
    private Integer port;
    private String docRoot;
    private ModuleAssembly configModule;

    public HttpModuleAssembler withInterface( String iface )
    {
        this.iface = iface;
        return this;
    }

    public HttpModuleAssembler withPort( Integer port )
    {
        this.port = port;
        return this;
    }

    public HttpModuleAssembler withDocRoot( String docRoot )
    {
        this.docRoot = docRoot;
        return this;
    }

    public HttpModuleAssembler withConfigModule( ModuleAssembly config )
    {
        this.configModule = config;
        return this;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        ModuleAssembly config = configModule == null ? module : configModule;
        
        new JettyServiceAssembler().withConfigModule( config ).withConfigVisibility( Visibility.application ).assemble( module );
        
        config.entities( JettyConfiguration.class ).visibleIn( Visibility.application );
        JettyConfiguration jettyConfig = config.forMixin( JettyConfiguration.class ).declareDefaults();
        jettyConfig.hostName().set( iface );
        jettyConfig.port().set( port );
        jettyConfig.resourcePath().set( docRoot );
    }

}
