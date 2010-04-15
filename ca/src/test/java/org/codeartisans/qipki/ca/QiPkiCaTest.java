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
import org.json.JSONArray;
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
        LOGGER.debug( "CAs List:\n{}", jsonCaList );

        JSONObject caList = new JSONObject( jsonCaList );
        JSONArray items = caList.getJSONArray( "items" );
        JSONObject firstCa = items.getJSONObject( 0 );
        String firstCaUri = firstCa.getString( "uri" );

        get = new HttpGet( firstCaUri + ".html" );
        get.addHeader( "Accept", "text/html" );
        String ca = httpClient.execute( httpHost, get, strResponseHandler );
        LOGGER.debug( "FirstCA.html:\n{}", ca );

        get = new HttpGet( firstCaUri + "/crl" );
        String crl = httpClient.execute( httpHost, get, strResponseHandler );
        LOGGER.debug( "FirstCA CRL:\n{}", crl );

        KeyPair keyPair = cryptgen.generateRSAKeyPair( 512 );
        PKCS10CertificationRequest pkcs10 = cryptgen.generatePKCS10(
                new X500Principal( "CN=qipki" ), keyPair,
                new GeneralNames( new GeneralName( GeneralName.rfc822Name, "qipki@codeartisans.org" ) ) );

        HttpPost post = new HttpPost( firstCaUri + "/pkcs10signer" );
        post.addHeader( "Accept", "text/plain" );
        String pkcs10PEM = cryptio.asPEM( pkcs10 ).toString();
        LOGGER.debug( "PKCS10 as PEM:\n{}", pkcs10PEM );
        post.setEntity( new StringEntity( pkcs10PEM ) );

        String newPem = httpClient.execute( httpHost, post, strResponseHandler );
        LOGGER.debug( "Signed certificate:\n{}", newPem );

    }

}
