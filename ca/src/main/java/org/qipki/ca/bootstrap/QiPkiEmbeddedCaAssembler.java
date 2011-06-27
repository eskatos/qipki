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
package org.qipki.ca.bootstrap;

import org.qipki.commons.bootstrap.CryptoValuesModuleAssembler;
import org.qipki.core.reindex.AutomaticReindexerConfiguration;
import org.qipki.crypto.bootstrap.CryptoEngineModuleAssembler;

import org.qi4j.api.common.Visibility;
import org.qi4j.api.structure.Application.Mode;
import org.qi4j.bootstrap.ApplicationAssembler;
import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.ApplicationAssemblyFactory;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.LayerAssembly;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.entitystore.memory.MemoryEntityStoreService;
import org.qi4j.library.fileconfig.FileConfiguration;
import org.qi4j.library.fileconfig.FileConfigurationOverride;
import org.qi4j.library.scheduler.SchedulerConfiguration;
import org.qi4j.library.scheduler.bootstrap.SchedulerAssembler;

public abstract class QiPkiEmbeddedCaAssembler
        implements ApplicationAssembler
{

    private String appName = CaAssemblyNames.APPLICATION_NAME;
    private final Mode appMode;
    private FileConfigurationOverride fileConfigOverride;

    public QiPkiEmbeddedCaAssembler( Mode appMode )
    {
        this.appMode = appMode;
    }

    public QiPkiEmbeddedCaAssembler( String appName, Mode appMode )
    {
        this( appMode );
        this.appName = appName;
    }

    public ApplicationAssembler withFileConfigurationOverride( FileConfigurationOverride fileConfigOverride )
    {
        this.fileConfigOverride = fileConfigOverride;
        return this;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public ApplicationAssembly assemble( ApplicationAssemblyFactory applicationFactory )
            throws AssemblyException
    {
        ApplicationAssembly app = applicationFactory.newApplicationAssembly();
        app.setName( appName );
        app.setMode( appMode );
        app.setVersion( CaAssemblyNames.APPLICATION_VERSION );

        LayerAssembly configuration = app.layer( CaAssemblyNames.LAYER_CONFIGURATION );
        {
            ModuleAssembly config = configuration.module( CaAssemblyNames.MODULE_CONFIGURATION );
            config.addServices( FileConfiguration.class ).visibleIn( Visibility.application );
            if ( fileConfigOverride != null ) {
                config.services( FileConfiguration.class ).setMetaInfo( fileConfigOverride );
            }
            config.addServices( MemoryEntityStoreService.class ).visibleIn( Visibility.module );
            config.entities( AutomaticReindexerConfiguration.class ).visibleIn( Visibility.application );
        }

        LayerAssembly application = app.layer( CaAssemblyNames.LAYER_APPLICATION );
        {
            new CaDCIModuleAssembler().assemble(
                    application.module( CaAssemblyNames.MODULE_CA_DCI ) );
        }

        LayerAssembly domain = app.layer( CaAssemblyNames.LAYER_DOMAIN );
        {
            new CaDomainModuleAssembler().assemble(
                    domain.module( CaAssemblyNames.MODULE_CA_DOMAIN ) );
        }

        LayerAssembly crypto = app.layer( CaAssemblyNames.LAYER_CRYPTO );
        {
            new CryptoEngineModuleAssembler( Visibility.application ).assemble(
                    crypto.module( CaAssemblyNames.MODULE_CRYPTO_ENGINE ) );
            new CryptoValuesModuleAssembler( Visibility.application ).assemble(
                    crypto.module( CaAssemblyNames.MODULE_CRYPTO_VALUES ) );
        }

        LayerAssembly infrastructure = app.layer( CaAssemblyNames.LAYER_INFRASTRUCTURE );
        {
            ModuleAssembly scheduler = infrastructure.module( CaAssemblyNames.MODULE_SCHEDULER );
            ModuleAssembly config = configuration.module( CaAssemblyNames.MODULE_CONFIGURATION );
            new SchedulerAssembler().withConfigAssembly( config ).
                    withConfigVisibility( Visibility.application ).
                    withTimeline().
                    visibleIn( Visibility.application ).
                    assemble( scheduler );
        }

        application.uses( domain, crypto, configuration );
        domain.uses( crypto, configuration, infrastructure );
        infrastructure.uses( configuration );

        return app;
    }

    protected void assembleDevTestModule( ModuleAssembly devTestModule )
            throws AssemblyException
    {
    }

}
