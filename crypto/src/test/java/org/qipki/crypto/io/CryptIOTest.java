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
package org.qipki.crypto.io;

import java.io.IOException;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;

import org.qipki.crypto.algorithms.AsymetricAlgorithm;
import org.qipki.crypto.asymetric.AsymetricGenerator;
import org.qipki.crypto.asymetric.AsymetricGeneratorImpl;
import org.qipki.crypto.asymetric.AsymetricGeneratorParameters;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.qipki.crypto.CryptoContext;

public class CryptIOTest
{

    @BeforeClass
    public static void beforeClass()
    {
        Security.addProvider( new BouncyCastleProvider() );
    }

    @Test
    public void testKeyPairIO()
            throws IOException
    {
        CryptoContext cryptoContext = new CryptoContext()
        {

            @Override
            public String providerName()
            {
                return BouncyCastleProvider.PROVIDER_NAME;
            }

        };

        AsymetricGenerator asymGen = new AsymetricGeneratorImpl( cryptoContext );
        CryptIO cryptIO = new CryptIOImpl( cryptoContext );

        KeyPair keypair = asymGen.generateKeyPair( new AsymetricGeneratorParameters( AsymetricAlgorithm.RSA, 512 ) );

        String keypairPEM = cryptIO.asPEM( keypair ).toString();
        System.out.println( keypairPEM );
        keypair = cryptIO.readKeyPairPEM( new StringReader( keypairPEM ) );

        String pubkeyPEM = cryptIO.asPEM( keypair.getPublic() ).toString();
        System.out.println( pubkeyPEM );

        PublicKey pubKey = ( PublicKey ) new PEMReader( new StringReader( pubkeyPEM ) ).readObject();

        Assert.assertTrue( pubKey.equals( keypair.getPublic() ) );

    }

}
