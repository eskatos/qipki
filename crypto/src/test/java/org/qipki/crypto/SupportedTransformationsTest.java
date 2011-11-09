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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;

import org.codeartisans.java.toolbox.Strings;
import static org.junit.Assert.*;
import org.junit.Test;

import org.qipki.crypto.algorithms.BlockCipherModeOfOperation;
import org.qipki.crypto.algorithms.BlockCipherPadding;
import org.qipki.crypto.algorithms.IllegalAlgorithmException;
import org.qipki.crypto.algorithms.SymetricAlgorithm;
import org.qipki.crypto.cipher.CipherTest;
import org.qipki.crypto.cipher.SymetricCipher;
import org.qipki.crypto.cipher.SymetricCipherFactoryParameters;
import org.qipki.crypto.constants.IOConstants;
import org.qipki.crypto.jca.Transformation;
import org.qipki.crypto.symetric.SymetricCipheringGeneratorParameters;

public class SupportedTransformationsTest
        extends AbstractQiPkiCryptoTest
{

    @Test
    public void testAllSymetricCipherAlgorithms()
            throws UnsupportedEncodingException
    {
        List<String> notSupported = new ArrayList<String>();
        for ( SymetricAlgorithm eachAlgo : SymetricAlgorithm.values() ) {
            for ( BlockCipherModeOfOperation eachMode : BlockCipherModeOfOperation.values() ) {
                for ( BlockCipherPadding eachPadding : BlockCipherPadding.values() ) {
                    String transformation = new Transformation( eachAlgo, eachMode, eachPadding ).jcaTransformation();
                    System.out.println( ">> " + transformation );
                    int keySize = 128;
                    switch ( eachAlgo ) {
                        case DES:
                            keySize = 64;
                            break;
                        case TripleDES:
                            keySize = 192;
                            break;
                    }
                    try {
                        SecretKey key = symGenerator.generateCipheringKey( new SymetricCipheringGeneratorParameters( eachAlgo, keySize ) );
                        SymetricCipher cipher = cipherFactory.newSymetricCipher( new SymetricCipherFactoryParameters( eachAlgo, eachMode, eachPadding ) );

                        for ( String eachSample : CipherTest.SAMPLES ) {

                            byte[] ciphered = cipher.cipher( eachSample, key );
                            byte[] deciphered = cipher.decipher( ciphered, key );
                            assertEquals( eachSample, new String( deciphered, IOConstants.UTF_8 ) );

                        }
                        System.out.println( ">> " + transformation + " is supported with " + keySize + "bits keys" );
                    } catch ( IllegalAlgorithmException ex ) {
                        System.out.println( ">> " + transformation + " is NOT supported with " + keySize + "bits keys => " + ex.getMessage() );
                        notSupported.add( transformation );
                    }
                }
            }
        }
        System.out.println();
        System.out.println( ">> Symetric Cipher Algorithms NOT Supported" );
        System.out.println();
        for ( String eachNotSupported : notSupported ) {
            System.out.println( " - " + eachNotSupported );
        }
        System.out.println();
    }

    @Test
    public void testAllSecretKeyAlgorithms()
    {
    }

}
