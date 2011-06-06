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
package org.qipki.ca.http;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;

import org.qipki.ca.http.utils.QiPkiTestApplicationHttpCa;
import org.qipki.client.ca.assembly.QiPkiCaClientAssembler;
import org.qipki.client.ca.services.CryptoStoreClientService;
import org.qipki.commons.rest.values.representations.CryptoStoreValue;

public class QiPkiHttpCaClientTest
        extends AbstractQiPkiHttpCaTest
{

    @BeforeClass
    public static void startQiPkiHttpCa()
    {
        qipkiServer = new QiPkiTestApplicationHttpCa( QiPkiHttpCaClientTest.class.getSimpleName() );
        qipkiServer.run();
    }

    private CryptoStoreClientService cryptoStoreClient;

    @Override
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        new QiPkiCaClientAssembler().assemble( module );
    }

    @Before
    public void beforeClient()
    {
        cryptoStoreClient = serviceLocator.<CryptoStoreClientService>findService( CryptoStoreClientService.class ).get();
    }

    @Test
    public void test()
    {
        Iterable<CryptoStoreValue> cryptoStores = cryptoStoreClient.list( 0 );
        for ( CryptoStoreValue eachCryptoStore : cryptoStores ) {
            System.out.println( "CryptoStore: " + eachCryptoStore.name().get() );
        }
    }

}
