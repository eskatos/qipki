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
package org.qipki.ca.tests.http;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.entitystore.memory.MemoryEntityStoreService;

import org.qipki.client.ca.QiPkiCaHttpClientConfiguration;
import org.qipki.client.ca.bootstrap.QiPkiCaClientAssembler;
import org.qipki.client.ca.api.QiPkiHttpCaClient;
import org.qipki.commons.rest.values.params.CryptoStoreFactoryParamsValue;
import org.qipki.commons.rest.values.representations.CryptoStoreValue;
import org.qipki.crypto.storage.KeyStoreType;

public class QiPkiHttpCaClientTest
        extends AbstractQiPkiHttpCaTest
{

    @BeforeClass
    public static void startQiPkiHttpCa()
    {
        qipkiApplication = new QiPkiTestApplicationHttpCa( QiPkiHttpCaClientTest.class.getSimpleName() );
        qipkiApplication.run();
    }

    private QiPkiHttpCaClient caClient;

    @Override
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        super.assemble( module );
        ModuleAssembly config = module.layer().module( "config" );
        config.services( MemoryEntityStoreService.class );

        new QiPkiCaClientAssembler().withConfigModule( config ).assemble( module );
        config.forMixin( QiPkiCaHttpClientConfiguration.class ).declareDefaults().apiUri().set( "http://localhost:8443/api" );
    }

    @Before
    public void beforeClient()
    {
        caClient = module.<QiPkiHttpCaClient>findService( QiPkiHttpCaClient.class ).get();
    }

    @Test
    public void test()
    {
        Iterable<CryptoStoreValue> cryptoStores = caClient.cryptoStore().list( 0 );
        for ( CryptoStoreValue eachCryptoStore : cryptoStores ) {
            String csName = eachCryptoStore.name().get();
            System.out.println( "CryptoStore: " + eachCryptoStore.name().get() );
            CryptoStoreValue cs = caClient.cryptoStore().get( eachCryptoStore.uri().get() );
            assertEquals( csName, cs.name().get() );
        }
        String newName = "AnotherOne-" + System.currentTimeMillis();
        CryptoStoreFactoryParamsValue params = paramsFactory.createCryptoStoreFactoryParams( newName, KeyStoreType.JCEKS, "changeit".toCharArray() );
        CryptoStoreValue newCs = caClient.cryptoStore().create( params );
        assertEquals( newName, newCs.name().get() );
    }

}
