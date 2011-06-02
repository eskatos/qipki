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
package org.qipki.ca.assembly;

import org.qipki.commons.assembly.CryptoValuesModuleAssembler;
import org.qipki.core.reindex.AutomaticReindexerConfiguration;
import org.qipki.crypto.assembly.CryptoEngineModuleAssembler;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.ApplicationAssembler;
import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.ApplicationAssemblyFactory;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.LayerAssembly;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.entitystore.memory.MemoryEntityStoreService;
import org.qi4j.library.fileconfig.FileConfiguration;

public abstract class QiPkiEmbeddedCaAssembler
        implements ApplicationAssembler
{

    @Override
    @SuppressWarnings( "unchecked" )
    public ApplicationAssembly assemble( ApplicationAssemblyFactory applicationFactory )
            throws AssemblyException
    {
        ApplicationAssembly app = applicationFactory.newApplicationAssembly();

        app.setName( CaAssemblyNames.APPLICATION_NAME );
        app.setVersion( CaAssemblyNames.APPLICATION_VERSION );

        LayerAssembly config = app.layer( CaAssemblyNames.LAYER_CONFIGURATION );
        {
            ModuleAssembly configMa = config.module( CaAssemblyNames.MODULE_CONFIGURATION );
            configMa.addServices( FileConfiguration.class ).visibleIn( Visibility.application );
            configMa.addServices( MemoryEntityStoreService.class ).visibleIn( Visibility.module );
            configMa.entities( AutomaticReindexerConfiguration.class ).visibleIn( Visibility.application );
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

        application.uses( domain, crypto, config );
        domain.uses( crypto, config );

        return app;
    }

    protected void assembleDevTestModule( ModuleAssembly devTestModule )
            throws AssemblyException
    {
    }

}
