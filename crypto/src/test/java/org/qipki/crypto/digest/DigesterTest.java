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
package org.qipki.crypto.digest;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Security;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.qipki.crypto.AbstractQiPkiCryptoTest;
import org.qipki.crypto.DefaultCryptoContext;
import static org.qipki.crypto.algorithms.DigestAlgorithm.SHA_256;
import org.qipki.crypto.codec.CryptCodexImpl;

public class DigesterTest
        extends AbstractQiPkiCryptoTest
{

    private static final String MESSAGE = "Le nom des fous est écrit partout.\n";

    private static final String EXPECTED_MD5 = "c623709621030a5e84d703b7a160d33a";

    private static final String EXPECTED_SHA_1 = "0fddb1ae5b5d1ab7b8503d1b68ff84c6e507ecdf";

    private static final String EXPECTED_SHA_256 = "2b9b4d8d02e5b9609ac1da0a7de879d0b6f41e845f0ba3f29ac3e15e7ee354ed";

    private static final String EXPECTED_SHA_384 = "20910472dd3a886c563d9637a4e1c94858319f090e9a56f335457d902a3c01d88fc7cc5aad5c99ccb446479c08ae5824";

    private static final String EXPECTED_SHA_512 = "cdbefb2d859f9e984a0032857469c3bba9fe03591772bbf5f1f866a4e8a31f9174e9a30e861d32ba4ff8e7c9f66bcdfed2df36f986e8388ce3cb775808de8bd4";

    @Test
    public void testWithQi4j()
            throws UnsupportedEncodingException
    {
        testDigester( digester );
    }

    @Test
    public void testWithoutQi4j()
            throws UnsupportedEncodingException, GeneralSecurityException
    {
        Security.addProvider( new BouncyCastleProvider() );
        testDigester( new DigesterImpl( new DefaultCryptoContext(), new CryptCodexImpl() ) );
        Security.removeProvider( BouncyCastleProvider.PROVIDER_NAME );
    }

    // SNIPPET BEGIN crypto.digest.1
    private void testDigester( Digester digester )
            throws UnsupportedEncodingException
    {
        // Digest messages
        String hexMd5 = digester.hexDigest( MESSAGE, DigestParameters.MD5 );
        String hexSha1 = digester.hexDigest( MESSAGE, DigestParameters.SHA_1 );
        String hexSha256 = digester.hexDigest( MESSAGE, DigestParameters.SHA_256 );
        String hexSha384 = digester.hexDigest( MESSAGE, DigestParameters.SHA_384 );
        String hexSha512 = digester.hexDigest( MESSAGE, DigestParameters.SHA_512 );

        // Test
        assertEquals( EXPECTED_MD5, hexMd5 );
        assertEquals( EXPECTED_SHA_1, hexSha1 );
        assertEquals( EXPECTED_SHA_256, hexSha256 );
        assertEquals( EXPECTED_SHA_384, hexSha384 );
        assertEquals( EXPECTED_SHA_512, hexSha512 );
    }
    // SNIPPET END crypto.digest.1

    @Test
    // SNIPPET BEGIN crypto.digest.2
    public void saltedHashExample()
    {
        // A Base64 encoded SHA-256 digest using a 64bit random salt and 1024 iterations
        String base64 = digester.base64Digest( MESSAGE, new DigestParameters( SHA_256, digester.generateSalt( 64 ), 1024 ) );

        // A Hex encoded SHA-256 digest using a 64bit random salt and 1024 iterations
        String hex = digester.hexDigest( MESSAGE, digester.newParamsBuilder().using( SHA_256 ).salted( 64 ).iterations( 1024 ).build() );
    }
    // SNIPPET END crypto.digest.2

}
