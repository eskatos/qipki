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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.EnumSet;
import javax.security.auth.x500.X500Principal;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.codeartisans.qipki.commons.crypto.states.KeyEscrowPolicy;
import org.codeartisans.qipki.commons.crypto.values.KeyPairSpecValue;
import org.codeartisans.qipki.commons.rest.values.params.CAFactoryParamsValue;
import org.codeartisans.qipki.commons.rest.values.params.CryptoStoreFactoryParamsValue;
import org.codeartisans.qipki.commons.rest.values.params.EscrowedKeyPairFactoryParamsValue;
import org.codeartisans.qipki.commons.rest.values.params.X509FactoryParamsValue;
import org.codeartisans.qipki.commons.rest.values.params.X509ProfileFactoryParamsValue;
import org.codeartisans.qipki.commons.rest.values.params.X509RevocationParamsValue;
import org.codeartisans.qipki.commons.rest.values.representations.CAValue;
import org.codeartisans.qipki.commons.rest.values.representations.CryptoStoreValue;
import org.codeartisans.qipki.commons.rest.values.representations.EscrowedKeyPairValue;
import org.codeartisans.qipki.commons.rest.values.representations.RestListValue;
import org.codeartisans.qipki.commons.rest.values.representations.X509DetailValue;
import org.codeartisans.qipki.commons.rest.values.representations.X509ProfileValue;
import org.codeartisans.qipki.commons.rest.values.representations.X509Value;
import org.codeartisans.qipki.crypto.algorithms.AsymetricAlgorithm;
import org.codeartisans.qipki.crypto.asymetric.AsymetricGeneratorParameters;
import org.codeartisans.qipki.crypto.storage.KeyStoreType;
import org.codeartisans.qipki.crypto.x509.ExtendedKeyUsage;
import org.codeartisans.qipki.crypto.x509.KeyUsage;
import org.codeartisans.qipki.crypto.x509.NetscapeCertType;
import org.codeartisans.qipki.crypto.x509.RevocationReason;

import org.json.JSONException;
import org.json.JSONObject;

import org.junit.Test;

import org.qi4j.api.value.ValueBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QiPkiHttpCaTest
        extends AbstractQiPkiHttpCaTest
{

    private static final Logger LOGGER = LoggerFactory.getLogger( QiPkiHttpCaTest.class );

    @Test
    public void testCA()
            throws InterruptedException, IOException, JSONException, GeneralSecurityException
    {
        // Get CA list
        HttpGet get = new HttpGet( caApi.caListUri().get() );
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
        HttpPost post = new HttpPost( caApi.cryptoStoreListUri().get() );
        addAcceptJsonHeader( post );
        CryptoStoreFactoryParamsValue csParams = paramsFactory.createKeyStoreFactoryParams( "MyTestCryptoStore", KeyStoreType.JKS, "changeit".toCharArray() );
        post.setEntity( new StringEntity( csParams.toJSON() ) );
        String csJson = httpClient.execute( post, strResponseHandler );
        CryptoStoreValue cryptoStore = valueBuilderFactory.newValueFromJSON( CryptoStoreValue.class, csJson );


        // Create a new CA
        post = new HttpPost( caApi.caListUri().get() );
        addAcceptJsonHeader( post );
        KeyPairSpecValue keyPairSpec = cryptoValuesFactory.createKeySpec( AsymetricAlgorithm.RSA, 512 );
        CAFactoryParamsValue caParams = paramsFactory.createCAFactoryParams( cryptoStore.uri().get(), "MyTestCA", "CN=MyTestCA", keyPairSpec, null );
        post.setEntity( new StringEntity( caParams.toJSON() ) );
        caJson = httpClient.execute( post, strResponseHandler );
        ca = valueBuilderFactory.newValueFromJSON( CAValue.class, caJson );


        // Create a new X509Profile
        post = new HttpPost( caApi.x509ProfileListUri().get() );
        addAcceptJsonHeader( post );
        X509ProfileFactoryParamsValue profileParams = paramsFactory.createX509ProfileFactoryParams(
                "SSLClient", "A simple SSLClient x509 profile for unit tests",
                x509ExtValuesFactory.buildKeyUsagesValue( true, EnumSet.of( KeyUsage.keyEncipherment, KeyUsage.digitalSignature ) ),
                x509ExtValuesFactory.buildExtendedKeyUsagesValue( false, EnumSet.of( ExtendedKeyUsage.clientAuth ) ),
                x509ExtValuesFactory.buildNetscapeCertTypesValue( false, EnumSet.of( NetscapeCertType.sslClient ) ),
                x509ExtValuesFactory.buildBasicConstraintsValue( true, false, 0 ),
                null );
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
        LOGGER.debug( "Will request a new X509 with the following PKCS#10: " + pkcs10PEM );
        X509FactoryParamsValue x509FactoryParams = paramsFactory.createX509FactoryParams( ca.uri().get(), sslClientProfile.uri().get(), pkcs10PEM );
        post = new HttpPost( caApi.x509ListUri().get() );
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
        get = new HttpGet( caApi.x509ListUri().get() );
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


        // Get KeyPair list
        get = new HttpGet( caApi.escrowedKeyPairListUri().get() );
        addAcceptJsonHeader( get );
        String jsonKeyPairList = httpClient.execute( get, strResponseHandler );
        LOGGER.debug( "EscrowedKeyPair List: {}", new JSONObject( jsonKeyPairList ).toString( 2 ) );


        // Create KeyPair
        EscrowedKeyPairFactoryParamsValue escrowParams = paramsFactory.createEscrowedKeyPairFactoryParams( AsymetricAlgorithm.RSA, 512 );
        post = new HttpPost( caApi.escrowedKeyPairListUri().get() );
        addAcceptJsonHeader( post );
        post.setEntity( new StringEntity( escrowParams.toJSON() ) );
        String jsonEscrowed = httpClient.execute( post, strResponseHandler );
        LOGGER.debug( "EscrowedKeyPair : {}", new JSONObject( jsonEscrowed ).toString( 2 ) );
        EscrowedKeyPairValue ekp = valueBuilderFactory.newValueFromJSON( EscrowedKeyPairValue.class, jsonEscrowed );


        // Recover KeyPair
        get = new HttpGet( ekp.recoveryUri().get() );
        addAcceptJsonHeader( get );
        String kpPem = httpClient.execute( get, strResponseHandler );
        LOGGER.debug( "EscrowedKeyPair PEM: {}", kpPem );
        KeyPair keypair = cryptio.readKeyPairPEM( new StringReader( kpPem ) );


        // Issue X509Certificate using an escrowed keypair
        String dn = "CN=qipki-escrowed";
        LOGGER.debug( "Will request a new X509 with the following DN: " + dn );
        x509FactoryParams = paramsFactory.createX509FactoryParams( ca.uri().get(), sslClientProfile.uri().get(), ekp.uri().get(), dn );
        post = new HttpPost( caApi.x509ListUri().get() );
        addAcceptJsonHeader( post );
        post.setEntity( new StringEntity( x509FactoryParams.toJSON() ) );
        jsonX509 = httpClient.execute( post, strResponseHandler );
        newX509 = valueBuilderFactory.newValueFromJSON( X509Value.class, jsonX509 );
        LOGGER.debug( "New X509 created using /api/x509/factory and an escrowed keypair after POST/302/REDIRECT: {}", newX509.toJSON() );


        // Getting new X509 PEM
        get = new HttpGet( newX509.pemUri().get() );
        String x509pem = httpClient.execute( get, strResponseHandler );
        LOGGER.debug( "X509 created from escrowed keypair PEM: {}", x509pem );
        X509Certificate x509Certificate = cryptio.readX509PEM( new StringReader( x509pem ) );


        // Getting EscrowedKeyPair from X509Certificate
        get = new HttpGet( newX509.recoveryUri().get() );
        kpPem = httpClient.execute( get, strResponseHandler );
        LOGGER.debug( "EscrowedKeyPair PEM: {}", kpPem );
        keypair = cryptio.readKeyPairPEM( new StringReader( kpPem ) );


        // Create local PKCS#12 keystore with keypair, certificate and full certchain
        char[] password = "changeit".toCharArray();
        KeyStore ks = KeyStore.getInstance( KeyStoreType.PKCS12.typeString(), BouncyCastleProvider.PROVIDER_NAME );
        ks.load( null, password );
        ks.setEntry( "wow", new KeyStore.PrivateKeyEntry( keyPair.getPrivate(), new Certificate[]{ x509Certificate } ), new KeyStore.PasswordProtection( password ) );
        String base64encodedp12 = cryptio.base64Encode( ks, password );
        System.out.println( base64encodedp12 );


        // Exporting CA in a PKCS#12 keystore
        get = new HttpGet( ca.exportUri().get() + "?password=changeit" );
        byte[] responseBytes = httpClient.execute( get, bytesResponseHandler );
        ks = KeyStore.getInstance( KeyStoreType.PKCS12.typeString(), BouncyCastleProvider.PROVIDER_NAME );
        ks.load( new ByteArrayInputStream( responseBytes ), password );
        base64encodedp12 = cryptio.base64Encode( ks, password );
        System.out.println( base64encodedp12 );


        // Exporting CA in a JKS keystore
        get = new HttpGet( ca.exportUri().get() + "?password=changeit&kstype=jks" );
        responseBytes = httpClient.execute( get, bytesResponseHandler );
        ks = KeyStore.getInstance( KeyStoreType.JKS.typeString() );
        ks.load( new ByteArrayInputStream( responseBytes ), password );
        base64encodedp12 = cryptio.base64Encode( ks, password );
        System.out.println( base64encodedp12 );


    }

}
