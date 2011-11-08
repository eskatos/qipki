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
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import static org.junit.Assert.*;
import org.junit.Test;

import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.test.AbstractQi4jTest;

import org.qipki.crypto.DefaultCryptoContext;
import static org.qipki.crypto.algorithms.DigestAlgorithm.*;
import org.qipki.crypto.bootstrap.CryptoEngineModuleAssembler;
import org.qipki.crypto.codec.CryptCodexImpl;
import org.qipki.crypto.random.Random;

public class DigesterTest
        extends AbstractQi4jTest
{

    private static final String MESSAGE = "Le nom des fous est écrit partout.\n";
    private static final String EXPECTED_MD5 = "c623709621030a5e84d703b7a160d33a";
    private static final String EXPECTED_SHA_1 = "0fddb1ae5b5d1ab7b8503d1b68ff84c6e507ecdf";
    private static final String EXPECTED_SHA_256 = "2b9b4d8d02e5b9609ac1da0a7de879d0b6f41e845f0ba3f29ac3e15e7ee354ed";
    private static final String EXPECTED_SHA_384 = "20910472dd3a886c563d9637a4e1c94858319f090e9a56f335457d902a3c01d88fc7cc5aad5c99ccb446479c08ae5824";
    private static final String EXPECTED_SHA_512 = "cdbefb2d859f9e984a0032857469c3bba9fe03591772bbf5f1f866a4e8a31f9174e9a30e861d32ba4ff8e7c9f66bcdfed2df36f986e8388ce3cb775808de8bd4";

    // SNIPPET BEGIN crypto.digest.1
    @Override
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        new CryptoEngineModuleAssembler().withWeakRandom().assemble( module );
    }

    @Test
    public void testWithQi4j()
            throws UnsupportedEncodingException
    {
        runTest( serviceLocator.<Digester>findService( Digester.class ).get() );
    }

    @Test
    public void testWithoutQi4j()
            throws UnsupportedEncodingException
    {
        Security.addProvider( new BouncyCastleProvider() );
        runTest( new DigesterImpl( new DefaultCryptoContext(), new CryptCodexImpl() ) );
        Security.removeProvider( BouncyCastleProvider.PROVIDER_NAME );
    }

    private void runTest( Digester digester )
            throws UnsupportedEncodingException
    {
        String hexMd5 = digester.hexDigest( MESSAGE, new DigestParameters( MD5 ) );
        String hexSha1 = digester.hexDigest( MESSAGE, new DigestParameters( SHA_1 ) );
        String hexSha256 = digester.hexDigest( MESSAGE, new DigestParameters( SHA_256 ) );
        String hexSha384 = digester.hexDigest( MESSAGE, new DigestParameters( SHA_384 ) );
        String hexSha512 = digester.hexDigest( MESSAGE, new DigestParameters( SHA_512 ) );

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
        Digester digest = serviceLocator.<Digester>findService( Digester.class ).get();
        Random random = serviceLocator.<Random>findService( Random.class ).get();

        byte[] salt = new byte[ 64 ];
        random.nextBytes( salt );

        String hexSaltedSha256x1024 = digest.hexDigest( MESSAGE, new DigestParameters( SHA_256, salt, 1024 ) );
    }
    // SNIPPET END crypto.digest.2

}
