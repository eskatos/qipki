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
package org.codeartisans.qipki.crypto.cipher;

import java.io.UnsupportedEncodingException;
import javax.crypto.SecretKey;

import org.codeartisans.qipki.crypto.QiCryptoEngine;
import org.codeartisans.qipki.crypto.algorithms.BlockCipherModeOfOperation;
import org.codeartisans.qipki.crypto.algorithms.BlockCipherPadding;
import org.codeartisans.qipki.crypto.algorithms.SymetricAlgorithm;
import org.codeartisans.qipki.crypto.codec.CryptCodexService;
import org.codeartisans.qipki.crypto.digest.DigestService;
import org.codeartisans.qipki.crypto.jca.Transformation;
import org.codeartisans.qipki.crypto.random.RandomService;
import org.codeartisans.qipki.crypto.symetric.SymetricGenerator;
import org.codeartisans.qipki.crypto.symetric.SymetricGeneratorParameters;
import org.codeartisans.qipki.crypto.symetric.SymetricGeneratorService;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.test.AbstractQi4jTest;

public class CipherTest
        extends AbstractQi4jTest
{

    @Override
    @SuppressWarnings( "unchecked" )
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        module.addServices( QiCryptoEngine.class ).instantiateOnStartup();
        module.addServices( RandomService.class,
                            SymetricGeneratorService.class,
                            CipherFactoryService.class,
                            DigestService.class,
                            CryptCodexService.class );
    }

    @Test
    @Ignore
    public void testAesCBCZeroPadding()
            throws UnsupportedEncodingException
    {
        SymetricGenerator symGenerator = serviceLocator.<SymetricGenerator>findService( SymetricGenerator.class ).get();
        CipherFactory cipherFactory = serviceLocator.<CipherFactory>findService( CipherFactory.class ).get();

        BlockCipher cipher = cipherFactory.newBlockCipher( SymetricAlgorithm.AES, BlockCipherModeOfOperation.CBC, BlockCipherPadding.ZERO_BYTE );
        SecretKey key = symGenerator.generateSecretKey( new SymetricGeneratorParameters( SymetricAlgorithm.AES, 128 ) );

        String original = "Le nom des fous est écrit partout.";
        byte[] ciphered = cipher.cipher( original.getBytes( "UTF-8" ), key );
        byte[] deciphered = cipher.decipher( ciphered, key );
        System.out.println( new String( deciphered, "UTF-8" ) );
    }

    @Test
    // @Ignore
    public void testAlgorithms()
    {
        SymetricGenerator symGenerator = serviceLocator.<SymetricGenerator>findService( SymetricGenerator.class ).get();
        CipherFactory cipherFactory = serviceLocator.<CipherFactory>findService( CipherFactory.class ).get();

        for ( SymetricAlgorithm eachAlgo : SymetricAlgorithm.values() ) {
            for ( BlockCipherModeOfOperation eachMode : BlockCipherModeOfOperation.values() ) {
                for ( BlockCipherPadding eachPadding : BlockCipherPadding.values() ) {
                    System.out.println( "> Testing " + new Transformation( eachAlgo, eachMode, eachPadding ).jcaTransformation() );
                    int keySize = 128;
                    switch ( eachAlgo ) {
                        case DES:
                            keySize = 64;
                            break;
                        case TripleDES:
                            keySize = 192;
                            break;
                    }
                    boolean supported = true;
                    switch ( eachMode ) {
                        case SIC:
                            switch ( eachAlgo ) {
                                case Blowfish:
                                case TripleDES:
                                case CAST_128:
                                case XTEA:
                                case TEA:
                                case DES:
                                    // Warning: SIC-Mode can become a twotime-pad if the blocksize of the cipher is too small. Use a cipher with a block size of at least 128 bits (e.g. AES)
                                    supported = false;
                            }
                    }

                    if ( supported ) {
                        System.out.println( ">> Generating Key" );
                        BlockCipher cipher = cipherFactory.newBlockCipher( eachAlgo, eachMode, eachPadding );
                        SecretKey key = symGenerator.generateSecretKey( new SymetricGeneratorParameters( eachAlgo, keySize ) );

                        byte[] data = new byte[]{};
                        System.out.println( ">> Ciphering" );
                        byte[] ciphered = cipher.cipher( data, key );
                        System.out.println( ">> Unciphering" );
                        byte[] deciphered = cipher.decipher( ciphered, key );
                        System.out.println( ">> Comparing" );
                        Assert.assertArrayEquals( data, deciphered );
                    } else {
                        System.out.println( ">> UNSUPPORTED" );
                    }
                }
            }
        }
    }

}
