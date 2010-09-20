/*
 * Copyright (c) 2010 Paul Merlin <paul@nosphere.org>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.codeartisans.qipki.ca.http;

import org.codeartisans.qipki.client.ca.assembly.QiPkiCaClientAssembler;
import org.codeartisans.qipki.client.ca.services.CAClientService;
import org.codeartisans.qipki.client.ca.services.CryptoStoreClientService;
import org.codeartisans.qipki.commons.rest.values.representations.CryptoStoreValue;

import org.junit.Before;
import org.junit.Test;

import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QiPkiHttpCaClientTest
        extends AbstractQiPkiHttpCaTest
{

    private static final Logger LOGGER = LoggerFactory.getLogger( QiPkiHttpCaClientTest.class );

    private CryptoStoreClientService cryptoStoreClient;

    private CAClientService caClient;

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
        caClient = serviceLocator.<CAClientService>findService( CAClientService.class ).get();
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
