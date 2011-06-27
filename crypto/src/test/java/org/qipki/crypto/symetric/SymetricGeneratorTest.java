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

import java.security.Security;
import javax.crypto.SecretKey;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.junit.Test;

import org.qipki.crypto.CryptoContext;
import org.qipki.crypto.algorithms.SymetricAlgorithm;

public class SymetricGeneratorTest
{

    @Test
    public void test()
    {
        Security.addProvider( new BouncyCastleProvider() );
        CryptoContext cryptoContext = new CryptoContext()
        {

            @Override
            public String providerName()
            {
                return BouncyCastleProvider.PROVIDER_NAME;
            }

        };
        SymetricGenerator symGen = new SymetricGeneratorImpl( cryptoContext );
        SecretKey key = symGen.generateSecretKey( new SymetricGeneratorParameters( SymetricAlgorithm.AES, 128 ) );
        System.out.println( key.toString() );
    }

}
