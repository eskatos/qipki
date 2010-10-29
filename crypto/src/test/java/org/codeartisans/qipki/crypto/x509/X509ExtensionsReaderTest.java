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
package org.codeartisans.qipki.crypto.x509;

import java.io.InputStreamReader;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Set;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.codeartisans.java.toolbox.CollectionUtils;
import org.codeartisans.qipki.crypto.codec.CryptCodex;
import org.codeartisans.qipki.crypto.codec.CryptCodexImpl;
import org.codeartisans.qipki.crypto.io.CryptIO;
import org.codeartisans.qipki.crypto.io.CryptIOImpl;
import static org.codeartisans.qipki.crypto.x509.X509TestConstants.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class X509ExtensionsReaderTest
{

    private static final Logger LOGGER = LoggerFactory.getLogger( X509ExtensionsReaderTest.class );
    private X509Certificate baltimoreCyberTrustCodeSigningRoot;
    private X509Certificate chamberOfCommerceRoot;

    @BeforeClass
    public static void beforeClass()
    {
        Security.addProvider( new BouncyCastleProvider() );
    }

    private X509ExtensionsReaderImpl x509ExtReader;

    @Before
    public void before()
    {
        CryptCodex cryptCodex = new CryptCodexImpl();
        CryptIO cryptIO = new CryptIOImpl();
        x509ExtReader = new X509ExtensionsReaderImpl( cryptCodex );
        baltimoreCyberTrustCodeSigningRoot = cryptIO.readX509PEM( new InputStreamReader( X509ExtensionsReaderTest.class.getResourceAsStream( RSRC_NAME_BALTIMORE_CRYBERTRUST_CODESIGNING_ROOT_PEM ) ) );
        chamberOfCommerceRoot = cryptIO.readX509PEM( new InputStreamReader( X509ExtensionsReaderTest.class.getResourceAsStream( RSRC_NAME_CHAMBER_OF_COMMERCE_ROOT_PEM ) ) );
    }

    @Test
    public void testExtendedKeyUsage()
    {
        Set<ExtendedKeyUsage> extendedKeyUsages = x509ExtReader.getExtendedKeyUsages( baltimoreCyberTrustCodeSigningRoot );
        LOGGER.info( "Extended Key Usages: {}", extendedKeyUsages );
        assertEquals( 1, extendedKeyUsages.size() );
        assertEquals( ExtendedKeyUsage.codeSigning, CollectionUtils.firstElementOrNull( extendedKeyUsages ) );
    }

    @Test
    public void testNetscapeCertType()
    {
        Set<NetscapeCertType> netscapeCertTypes = x509ExtReader.getNetscapeCertTypes( chamberOfCommerceRoot );
        LOGGER.info( "Netscape Cert Types: {}", netscapeCertTypes );
        assertEquals( 3, netscapeCertTypes.size() );
        assertTrue( netscapeCertTypes.contains( NetscapeCertType.sslCA ) );
        assertTrue( netscapeCertTypes.contains( NetscapeCertType.smimeCA ) );
        assertTrue( netscapeCertTypes.contains( NetscapeCertType.objectSigningCA ) );
    }

}
