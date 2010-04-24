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
import org.codeartisans.qipki.commons.values.params.X509FactoryParamsValue;
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
    private static final String ACCEPT = "Accept";
    private static final String JSON = "application/json";
    private static final String HTML = "test/html";
    private static final String TEXT = "text/plain";

    @Test
    public void testCA()
            throws InterruptedException, IOException, JSONException
    {

        // Get CA list
        HttpGet get = new HttpGet( "/api/ca" );
        get.addHeader( ACCEPT, JSON );
        String jsonCaList = httpClient.execute( httpHost, get, strResponseHandler );
        LOGGER.debug( "CAs List: {}", new JSONObject( jsonCaList ).toString( 2 ) );
        RestListValue caList = valueBuilderFactory.newValueFromJSON( RestListValue.class, jsonCaList );
        CAValue firstCa = ( CAValue ) caList.items().get().get( 0 );
        String firstCaUri = firstCa.uri().get();


        // Get first CA as HTML
        get = new HttpGet( firstCaUri + ".html" );
        get.addHeader( ACCEPT, HTML );
        String ca = httpClient.execute( httpHost, get, strResponseHandler );
        LOGGER.debug( "First CA HTML:\n{}", ca );


        // Get first CA as Value
        get = new HttpGet( firstCaUri );
        get.addHeader( ACCEPT, JSON );
        String caJson = httpClient.execute( httpHost, get, strResponseHandler );
        CAValue caValue = valueBuilderFactory.newValueFromJSON( CAValue.class, caJson );
        LOGGER.debug( "First CA JSON:\n{}", caValue.toJSON() );


        // Get first CA CRL
        get = new HttpGet( firstCaUri + "/crl" );
        String crl = httpClient.execute( httpHost, get, strResponseHandler );
        LOGGER.debug( "First CA CRL:\n{}", crl );


        // Request certificate on X509Factory with a PKCS#10 request using the first CA
        KeyPair keyPair = asymGenerator.generateKeyPair( new AsymetricGeneratorParameters( AsymetricAlgorithm.RSA, 512 ) );
        PKCS10CertificationRequest pkcs10 = x509Generator.generatePKCS10(
                new X500Principal( "CN=qipki" ), keyPair,
                new GeneralNames( new GeneralName( GeneralName.rfc822Name, "qipki@codeartisans.org" ) ) );
        String pkcs10PEM = cryptio.asPEM( pkcs10 ).toString();
        X509FactoryParamsValue x509FactoryParams = paramsFactory.createX509FactoryParams( caValue.identity().get(), pkcs10PEM );
        HttpPost post = new HttpPost( "/api/x509/factory" );
        post.addHeader( ACCEPT, JSON );
        post.setEntity( new StringEntity( x509FactoryParams.toJSON() ) );
        String jsonX509 = httpClient.execute( httpHost, post, strResponseHandler );
        // LOGGER.debug( "New X509 created using /api/x509/factory after POST/302/REDIRECT: {}", new JSONObject( jsonX509 ).toString( 2 ) );
        X509Value newX509 = valueBuilderFactory.newValueFromJSON( X509Value.class, jsonX509 );
        LOGGER.debug( "New X509 created using /api/x509/factory after POST/302/REDIRECT: {}", newX509.toJSON() );


        // Get detailled info about new X509
        get = new HttpGet( newX509.uri().get() + "/detail" );
        get.addHeader( ACCEPT, JSON );
        String jsonX509Detail = httpClient.execute( httpHost, get, strResponseHandler );
        LOGGER.debug( "New X509 detail: {}", new JSONObject( jsonX509Detail ).toString( 2 ) );
        X509DetailValue newX509Detail = valueBuilderFactory.newValueFromJSON( X509DetailValue.class, jsonX509Detail );


        // Get X509 list
        get = new HttpGet( "/api/x509" );
        get.addHeader( ACCEPT, JSON );
        String jsonX509List = httpClient.execute( httpHost, get, strResponseHandler );
        LOGGER.debug( "X509s List: {}", new JSONObject( jsonX509List ).toString( 2 ) );
        RestListValue x509List = valueBuilderFactory.newValueFromJSON( RestListValue.class, jsonX509List );
        X509Value firstX509 = ( X509Value ) x509List.items().get().get( 0 );


        // Get first X509
        get = new HttpGet( firstX509.uri().get() );
        get.addHeader( ACCEPT, JSON );
        jsonX509 = httpClient.execute( httpHost, get, strResponseHandler );
        LOGGER.debug( "First X509: {}", new JSONObject( jsonX509 ).toString( 2 ) );
        firstX509 = valueBuilderFactory.newValueFromJSON( X509Value.class, jsonX509 );

    }

}
