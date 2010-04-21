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
import java.security.KeyPair;
import javax.security.auth.x500.X500Principal;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.codeartisans.qipki.commons.values.rest.CAValue;
import org.codeartisans.qipki.commons.values.rest.RestListValue;
import org.codeartisans.qipki.commons.values.rest.X509DetailValue;
import org.codeartisans.qipki.commons.values.rest.X509Value;
import org.codeartisans.qipki.crypto.algorithms.AsymetricAlgorithm;
import org.codeartisans.qipki.core.crypto.asymetric.AsymetricGeneratorParameters;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QiPkiCaTest
        extends AbstractQiPkiTest
{

    private static final Logger LOGGER = LoggerFactory.getLogger( QiPkiCaTest.class );

    @Test
    public void testCA()
            throws InterruptedException, IOException, JSONException
    {

        HttpGet get = new HttpGet( "/api/ca" );
        get.addHeader( "Accept", "application/json" );

        String jsonCaList = httpClient.execute( httpHost, get, strResponseHandler );
        LOGGER.debug( "CAs List: {}", new JSONObject( jsonCaList ).toString( 2 ) );

        RestListValue caList = valueBuilderFactory.newValueFromJSON( RestListValue.class, jsonCaList );
        CAValue firstCa = ( CAValue ) caList.items().get().get( 0 );
        String firstCaUri = firstCa.uri().get();

        get = new HttpGet( firstCaUri + ".html" );
        get.addHeader( "Accept", "text/html" );
        String ca = httpClient.execute( httpHost, get, strResponseHandler );
        LOGGER.debug( "First CA HTML:\n{}", ca );

        get = new HttpGet( firstCaUri + "/crl" );
        String crl = httpClient.execute( httpHost, get, strResponseHandler );
        LOGGER.debug( "First CA CRL:\n{}", crl );

        KeyPair keyPair = asymGenerator.generateKeyPair( new AsymetricGeneratorParameters( AsymetricAlgorithm.RSA, 512 ) );
        PKCS10CertificationRequest pkcs10 = x509Generator.generatePKCS10(
                new X500Principal( "CN=qipki" ), keyPair,
                new GeneralNames( new GeneralName( GeneralName.rfc822Name, "qipki@codeartisans.org" ) ) );

        HttpPost post = new HttpPost( firstCaUri + "/pkcs10signer" );
        post.addHeader( "Accept", "text/plain" );
        String pkcs10PEM = cryptio.asPEM( pkcs10 ).toString();
        LOGGER.debug( "PKCS10 as PEM:\n{}", pkcs10PEM );
        post.setEntity( new StringEntity( pkcs10PEM ) );

        String newPem = httpClient.execute( httpHost, post, strResponseHandler );
        LOGGER.debug( "Signed certificate:\n{}", newPem );

        get = new HttpGet( "/api/x509" );
        get.addHeader( "Accept", "application/json" );

        String jsonX509List = httpClient.execute( httpHost, get, strResponseHandler );
        LOGGER.debug( "X509s List: {}", new JSONObject( jsonX509List ).toString( 2 ) );

        RestListValue x509List = valueBuilderFactory.newValueFromJSON( RestListValue.class, jsonX509List );
        X509Value firstX509 = ( X509Value ) x509List.items().get().get( 0 );

        get = new HttpGet( firstX509.uri().get() );
        get.addHeader( "Accept", "application/json" );
        String jsonX509 = httpClient.execute( httpHost, get, strResponseHandler );
        LOGGER.debug( "First X509: {}", new JSONObject( jsonX509 ).toString( 2 ) );

        firstX509 = valueBuilderFactory.newValueFromJSON( X509Value.class, jsonX509 );

        get = new HttpGet( firstX509.uri().get() + "/detail" );
        get.addHeader( "Accept", "application/json" );
        String jsonX509Detail = httpClient.execute( httpHost, get, strResponseHandler );
        LOGGER.debug( "First X509 detail: {}", new JSONObject( jsonX509Detail ).toString( 2 ) );

        X509DetailValue firstX509Detail = valueBuilderFactory.newValueFromJSON( X509DetailValue.class, jsonX509Detail );
    }

}
