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
package org.codeartisans.qipki.ca;

import java.io.IOException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.codeartisans.qipki.ca.utils.QiPkiCaFixtures;
import org.codeartisans.qipki.crypto.storage.KeyStoreType;
import org.codeartisans.qipki.commons.rest.values.params.CryptoStoreFactoryParamsValue;
import org.codeartisans.qipki.commons.rest.values.representations.CryptoStoreValue;
import org.codeartisans.qipki.commons.rest.values.representations.RestListValue;
import org.codeartisans.qipki.commons.rest.values.representations.RestValue;
import static org.junit.Assert.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QiPkiCryptoStoreTest
        extends AbstractQiPkiTest
{

    private static final Logger LOGGER = LoggerFactory.getLogger( QiPkiCryptoStoreTest.class );

    @Test
    public void testListKeystores()
            throws IOException
    {
        HttpGet get = new HttpGet( qiPkiApi.cryptoStoreListUri().get() );
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
        HttpPost post = new HttpPost( qiPkiApi.cryptoStoreListUri().get() );
        addAcceptJsonHeader( post );
        String ksName = "Another KeyStore";
        CryptoStoreFactoryParamsValue params = paramsFactory.createKeyStoreFactoryParams( ksName,
                                                                                          KeyStoreType.JKS,
                                                                                          "changeit".toCharArray() );
        post.setEntity( new StringEntity( params.toJSON() ) );
        String ksJson = httpClient.execute( post, strResponseHandler );
        CryptoStoreValue ks = valueBuilderFactory.newValueFromJSON( CryptoStoreValue.class, ksJson );

        assertEquals( ksName, ks.name().get() );

    }

}
