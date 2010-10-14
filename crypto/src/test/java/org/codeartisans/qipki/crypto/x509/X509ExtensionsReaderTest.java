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
