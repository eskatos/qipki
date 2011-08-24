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

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.qi4j.api.unitofwork.UnitOfWork;
import org.qi4j.api.unitofwork.UnitOfWorkCompletionException;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

import org.qipki.ca.application.contexts.RootContext;
import org.qipki.ca.application.contexts.ca.CAContext;
import org.qipki.ca.application.contexts.ca.CAListContext;
import org.qipki.ca.application.contexts.cryptostore.CryptoStoreListContext;
import org.qipki.ca.application.contexts.x509profile.X509ProfileListContext;
import org.qipki.ca.domain.ca.root.RootCA;
import org.qipki.ca.domain.ca.sub.SubCA;
import org.qipki.ca.domain.cryptostore.CryptoStore;
import org.qipki.ca.domain.x509profile.X509Profile;
import org.qipki.commons.crypto.services.X509ExtensionsValueFactory;
import org.qipki.commons.crypto.states.KeyEscrowPolicy;
import org.qipki.commons.crypto.values.KeyPairSpecValue;
import org.qipki.crypto.algorithms.AsymetricAlgorithm;
import org.qipki.crypto.storage.KeyStoreType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QiPkiEmbeddedCaTest
        extends AbstractQiPkiCaTest
{

    private static final Logger LOGGER = LoggerFactory.getLogger( QiPkiEmbeddedCaTest.class );
    private static final String CRYPTOSTORE_NAME = "Crypto Store";
    private static final String SERVER_CA_NAME = "Server CA";
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

    @Before
    public void beforeEachTest()
    {
        rootCtx = qipkiApplication.newRootContext();
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
        X509ExtensionsValueFactory x509ExtFactory = xplCtx.x509ExtensionsValueFactory();
        KeyPairSpecValue keyPairSpec = calCtx.createKeyPairSpecValue( AsymetricAlgorithm.RSA, 512 );

        CryptoStore cryptoStore = cslCtx.createCryptoStore( CRYPTOSTORE_NAME, KeyStoreType.PKCS12, PASSWORD );

        LOGGER.info( "CryptoStore created" );

        RootCA rootCa = calCtx.createRootCA( cryptoStore.identity().get(), "Root CA", 1,
                                             "CN=RootCa,O=QiPki,OU=QiPkiCa,OU=UnitTests", keyPairSpec );
        SubCA serverCa = calCtx.createSubCA( cryptoStore.identity().get(), SERVER_CA_NAME, 1,
                                             "CN=ServerCa,O=QiPki,OU=QiPkiCa,OU=UnitTests", keyPairSpec,
                                             rootCa.identity().get() );
        SubCA clientCa = calCtx.createSubCA( cryptoStore.identity().get(), CLIENT_CA_NAME, 1,
                                             "CN=ClientCa,O=QiPki,OU=QiPkiCa,OU=UnitTests", keyPairSpec,
                                             rootCa.identity().get() );

        LOGGER.info( "CAs created" );

        X509Profile serverProfile = xplCtx.createX509ProfileForSSLServer( "Server Profile", 1, null );
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
    public void testCreateBaseCertificates()
    {
        System.out.println( "##################################################" );
        System.out.println( "             CreateBaseCertificates" );
        System.out.println( "##################################################" );
    }

    @Test
    public void testRenewX509()
    {
        System.out.println( "##################################################" );
        System.out.println( "                     RenewX509" );
        System.out.println( "##################################################" );
    }

    @Test
    public void testRevokeX509()
    {
        System.out.println( "##################################################" );
        System.out.println( "                     RevokeX509" );
        System.out.println( "##################################################" );
    }

    @Test
    public void testIssueEscrowedCertifiedKeyPair()
    {
        System.out.println( "##################################################" );
        System.out.println( "          IssueEscrowedCertifiedKeyPair" );
        System.out.println( "##################################################" );
    }

    @Test
    public void testRecoverEscrowedCertifiedKeyPair()
    {
        System.out.println( "##################################################" );
        System.out.println( "         RecoverEscrowedCertifiedKeyPair" );
        System.out.println( "##################################################" );
    }

    @Test
    public void testRenewEscrowedCertifiedKeyPair()
    {
        System.out.println( "##################################################" );
        System.out.println( "         RenewEscrowedCertifiedKeyPair" );
        System.out.println( "##################################################" );
    }

    @Test
    public void testBatchEnroll()
    {
        System.out.println( "##################################################" );
        System.out.println( "                 BatchEnroll" );
        System.out.println( "##################################################" );
    }

}
