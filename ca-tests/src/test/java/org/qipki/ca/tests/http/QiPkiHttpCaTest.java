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

import info.aduna.io.FileUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.qi4j.api.value.ValueBuilder;

import org.qipki.commons.crypto.states.KeyEscrowPolicy;
import org.qipki.commons.crypto.values.KeyPairSpecValue;
import org.qipki.commons.rest.values.params.CAFactoryParamsValue;
import org.qipki.commons.rest.values.params.CryptoStoreFactoryParamsValue;
import org.qipki.commons.rest.values.params.EscrowedKeyPairFactoryParamsValue;
import org.qipki.commons.rest.values.params.X509FactoryParamsValue;
import org.qipki.commons.rest.values.params.X509ProfileFactoryParamsValue;
import org.qipki.commons.rest.values.params.X509RevocationParamsValue;
import org.qipki.commons.rest.values.representations.CAValue;
import org.qipki.commons.rest.values.representations.CryptoStoreValue;
import org.qipki.commons.rest.values.representations.EscrowedKeyPairValue;
import org.qipki.commons.rest.values.representations.RestListValue;
import org.qipki.commons.rest.values.representations.X509DetailValue;
import org.qipki.commons.rest.values.representations.X509ProfileValue;
import org.qipki.commons.rest.values.representations.X509Value;
import org.qipki.crypto.algorithms.AsymetricAlgorithm;
import org.qipki.crypto.asymetric.AsymetricGeneratorParameters;
import org.qipki.crypto.storage.KeyStoreType;
import org.qipki.crypto.x509.DistinguishedName;
import org.qipki.crypto.x509.ExtendedKeyUsage;
import org.qipki.crypto.x509.KeyUsage;
import org.qipki.crypto.x509.NetscapeCertType;
import org.qipki.crypto.x509.RevocationReason;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Ignore;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qipki.commons.crypto.values.x509.X509GeneralNameValue;
import org.qipki.crypto.x509.X509GeneralName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QiPkiHttpCaTest
        extends AbstractQiPkiHttpCaTest
{

    private static final Logger LOGGER = LoggerFactory.getLogger( QiPkiHttpCaTest.class );

    @BeforeClass
    public static void startQiPkiApplication()
    {
        qipkiApplication = new QiPkiTestApplicationHttpCa( QiPkiHttpCaTest.class.getSimpleName() );
        qipkiApplication.run();
    }

    private String testCryptoStoreName = "MyCryptoStore";
    private String testCaName = "MyCa";

    @Test
    public void testOnce()
            throws InterruptedException, IOException, JSONException, GeneralSecurityException
    {
        testCA();
    }

    @Test
    @Ignore // DO NOT WORK !!!
    public void testReindex()
            throws IOException, JSONException
    {
        qipkiApplication.stop();

        LOGGER.info( "WILL DELETE INDEX REPOSITORY" );
        FileUtil.deltree( new File( "target/qi4j-index" ) );
        LOGGER.info( "INDEX REPOSITORY DELETED" );

        qipkiApplication.run();

        LOGGER.info( "HAS INDEX REPOSITORY BEEN REFILLED?" );
        // Get CA list
        HttpGet get = new HttpGet( caApi.caListUri().get() );
        addAcceptJsonHeader( get );
        String jsonCaList = httpClient.execute( get, strResponseHandler );
        LOGGER.debug( "CAs List: {}", new JSONObject( jsonCaList ).toString( 2 ) );
        RestListValue caList = module.newValueFromJSON( RestListValue.class, jsonCaList );
        assertEquals( 4, caList.items().get().size() );
        LOGGER.info( "INDEX REPOSITORY BEEN REFILLED SUCCESSFULLY" );
    }

    @Test
    public void testRepeatedly()
            throws InterruptedException, IOException, JSONException, GeneralSecurityException
    {
        for ( int i = 0; i < 10; i++ ) {
            testCryptoStoreName = "MyTestCryptoStore" + i;
            testCaName = "MyTestCa" + i;
            testCA();
        }
    }

    @Test
    @Ignore // Ignored for speeding up the release process
    // Run 500 testCA using 20 threads doing 25 testCA each
    public void testMultiThreaded()
            throws InterruptedException
    {
        List<Thread> threads = new ArrayList<Thread>();
        List<TestRunnable> runnables = new ArrayList<TestRunnable>();
        for ( int i = 0; i < 20; i++ ) {
            TestRunnable runnable = new TestRunnable( this, i );
            runnables.add( runnable );
            Thread thread = new Thread( runnable );
            threads.add( thread );
            thread.start();
        }
        for ( Thread eachThread : threads ) {
            eachThread.join();
        }
        List<Exception> failures = new ArrayList<Exception>();
        for ( TestRunnable eachRunnable : runnables ) {
            if ( eachRunnable.failure != null ) {
                failures.add( eachRunnable.failure );
                LOGGER.info( "One thread failed because of '{}'", eachRunnable.failure.getMessage(), eachRunnable.failure );
            }
        }
        if ( !failures.isEmpty() ) {
            LOGGER.info( "Multithreaded test failed with {} failures", failures.size() );
            fail( "Multithreaded test failed with " + failures.size() + " failures" );
        }
    }

    private class TestRunnable
            implements Runnable
    {

        private final int number;
        private final QiPkiHttpCaTest test;
        private Exception failure;

        public TestRunnable( QiPkiHttpCaTest test, int number )
        {
            this.test = test;
            this.number = number;
        }

        @Override
        public void run()
        {
            for ( int i = 0; i < 25; i++ ) {
                test.testCryptoStoreName = "ThreadedTestCryptoStore" + number + "-" + i;
                test.testCaName = "ThreadedTestCa" + number + "-" + i;
                try {
                    test.testCA();
                } catch ( Exception ex ) {
                    failure = ex;
                    break;
                }
            }
        }

    }

    private void testCA()
            throws InterruptedException, IOException, JSONException, GeneralSecurityException
    {
        // Get CA list
        HttpGet get = new HttpGet( caApi.caListUri().get() );
        addAcceptJsonHeader( get );
        String jsonCaList = httpClient.execute( get, strResponseHandler );
        LOGGER.debug( "CAs List: {}", new JSONObject( jsonCaList ).toString( 2 ) );
        RestListValue caList = module.newValueFromJSON( RestListValue.class, jsonCaList );
        CAValue firstCa = ( CAValue ) caList.items().get().get( 0 );

        // Get first CA as Value
        get = new HttpGet( firstCa.uri().get() );
        addAcceptJsonHeader( get );
        String caJson = httpClient.execute( get, strResponseHandler );
        CAValue ca = module.newValueFromJSON( CAValue.class, caJson );
        LOGGER.debug( "First CA JSON:\n{}", ca.toString() );


        // Get first CA CRL
        get = new HttpGet( ca.crlUri().get() );
        String crl = httpClient.execute( get, strResponseHandler );
        LOGGER.debug( "First CA CRL:\n{}", crl );
        X509CRL x509CRL = cryptio.readCRLPEM( new StringReader( crl ) );


        // Create a new CryptoStore
        HttpPost post = new HttpPost( caApi.cryptoStoreListUri().get() );
        addAcceptJsonHeader( post );
        CryptoStoreFactoryParamsValue csParams = paramsFactory.createCryptoStoreFactoryParams( testCryptoStoreName, KeyStoreType.JKS, "changeit".toCharArray() );
        post.setEntity( new StringEntity( csParams.toString() ) );
        String csJson = httpClient.execute( post, strResponseHandler );
        CryptoStoreValue cryptoStore = module.newValueFromJSON( CryptoStoreValue.class, csJson );


        // Create a new CA
        post = new HttpPost( caApi.caListUri().get() );
        addAcceptJsonHeader( post );
        KeyPairSpecValue keyPairSpec = cryptoValuesFactory.createKeySpec( AsymetricAlgorithm.RSA, 512 );
        CAFactoryParamsValue caParams = paramsFactory.createCAFactoryParams( cryptoStore.uri().get(), testCaName, 1, "CN=" + testCaName, keyPairSpec, null );
        post.setEntity( new StringEntity( caParams.toString() ) );
        caJson = httpClient.execute( post, strResponseHandler );
        ca = module.newValueFromJSON( CAValue.class, caJson );


        // Create a new X509Profile
        post = new HttpPost( caApi.x509ProfileListUri().get() );
        addAcceptJsonHeader( post );
        X509ProfileFactoryParamsValue profileParams = paramsFactory.createX509ProfileFactoryParams(
                "SSLClient", 1, "A simple SSLClient x509 profile for unit tests",
                x509ExtValuesFactory.buildKeyUsagesValue( true, EnumSet.of( KeyUsage.keyEncipherment, KeyUsage.digitalSignature ) ),
                x509ExtValuesFactory.buildExtendedKeyUsagesValue( false, EnumSet.of( ExtendedKeyUsage.clientAuth ) ),
                x509ExtValuesFactory.buildNetscapeCertTypesValue( false, EnumSet.of( NetscapeCertType.sslClient ) ),
                x509ExtValuesFactory.buildBasicConstraintsValue( true, false, 0 ),
                null );
        post.setEntity( new StringEntity( profileParams.toString() ) );
        String sslClientProfileJson = httpClient.execute( post, strResponseHandler );
        X509ProfileValue sslClientProfile = module.newValueFromJSON( X509ProfileValue.class, sslClientProfileJson );


        // Add profile to CA
        post = new HttpPost( ca.uri().get() );
        addAcceptJsonHeader( post );
        ValueBuilder<CAValue> caValueBuilder = module.newValueBuilderWithPrototype( ca ); // Needed as Values are immutables
        ca = caValueBuilder.prototype();
        ca.allowedX509Profiles().get().add( paramsFactory.createX509ProfileAssignment( sslClientProfile.uri().get(), KeyEscrowPolicy.allowed ) );
        ca = caValueBuilder.newInstance();
        post.setEntity( new StringEntity( ca.toString() ) );
        caJson = httpClient.execute( post, strResponseHandler );
        ca = module.newValueFromJSON( CAValue.class, caJson );


        // Request certificate on X509Factory with a PKCS#10 request using the first CA
        KeyPair keyPair = asymGenerator.generateKeyPair( new AsymetricGeneratorParameters( AsymetricAlgorithm.RSA, 512 ) );
        PKCS10CertificationRequest pkcs10 = x509Generator.generatePKCS10(
                new DistinguishedName( "CN=qipki" ), keyPair,
                new GeneralNames( new DERSequence( new ASN1Encodable[]
                {
                    new GeneralName( GeneralName.rfc822Name, "qipki@codeartisans.org" ),
                    new GeneralName( GeneralName.dNSName, "qipki.org" )
                } ) ) );
        String pkcs10PEM = cryptio.asPEM( pkcs10 ).toString();
        LOGGER.debug( "Will request a new X509 with the following PKCS#10: " + pkcs10PEM );
        X509FactoryParamsValue x509FactoryParams = paramsFactory.createX509FactoryParams( ca.uri().get(), sslClientProfile.uri().get(), pkcs10PEM );
        post = new HttpPost( caApi.x509ListUri().get() );
        addAcceptJsonHeader( post );
        post.setEntity( new StringEntity( x509FactoryParams.toString() ) );
        String jsonX509 = httpClient.execute( post, strResponseHandler );
        X509Value newX509 = module.newValueFromJSON( X509Value.class, jsonX509 );
        LOGGER.debug( "New X509 created using /api/x509/factory after POST/302/REDIRECT: {}", newX509.toString() );


        // Get detailled info about new X509
        get = new HttpGet( newX509.detailUri().get() );
        addAcceptJsonHeader( get );
        String jsonX509Detail = httpClient.execute( get, strResponseHandler );
        LOGGER.debug( "New X509 detail: {}", new JSONObject( jsonX509Detail ).toString( 2 ) );
        X509DetailValue newX509Detail = module.newValueFromJSON( X509DetailValue.class, jsonX509Detail );

        assertTrue( newX509Detail.keysExtensions().get().extendedKeyUsages().get().extendedKeyUsages().get().contains( ExtendedKeyUsage.clientAuth ) );
        assertTrue( newX509Detail.keysExtensions().get().netscapeCertTypes().get().netscapeCertTypes().get().contains( NetscapeCertType.sslClient ) );
        assertEquals( 2, newX509Detail.namesExtensions().get().subjectAlternativeNames().get().alternativeNames().get().size() );
        Iterator<X509GeneralNameValue> sanIterator = newX509Detail.namesExtensions().get().subjectAlternativeNames().get().alternativeNames().get().iterator();
        X509GeneralNameValue rfc822San = sanIterator.next();
        X509GeneralNameValue dnsSan = sanIterator.next();
        assertEquals( X509GeneralName.rfc822Name, rfc822San.nameType().get() );
        assertEquals( "qipki@codeartisans.org", rfc822San.nameValue().get() );
        assertEquals( X509GeneralName.dNSName, dnsSan.nameType().get() );
        assertEquals( "qipki.org", dnsSan.nameValue().get() );


        // Get X509 list
        get = new HttpGet( caApi.x509ListUri().get() );
        addAcceptJsonHeader( get );
        String jsonX509List = httpClient.execute( get, strResponseHandler );
        LOGGER.debug( "X509s List: {}", new JSONObject( jsonX509List ).toString( 2 ) );
        RestListValue x509List = module.newValueFromJSON( RestListValue.class, jsonX509List );
        X509Value firstX509 = ( X509Value ) x509List.items().get().get( 0 );


        // Get first X509
        get = new HttpGet( firstX509.uri().get() );
        addAcceptJsonHeader( get );
        jsonX509 = httpClient.execute( get, strResponseHandler );
        LOGGER.debug( "First X509: {}", new JSONObject( jsonX509 ).toString( 2 ) );
        firstX509 = module.newValueFromJSON( X509Value.class, jsonX509 );


        // Revoke first X509
        X509RevocationParamsValue x509RevocationParams = paramsFactory.createX509RevocationParams( RevocationReason.cessationOfOperation );
        post = new HttpPost( firstX509.revocationUri().get() );
        addAcceptJsonHeader( post );
        post.setEntity( new StringEntity( x509RevocationParams.toString() ) );
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
        post.setEntity( new StringEntity( escrowParams.toString() ) );
        String jsonEscrowed = httpClient.execute( post, strResponseHandler );
        LOGGER.debug( "EscrowedKeyPair : {}", new JSONObject( jsonEscrowed ).toString( 2 ) );
        EscrowedKeyPairValue ekp = module.newValueFromJSON( EscrowedKeyPairValue.class, jsonEscrowed );


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
        post.setEntity( new StringEntity( x509FactoryParams.toString() ) );
        jsonX509 = httpClient.execute( post, strResponseHandler );
        newX509 = module.newValueFromJSON( X509Value.class, jsonX509 );
        LOGGER.debug( "New X509 created using /api/x509/factory and an escrowed keypair after POST/302/REDIRECT: {}", newX509.toString() );


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
