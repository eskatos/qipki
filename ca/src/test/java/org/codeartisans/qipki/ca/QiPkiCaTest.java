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
import org.codeartisans.qipki.commons.states.KeyEscrowPolicy;
import org.codeartisans.qipki.commons.values.crypto.KeyPairSpecValue;
import org.codeartisans.qipki.commons.values.params.CAFactoryParamsValue;
import org.codeartisans.qipki.commons.values.params.CryptoStoreFactoryParamsValue;
import org.codeartisans.qipki.commons.values.params.X509FactoryParamsValue;
import org.codeartisans.qipki.commons.values.params.X509ProfileFactoryParamsValue;
import org.codeartisans.qipki.commons.values.params.X509RevocationParamsValue;
import org.codeartisans.qipki.commons.values.rest.CAValue;
import org.codeartisans.qipki.commons.values.rest.CryptoStoreValue;
import org.codeartisans.qipki.commons.values.rest.RestListValue;
import org.codeartisans.qipki.commons.values.rest.X509DetailValue;
import org.codeartisans.qipki.commons.values.rest.X509ProfileValue;
import org.codeartisans.qipki.commons.values.rest.X509Value;
import org.codeartisans.qipki.crypto.algorithms.AsymetricAlgorithm;
import org.codeartisans.qipki.crypto.asymetric.AsymetricGeneratorParameters;
import org.codeartisans.qipki.crypto.storage.KeyStoreType;
import org.codeartisans.qipki.crypto.x509.RevocationReason;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.qi4j.api.value.ValueBuilder;
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
        // Get CA list
        HttpGet get = new HttpGet( qiPkiApi.caListUri().get() );
        addAcceptJsonHeader( get );
        String jsonCaList = httpClient.execute( get, strResponseHandler );
        LOGGER.debug( "CAs List: {}", new JSONObject( jsonCaList ).toString( 2 ) );
        RestListValue caList = valueBuilderFactory.newValueFromJSON( RestListValue.class, jsonCaList );
        CAValue firstCa = ( CAValue ) caList.items().get().get( 0 );


        // Get first CA as Value
        get = new HttpGet( firstCa.uri().get() );
        addAcceptJsonHeader( get );
        String caJson = httpClient.execute( get, strResponseHandler );
        CAValue ca = valueBuilderFactory.newValueFromJSON( CAValue.class, caJson );
        LOGGER.debug( "First CA JSON:\n{}", ca.toJSON() );


        // Get first CA CRL
        get = new HttpGet( ca.crlUri().get() );
        String crl = httpClient.execute( get, strResponseHandler );
        LOGGER.debug( "First CA CRL:\n{}", crl );


        // Create a new CryptoStore
        HttpPost post = new HttpPost( qiPkiApi.cryptoStoreFactoryUri().get() );
        addAcceptJsonHeader( post );
        CryptoStoreFactoryParamsValue csParams = paramsFactory.createKeyStoreFactoryParams( "MyTestCryptoStore", KeyStoreType.JKS, "changeit".toCharArray() );
        post.setEntity( new StringEntity( csParams.toJSON() ) );
        String csJson = httpClient.execute( post, strResponseHandler );
        CryptoStoreValue cryptoStore = valueBuilderFactory.newValueFromJSON( CryptoStoreValue.class, csJson );


        // Create a new CA
        post = new HttpPost( qiPkiApi.caFactoryUri().get() );
        addAcceptJsonHeader( post );
        KeyPairSpecValue keyPairSpec = cryptoValuesFactory.createKeySpec( AsymetricAlgorithm.RSA, 512 );
        CAFactoryParamsValue caParams = paramsFactory.createCAFactoryParams( cryptoStore.uri().get(), "MyTestCA", "CN=MyTestCA", keyPairSpec, null );
        post.setEntity( new StringEntity( caParams.toJSON() ) );
        caJson = httpClient.execute( post, strResponseHandler );
        ca = valueBuilderFactory.newValueFromJSON( CAValue.class, caJson );


        // Create a new X509Profile
        post = new HttpPost( qiPkiApi.x509ProfileFactoryUri().get() );
        addAcceptJsonHeader( post );
        X509ProfileFactoryParamsValue profileParams = paramsFactory.createX509ProfileFactoryParams( "SSLClient" );
        post.setEntity( new StringEntity( profileParams.toJSON() ) );
        String sslClientProfileJson = httpClient.execute( post, strResponseHandler );
        X509ProfileValue sslClientProfile = valueBuilderFactory.newValueFromJSON( X509ProfileValue.class, sslClientProfileJson );


        // Add profile to CA
        post = new HttpPost( ca.uri().get() );
        addAcceptJsonHeader( post );
        ValueBuilder<CAValue> caValueBuilder = valueBuilderFactory.newValueBuilder( CAValue.class ).withPrototype( ca ); // Needed as Values are immutables
        ca = caValueBuilder.prototype();
        ca.allowedX509Profiles().get().add( paramsFactory.createX509ProfileAssignment( sslClientProfile.uri().get(), KeyEscrowPolicy.allowed ) );
        ca = caValueBuilder.newInstance();
        post.setEntity( new StringEntity( ca.toJSON() ) );
        caJson = httpClient.execute( post, strResponseHandler );
        ca = valueBuilderFactory.newValueFromJSON( CAValue.class, caJson );


        // Request certificate on X509Factory with a PKCS#10 request using the first CA
        KeyPair keyPair = asymGenerator.generateKeyPair( new AsymetricGeneratorParameters( AsymetricAlgorithm.RSA, 512 ) );
        PKCS10CertificationRequest pkcs10 = x509Generator.generatePKCS10(
                new X500Principal( "CN=qipki" ), keyPair,
                new GeneralNames( new GeneralName( GeneralName.rfc822Name, "qipki@codeartisans.org" ) ) );
        String pkcs10PEM = cryptio.asPEM( pkcs10 ).toString();
        X509FactoryParamsValue x509FactoryParams = paramsFactory.createX509FactoryParams( ca.uri().get(), pkcs10PEM );
        post = new HttpPost( qiPkiApi.x509FactoryUri().get() );
        addAcceptJsonHeader( post );
        post.setEntity( new StringEntity( x509FactoryParams.toJSON() ) );
        String jsonX509 = httpClient.execute( post, strResponseHandler );
        X509Value newX509 = valueBuilderFactory.newValueFromJSON( X509Value.class, jsonX509 );
        LOGGER.debug( "New X509 created using /api/x509/factory after POST/302/REDIRECT: {}", newX509.toJSON() );


        // Get detailled info about new X509
        get = new HttpGet( newX509.detailUri().get() );
        addAcceptJsonHeader( get );
        String jsonX509Detail = httpClient.execute( get, strResponseHandler );
        LOGGER.debug( "New X509 detail: {}", new JSONObject( jsonX509Detail ).toString( 2 ) );
        X509DetailValue newX509Detail = valueBuilderFactory.newValueFromJSON( X509DetailValue.class, jsonX509Detail );


        // Get X509 list
        get = new HttpGet( qiPkiApi.x509ListUri().get() );
        addAcceptJsonHeader( get );
        String jsonX509List = httpClient.execute( get, strResponseHandler );
        LOGGER.debug( "X509s List: {}", new JSONObject( jsonX509List ).toString( 2 ) );
        RestListValue x509List = valueBuilderFactory.newValueFromJSON( RestListValue.class, jsonX509List );
        X509Value firstX509 = ( X509Value ) x509List.items().get().get( 0 );


        // Get first X509
        get = new HttpGet( firstX509.uri().get() );
        addAcceptJsonHeader( get );
        jsonX509 = httpClient.execute( get, strResponseHandler );
        LOGGER.debug( "First X509: {}", new JSONObject( jsonX509 ).toString( 2 ) );
        firstX509 = valueBuilderFactory.newValueFromJSON( X509Value.class, jsonX509 );


        // Revoke first X509
        X509RevocationParamsValue x509RevocationParams = paramsFactory.createX509RevocationParams( RevocationReason.cessationOfOperation );
        post = new HttpPost( firstX509.revocationUri().get() );
        addAcceptJsonHeader( post );
        post.setEntity( new StringEntity( x509RevocationParams.toJSON() ) );
        String jsonRevocation = httpClient.execute( post, strResponseHandler );
        LOGGER.debug( jsonRevocation );
    }

}
