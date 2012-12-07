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
package org.qipki.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class DefaultCryptoContext
    implements CryptoContext
{

    private final String providerName;
    private final SecureRandom random;

    public DefaultCryptoContext()
        throws NoSuchAlgorithmException
    {
        this( BouncyCastleProvider.PROVIDER_NAME, "SHA1PRNG", 128 );
    }

    public DefaultCryptoContext( String randomAlgorithm )
        throws NoSuchAlgorithmException
    {
        this( BouncyCastleProvider.PROVIDER_NAME, randomAlgorithm, 128 );
    }

    public DefaultCryptoContext( String randomAlgorithm, int seedSize )
        throws NoSuchAlgorithmException
    {
        this( BouncyCastleProvider.PROVIDER_NAME, randomAlgorithm, seedSize );
    }

    public DefaultCryptoContext( String providerName, String randomAlgorithm, int seedSize )
        throws NoSuchAlgorithmException
    {
        this.providerName = providerName;
        this.random = SecureRandom.getInstance( "SHA1PRNG" );
        this.random.setSeed( random.generateSeed( 128 ) );
    }

    public DefaultCryptoContext( SecureRandom random )
    {
        this( BouncyCastleProvider.PROVIDER_NAME, random );
    }

    public DefaultCryptoContext( String providerName, SecureRandom random )
    {
        this.providerName = providerName;
        this.random = random;
    }

    @Override
    public String providerName()
    {
        return providerName;
    }

    @Override
    public SecureRandom random()
    {
        return random;
    }

}
