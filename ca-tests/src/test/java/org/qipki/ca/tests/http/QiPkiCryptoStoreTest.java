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

import org.qipki.ca.tests.QiPkiCaFixtures;
import java.io.IOException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import org.qipki.crypto.storage.KeyStoreType;
import org.qipki.commons.rest.values.params.CryptoStoreFactoryParamsValue;
import org.qipki.commons.rest.values.representations.CryptoStoreValue;
import org.qipki.commons.rest.values.representations.RestListValue;
import org.qipki.commons.rest.values.representations.RestValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QiPkiCryptoStoreTest
        extends AbstractQiPkiHttpCaTest
{

    private static final Logger LOGGER = LoggerFactory.getLogger( QiPkiCryptoStoreTest.class );

    @BeforeClass
    public static void startQiPkiHttpCa()
    {
        qipkiServer = new QiPkiTestApplicationHttpCa( QiPkiCryptoStoreTest.class.getSimpleName() );
        qipkiServer.run();
    }

    @Test
    public void testListKeystores()
            throws IOException
    {
        HttpGet get = new HttpGet( caApi.cryptoStoreListUri().get() );
        addAcceptJsonHeader( get );

        String jsonKsList = httpClient.execute( get, strResponseHandler );

        LOGGER.debug( "KEYSTORES: {}", jsonKsList );

        RestListValue ksList = valueBuilderFactory.newValueFromJSON( RestListValue.class, jsonKsList );

        assertEquals( 1, ksList.items().get().size() );

        RestValue value = ksList.items().get().get( 0 );

        CryptoStoreValue ks = ( CryptoStoreValue ) value;

        assertEquals( QiPkiCaFixtures.KEYSTORE_NAME, ks.name().get() );

    }

    @Test
    public void testCreateKeystore()
            throws IOException
    {
        HttpPost post = new HttpPost( caApi.cryptoStoreListUri().get() );
        addAcceptJsonHeader( post );
        String ksName = "Another KeyStore";
        CryptoStoreFactoryParamsValue params = paramsFactory.createCryptoStoreFactoryParams( ksName,
                                                                                             KeyStoreType.JKS,
                                                                                             "changeit".toCharArray() );
        post.setEntity( new StringEntity( params.toJSON() ) );
        String ksJson = httpClient.execute( post, strResponseHandler );
        LOGGER.info( "testCreateKeyStore JSON: " + ksJson );
        CryptoStoreValue ks = valueBuilderFactory.newValueFromJSON( CryptoStoreValue.class, ksJson );

        assertEquals( ksName, ks.name().get() );

    }

}
