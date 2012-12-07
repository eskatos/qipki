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
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.SecretKey;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.macs.CBCBlockCipherMac;
import org.junit.Ignore;
import org.junit.Test;
import org.qipki.crypto.algorithms.BCMACAlgorithm;
import org.qipki.crypto.algorithms.BlockCipherAlgorithm;
import org.qipki.crypto.algorithms.BlockCipherModeOfOperation;
import org.qipki.crypto.algorithms.BlockCipherPadding;
import org.qipki.crypto.algorithms.HMACAlgorithm;
import org.qipki.crypto.algorithms.IllegalAlgorithmException;
import org.qipki.crypto.cipher.CipherTest;
import org.qipki.crypto.cipher.SymetricCipher;
import org.qipki.crypto.cipher.SymetricCipherFactoryParameters;
import org.qipki.crypto.constants.IOConstants;
import org.qipki.crypto.jca.Transformation;
import org.qipki.crypto.mac.BCMACParameters;
import org.qipki.crypto.mac.HMACParameters;
import org.qipki.crypto.mac.MacTest;
import org.qipki.crypto.symetric.SymetricCipheringGeneratorParameters;
import org.qipki.crypto.symetric.SymetricSigningGeneratorParameters;

import static org.junit.Assert.assertEquals;

public class SupportedTransformationsTest
    extends AbstractQiPkiCryptoTest
{

    private static final boolean DEBUG = false;

    @Test
    @Ignore( "CBC-MAC and CMAC support is a work in progress" )
    public void test()
        throws NoSuchAlgorithmException, NoSuchProviderException
    {
        BlockCipher cipher = new AESEngine();
        CBCBlockCipherMac plop = new CBCBlockCipherMac( cipher );
        System.out.println( plop.getAlgorithmName() );

        int keySize = 128;
        SecretKey signingKey = symGenerator.generateSigningKey( new SymetricSigningGeneratorParameters( BCMACAlgorithm.CBC_MAC, keySize ) );
        String hexMac = mac.hexMac( MacTest.SAMPLES[0], new BCMACParameters( BCMACAlgorithm.CBC_MAC, BlockCipherAlgorithm.AES, signingKey ) );
        System.out.println( hexMac );
    }

    @Test
    public void testAllHMACAlgorithms()
    {
        List<String> notSupported = new ArrayList<String>();
        for( HMACAlgorithm eachAlgo : HMACAlgorithm.values() )
        {
            String transformation = new Transformation( eachAlgo ).jcaTransformation();
            if( DEBUG )
            {
                System.out.println( ">> " + transformation );
            }
            int keySize = 128;
            try
            {
                SecretKey key = symGenerator.generateSigningKey( new SymetricSigningGeneratorParameters( eachAlgo, keySize ) );

                for( String eachSample : MacTest.SAMPLES )
                {
                    String hexMac = mac.hexMac( eachSample, new HMACParameters( eachAlgo, key ) );
                }
            }
            catch( UnsupportedOperationException ex )
            {
                if( DEBUG )
                {
                    System.out.println( ">> " + transformation + " is NOT supported with " + keySize + "bits keys => " + ex.getMessage() );
                }
                notSupported.add( transformation );
            }
            catch( CryptoFailure ex )
            {
                if( DEBUG )
                {
                    System.out.println( ">> " + transformation + " is NOT supported with " + keySize + "bits keys => " + ex.getMessage() );
                }
                notSupported.add( transformation );
            }
        }
        System.out.println();
        System.out.println( ">> MAC Algorithms NOT Supported" );
        for( String eachNotSupported : notSupported )
        {
            System.out.println( " - " + eachNotSupported );
        }
        System.out.println();
    }

    @Test
    public void testAllBCMACAlgorithms()
    {
        List<String> notSupported = new ArrayList<String>();
        for( BCMACAlgorithm bcMacAlgo : BCMACAlgorithm.values() )
        {
            for( BlockCipherAlgorithm bcAlgo : BlockCipherAlgorithm.values() )
            {
                String transformation = new Transformation( bcMacAlgo, bcAlgo ).jcaTransformation();
                if( DEBUG )
                {
                    System.out.println( ">> " + transformation );
                }
                int keySize = 128;
                try
                {
                    SecretKey key = symGenerator.generateSigningKey( new SymetricSigningGeneratorParameters( bcMacAlgo, keySize ) );

                    for( String eachSample : MacTest.SAMPLES )
                    {
                        String hexMac = mac.hexMac( eachSample, new BCMACParameters( bcMacAlgo, bcAlgo, key ) );
                    }
                }
                catch( UnsupportedOperationException ex )
                {
                    if( DEBUG )
                    {
                        System.out.println( ">> " + transformation + " is NOT supported with " + keySize + "bits keys => " + ex.getMessage() );
                    }
                    notSupported.add( transformation );
                }
                catch( CryptoFailure ex )
                {
                    if( DEBUG )
                    {
                        System.out.println( ">> " + transformation + " is NOT supported with " + keySize + "bits keys => " + ex.getMessage() );
                    }
                    notSupported.add( transformation );
                }
            }
        }
        System.out.println();
        System.out.println( ">> MAC Algorithms NOT Supported" );
        for( String eachNotSupported : notSupported )
        {
            System.out.println( " - " + eachNotSupported );
        }
        System.out.println();
    }

    @Test
    public void testAllSymetricCipherAlgorithms()
        throws UnsupportedEncodingException
    {
        List<String> notSupported = new ArrayList<String>();
        for( BlockCipherAlgorithm eachAlgo : BlockCipherAlgorithm.values() )
        {
            for( BlockCipherModeOfOperation eachMode : BlockCipherModeOfOperation.values() )
            {
                for( BlockCipherPadding eachPadding : BlockCipherPadding.values() )
                {
                    String transformation = new Transformation( eachAlgo, eachMode, eachPadding ).jcaTransformation();
                    if( DEBUG )
                    {
                        System.out.println( ">> " + transformation );
                    }
                    int keySize = 128;
                    switch( eachAlgo )
                    {
                        case DES:
                            keySize = 64;
                            break;
                        case TripleDES:
                            keySize = 192;
                            break;
                    }
                    try
                    {
                        SecretKey key = symGenerator.generateCipheringKey( new SymetricCipheringGeneratorParameters( eachAlgo, keySize ) );
                        SymetricCipher cipher = cipherFactory.newSymetricCipher( new SymetricCipherFactoryParameters( eachAlgo, eachMode, eachPadding ) );

                        for( String eachSample : CipherTest.SAMPLES )
                        {

                            byte[] ciphered = cipher.cipher( eachSample, key );
                            byte[] deciphered = cipher.decipher( ciphered, key );
                            assertEquals( eachSample, new String( deciphered, IOConstants.UTF_8 ) );

                        }
                        if( DEBUG )
                        {
                            System.out.println( ">> " + transformation + " is supported with " + keySize + "bits keys" );
                        }
                    }
                    catch( IllegalAlgorithmException ex )
                    {
                        if( DEBUG )
                        {
                            System.out.println( ">> " + transformation + " is NOT supported with " + keySize + "bits keys => " + ex.getMessage() );
                        }
                        notSupported.add( transformation );
                    }
                }
            }
        }
        System.out.println();
        System.out.println( ">> Symetric Cipher Algorithms NOT Supported" );
        for( String eachNotSupported : notSupported )
        {
            System.out.println( " - " + eachNotSupported );
        }
        System.out.println();
    }

}
