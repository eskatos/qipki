/*
 * Copyright 2012 paul.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.qipki.ca.tests.embedded;

import java.util.EnumSet;
import org.junit.Before;
import org.junit.Test;
import org.qi4j.api.common.Visibility;
import org.qi4j.api.structure.Module;
import org.qi4j.api.value.ValueBuilder;
import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.LayerAssembly;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.entitystore.memory.MemoryEntityStoreService;
import org.qi4j.library.fileconfig.FileConfiguration;
import org.qi4j.test.AbstractQi4jTest;
import static org.qipki.ca.bootstrap.CaAssemblyNames.*;
import org.qipki.ca.bootstrap.CaDCIModuleAssembler;
import org.qipki.ca.bootstrap.CaDomainModuleAssembler;
import org.qipki.commons.bootstrap.CryptoValuesModuleAssembler;
import org.qipki.commons.crypto.states.X509State;
import org.qipki.commons.crypto.values.x509.ExtendedKeyUsagesValue;
import org.qipki.core.bootstrap.persistence.InMemoryPersistenceAssembler;
import org.qipki.core.bootstrap.persistence.PersistenceAssembler;
import org.qipki.core.reindex.AutomaticReindexerConfiguration;
import org.qipki.crypto.bootstrap.CryptoEngineModuleAssembler;
import org.qipki.crypto.x509.ExtendedKeyUsage;

public class ValueLookupFullTest
        extends AbstractQi4jTest
{

    @Override
    public void assemble( ModuleAssembly testModule )
            throws AssemblyException
    {
        LayerAssembly testLayer = testModule.layer();
        ApplicationAssembly app = testLayer.application();

        LayerAssembly presentationLayer = app.layer( LAYER_PRESENTATION );
        LayerAssembly configLayer = app.layer( LAYER_CONFIGURATION );
        {
            ModuleAssembly config = configLayer.module( MODULE_CONFIGURATION );
            config.services( FileConfiguration.class ).visibleIn( Visibility.application );
            config.services( MemoryEntityStoreService.class ).visibleIn( Visibility.module );
            config.entities( AutomaticReindexerConfiguration.class ).visibleIn( Visibility.application );
        }
        LayerAssembly appLayer = app.layer( LAYER_APPLICATION );
        {
            new CaDCIModuleAssembler().assemble( appLayer.module( MODULE_CA_DCI ) );
        }
        LayerAssembly cryptoLayer = app.layer( LAYER_CRYPTO );
        {
            ModuleAssembly config = configLayer.module( MODULE_CONFIGURATION );
            new CryptoEngineModuleAssembler( Visibility.application ).withConfigModule( config ).withConfigVisibility( Visibility.application ).assemble( cryptoLayer.module( MODULE_CRYPTO_ENGINE ) );
            new CryptoValuesModuleAssembler( Visibility.application ).assemble( cryptoLayer.module( MODULE_CRYPTO_VALUES ) );

        }
        LayerAssembly infraLayer = app.layer( LAYER_INFRASTRUCTURE );
        {
            ModuleAssembly config = configLayer.module( MODULE_CONFIGURATION );
            PersistenceAssembler persistAss = new InMemoryPersistenceAssembler();
            persistAss.assemble( infraLayer.module( MODULE_PERSISTENCE ) );
            persistAss.assembleConfigModule( config );
        }
        LayerAssembly domainLayer = app.layer( LAYER_DOMAIN );
        {
            new CaDomainModuleAssembler().assemble( domainLayer.module( MODULE_CA_DOMAIN ) );
        }
        LayerAssembly mngmtLayer = app.layer( LAYER_MANAGEMENT );

        ModuleAssembly presentationModule = presentationLayer.module( MODULE_TESTS_IN_PRESENTATION );

        presentationLayer.uses( appLayer, cryptoLayer, configLayer );
        appLayer.uses( domainLayer, cryptoLayer, configLayer );
        domainLayer.uses( cryptoLayer, configLayer, infraLayer );
        cryptoLayer.uses( configLayer );
        infraLayer.uses( configLayer );
    }

    private Module presentationModule;

    @Before
    public void before()
    {
        presentationModule = application.findModule( LAYER_PRESENTATION, MODULE_TESTS_IN_PRESENTATION );
    }

    @Test
    public void test()
    {
        ValueBuilder<ExtendedKeyUsagesValue> builder = presentationModule.newValueBuilder( ExtendedKeyUsagesValue.class );
        ExtendedKeyUsagesValue value = builder.prototype();
        value.critical().set( Boolean.TRUE );
        value.extendedKeyUsages().set( EnumSet.of( ExtendedKeyUsage.codeSigning ) );
        value = builder.newInstance();
        System.out.println( value );
    }

}
