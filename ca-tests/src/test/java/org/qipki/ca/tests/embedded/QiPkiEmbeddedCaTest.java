/*
 * Copyright (c) 2011, Paul Merlin. All Rights Reserved.
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
package org.qipki.ca.tests.embedded;

import java.security.KeyPair;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.codeartisans.java.toolbox.Collections;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qi4j.api.query.Query;
import org.qi4j.api.unitofwork.UnitOfWork;
import org.qi4j.api.unitofwork.UnitOfWorkCompletionException;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;
import org.qipki.ca.application.contexts.RootContext;
import org.qipki.ca.application.contexts.ca.CAContext;
import org.qipki.ca.application.contexts.ca.CAListContext;
import org.qipki.ca.application.contexts.cryptostore.CryptoStoreListContext;
import org.qipki.ca.application.contexts.x509.X509ListContext;
import org.qipki.ca.application.contexts.x509profile.X509ProfileListContext;
import org.qipki.ca.domain.ca.CA;
import org.qipki.ca.domain.ca.root.RootCA;
import org.qipki.ca.domain.ca.sub.SubCA;
import org.qipki.ca.domain.cryptostore.CryptoStore;
import org.qipki.ca.domain.revocation.Revocation;
import org.qipki.ca.domain.x509.X509;
import org.qipki.ca.domain.x509profile.X509Profile;
import org.qipki.commons.crypto.states.KeyEscrowPolicy;
import org.qipki.commons.crypto.values.KeyPairSpecValue;
import org.qipki.crypto.algorithms.AsymetricAlgorithm;
import org.qipki.crypto.asymetric.AsymetricGeneratorParameters;
import org.qipki.crypto.storage.KeyStoreType;
import org.qipki.crypto.x509.DistinguishedName;
import org.qipki.crypto.x509.RevocationReason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QiPkiEmbeddedCaTest
        extends AbstractQiPkiCaTest
{

    private static final Logger LOGGER = LoggerFactory.getLogger( QiPkiEmbeddedCaTest.class );
    private static final String CRYPTOSTORE_NAME = "Crypto Store";
    private static final String SERVER_CA_NAME = "Server CA";
    private static final String SERVER_PROFILE_NAME = "Server Profile";
    private static final String CLIENT_CA_NAME = "Client CA";
    private static final char[] PASSWORD = "changeit".toCharArray();
    private static UnitOfWorkFactory uowf;

    @BeforeClass
    public static void startQiPkiApplication()
    {
        qipkiApplication = new QiPkiTestApplicationEmbeddedCa( QiPkiEmbeddedCaTest.class.getSimpleName() );
        qipkiApplication.run();
        uowf = qipkiApplication.unitOfWorkFactory();
    }

    private RootContext rootCtx;
    private KeyPair keyPair;

    @Before
    public void beforeEachTest()
    {
        rootCtx = qipkiApplication.newRootContext();
        keyPair = asymGenerator.generateKeyPair( new AsymetricGeneratorParameters( AsymetricAlgorithm.RSA, 512 ) );
    }

    @Test
    public void testInit()
            throws UnitOfWorkCompletionException
    {
        System.out.println( "##################################################" );
        System.out.println( "                    Init" );
        System.out.println( "##################################################" );

        UnitOfWork uow = uowf.newUnitOfWork();

        CryptoStoreListContext cslCtx = rootCtx.cryptoStoreListContext();
        CAListContext calCtx = rootCtx.caListContext();
        X509ProfileListContext xplCtx = rootCtx.x509ProfileListContext();
        KeyPairSpecValue keyPairSpec = calCtx.createKeyPairSpecValue( AsymetricAlgorithm.RSA, 512 );

        CryptoStore cryptoStore = cslCtx.createCryptoStore( CRYPTOSTORE_NAME, KeyStoreType.PKCS12, PASSWORD );

        LOGGER.info( "CryptoStore created" );

        RootCA rootCa = calCtx.createRootCA( cryptoStore.identity().get(),
                                             "Root CA", 1, buildDN( "RootCa" ),
                                             keyPairSpec, null );
        SubCA serverCa = calCtx.createSubCA( cryptoStore.identity().get(), rootCa.identity().get(),
                                             SERVER_CA_NAME, 1, buildDN( "ServerCa" ),
                                             keyPairSpec, null );
        SubCA clientCa = calCtx.createSubCA( cryptoStore.identity().get(), rootCa.identity().get(),
                                             CLIENT_CA_NAME, 1, buildDN( "ClientCa" ),
                                             keyPairSpec,
                                             null );

        LOGGER.info( "CAs created" );

        X509Profile serverProfile = xplCtx.createX509ProfileForSSLServer( SERVER_PROFILE_NAME, 1, null );
        X509Profile clientProfile = xplCtx.createX509ProfileForSSLClient( "Client Profile", 1, null );

        LOGGER.info( "X509Profiles created" );

        final Map<String, KeyEscrowPolicy> profileAssignments = new HashMap<String, KeyEscrowPolicy>();
        profileAssignments.put( serverProfile.identity().get(), KeyEscrowPolicy.allowed );
        CAContext caCtx = rootCtx.caContext( serverCa.identity().get() );
        caCtx.updateCA( profileAssignments );
        profileAssignments.clear();

        profileAssignments.put( clientProfile.identity().get(), KeyEscrowPolicy.allowed );
        caCtx = rootCtx.caContext( clientCa.identity().get() );
        caCtx.updateCA( profileAssignments );
        profileAssignments.clear();

        LOGGER.info( "X509Profiles assigned" );

        uow.complete();
    }

    @Test
    public void testRenewX509()
            throws UnitOfWorkCompletionException, CertificateEncodingException
    {
        System.out.println( "##################################################" );
        System.out.println( "                   RenewX509" );
        System.out.println( "##################################################" );

        X509Certificate x509Certificate = issueX509();
        PKCS10CertificationRequest pkcs10 = x509Generator.generatePKCS10( new DistinguishedName( buildDN( "RenewX509" ) ), keyPair );

        UnitOfWork uow = uowf.newUnitOfWork();

        X509 x509 = findX509ByHexSha256Hash( rootCtx.x509ListContext(), x509Certificate );
        x509 = rootCtx.x509Context( x509.identity().get() ).renew( pkcs10 );

        x509Certificate = x509.x509Certificate();
        assertNotNull( x509Certificate );

        uow.complete();
    }

    @Test
    public void testRevokeX509()
            throws UnitOfWorkCompletionException, CertificateEncodingException
    {
        System.out.println( "##################################################" );
        System.out.println( "                   RevokeX509" );
        System.out.println( "##################################################" );

        X509Certificate x509Certificate = issueX509();

        UnitOfWork uow = uowf.newUnitOfWork();

        X509 x509 = findX509ByHexSha256Hash( rootCtx.x509ListContext(), x509Certificate );
        Revocation revocation = rootCtx.x509Context( x509.identity().get() ).revoke( RevocationReason.cessationOfOperation );

        assertNotNull( revocation );

        uow.complete();
    }

    private X509Certificate issueX509()
            throws UnitOfWorkCompletionException
    {
        PKCS10CertificationRequest pkcs10 = x509Generator.generatePKCS10( new DistinguishedName( buildDN( "IssueX509" ) ), keyPair );

        UnitOfWork uow = uowf.newUnitOfWork();

        CA ca = findCAByName( rootCtx.caListContext(), SERVER_CA_NAME );
        X509Profile profile = findX509ProfileByName( rootCtx.x509ProfileListContext(), SERVER_PROFILE_NAME );
        X509 x509 = rootCtx.x509ListContext().createX509( ca.identity().get(), profile.identity().get(), pkcs10 );

        X509Certificate x509Certificate = x509.x509Certificate();
        assertNotNull( x509Certificate );

        uow.complete();

        return x509Certificate;
    }

    private String buildDN( String cn )
    {
        return "CN=" + cn + ",O=QiPki,OU=QiPkiCa,OU=UnitTests";
    }

    private CA findCAByName( CAListContext caListContext, String caName )
    {
        Query<CA> findCA = caListContext.findByName( caName, 0 );
        long caCount = findCA.count();
        if ( caCount <= 0 || caCount > 1 ) {
            throw new IllegalStateException( "No or more than one (" + caCount + ") " + caName + " CA found, cannot continue" );
        }
        return Collections.firstElementOrNull( findCA );
    }

    private X509Profile findX509ProfileByName( X509ProfileListContext x509ProfileListContext, String profileName )
    {
        Query<X509Profile> findProfile = x509ProfileListContext.findByName( profileName, 0 );
        long profileCount = findProfile.count();
        if ( profileCount <= 0 || profileCount > 1 ) {
            throw new IllegalStateException( "No or more than one (" + profileCount + ") " + profileName + " X509Profile found, cannot continue" );
        }
        return Collections.firstElementOrNull( findProfile );
    }

    private X509 findX509ByHexSha256Hash( X509ListContext x509ListContext, X509Certificate cert )
            throws CertificateEncodingException
    {
        String hexSha256 = new Sha256Hash( cert.getEncoded() ).toHex();
        X509 x509 = x509ListContext.findByHexSha256( hexSha256 );
        LOGGER.debug( "Tried to find a X509 by its hexSha256 {} and found {}", hexSha256, x509 );
        return x509;
    }

}
