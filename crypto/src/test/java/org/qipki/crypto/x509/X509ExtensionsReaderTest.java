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
package org.qipki.crypto.x509;

import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.EnumSet;
import java.util.Set;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.codeartisans.java.toolbox.CollectionUtils;
import org.qipki.crypto.codec.CryptCodex;
import org.qipki.crypto.codec.CryptCodexImpl;
import org.qipki.crypto.io.CryptIO;
import org.qipki.crypto.io.CryptIOImpl;
import static org.qipki.crypto.x509.X509TestConstants.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class X509ExtensionsReaderTest
{

    private static final Logger LOGGER = LoggerFactory.getLogger( X509ExtensionsReaderTest.class );

    @BeforeClass
    public static void beforeClass()
    {
        Security.addProvider( new BouncyCastleProvider() );
    }

    private CryptCodexImpl cryptCodex;
    private CryptIOImpl cryptIO;
    private X509Certificate baltimoreCyberTrustCodeSigningRoot;
    private X509Certificate chamberOfCommerceRoot;
    private X509ExtensionsReaderImpl x509ExtReader;

    @Before
    public void before()
    {
        cryptCodex = new CryptCodexImpl();
        cryptIO = new CryptIOImpl();
        x509ExtReader = new X509ExtensionsReaderImpl( cryptCodex );
        baltimoreCyberTrustCodeSigningRoot = cryptIO.readX509PEM( new InputStreamReader( X509ExtensionsReaderTest.class.getResourceAsStream( RSRC_NAME_BALTIMORE_CRYBERTRUST_CODESIGNING_ROOT_PEM ) ) );
        chamberOfCommerceRoot = cryptIO.readX509PEM( new InputStreamReader( X509ExtensionsReaderTest.class.getResourceAsStream( RSRC_NAME_CHAMBER_OF_COMMERCE_ROOT_PEM ) ) );
    }

    @Test
    public void testSubjectKeyIdentifier()
    {
        byte[] ski = x509ExtReader.getSubjectKeyIdentifier( baltimoreCyberTrustCodeSigningRoot );
        String hexSki = cryptCodex.toHexString( ski );
        LOGGER.info( "SubjectKeyIdentifier: {}", hexSki );
        assertTrue( hexSki.startsWith( "c841" ) );
        assertTrue( hexSki.endsWith( "425a" ) );

        ski = x509ExtReader.getSubjectKeyIdentifier( chamberOfCommerceRoot );
        hexSki = cryptCodex.toHexString( ski );
        LOGGER.info( "SubjectKeyIdentifier: {}", hexSki );
        assertTrue( hexSki.startsWith( "e394" ) );
        assertTrue( hexSki.endsWith( "a28a" ) );
    }

    @Test
    public void testKeyUsage()
    {
        Set<KeyUsage> baltimoreKeyUsages = x509ExtReader.getKeyUsages( baltimoreCyberTrustCodeSigningRoot );
        LOGGER.info( "Key Usages: {}", baltimoreKeyUsages );
        assertEquals( 2, baltimoreKeyUsages.size() );
        assertTrue( baltimoreKeyUsages.containsAll( EnumSet.of( KeyUsage.keyCertSign, KeyUsage.cRLSign ) ) );

        Set<KeyUsage> cocKeyUsages = x509ExtReader.getKeyUsages( chamberOfCommerceRoot );
        LOGGER.info( "Key Usages: {}", cocKeyUsages );
        assertEquals( 2, cocKeyUsages.size() );
        assertTrue( cocKeyUsages.containsAll( EnumSet.of( KeyUsage.keyCertSign, KeyUsage.cRLSign ) ) );
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
