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
package org.qipki.crypto.symetric;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.security.Security;
import javax.crypto.SecretKey;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.junit.Test;

import org.qipki.crypto.CryptoContext;
import org.qipki.crypto.DefaultCryptoContext;
import org.qipki.crypto.algorithms.BlockCipherModeOfOperation;
import org.qipki.crypto.algorithms.BlockCipherPadding;
import org.qipki.crypto.algorithms.SymetricAlgorithm;
import org.qipki.crypto.cipher.BlockCipher;
import org.qipki.crypto.cipher.CipherFactory;
import org.qipki.crypto.cipher.CipherFactoryImpl;
import org.qipki.crypto.random.Random;
import org.qipki.crypto.random.RandomImpl;

public class SymetricGeneratorTest
{

    @Test
    public void test()
            throws Exception
    {
        Security.addProvider( new BouncyCastleProvider() );
        Random random = new RandomImpl();
        ( ( RandomImpl ) random ).activate();
        CryptoContext cryptoContext = new DefaultCryptoContext();

        SymetricGenerator symGen = new SymetricGeneratorImpl( cryptoContext );
        SecretKey key = symGen.generateSecretKey( new SymetricGeneratorParameters( SymetricAlgorithm.AES, 128 ) );

        CipherFactory cipherFactory = new CipherFactoryImpl( random );
        BlockCipher cipher = cipherFactory.newBlockCipher( SymetricAlgorithm.AES, BlockCipherModeOfOperation.CBC, BlockCipherPadding.PKCS5 );
        byte[] ciphered = cipher.cipher( "CipherMe".getBytes( "UTF-8" ), key );
        byte[] deciphered = cipher.decipher( ciphered, key );
        System.out.println( new String( deciphered, "UTF-8" ) ); // Will output "CipherMe"

        Security.removeProvider( BouncyCastleProvider.PROVIDER_NAME );
    }

}
