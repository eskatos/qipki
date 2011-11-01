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
package org.qipki.crypto.cipher;

import java.io.UnsupportedEncodingException;
import javax.crypto.SecretKey;

import org.qipki.crypto.algorithms.BlockCipherModeOfOperation;
import org.qipki.crypto.algorithms.BlockCipherPadding;
import org.qipki.crypto.algorithms.IllegalAlgorithmException;
import org.qipki.crypto.algorithms.SymetricAlgorithm;
import org.qipki.crypto.bootstrap.CryptoEngineModuleAssembler;
import org.qipki.crypto.jca.Transformation;
import org.qipki.crypto.symetric.SymetricGenerator;
import org.qipki.crypto.symetric.SymetricGeneratorParameters;

import static org.junit.Assert.*;
import org.junit.Test;

import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.test.AbstractQi4jTest;
import org.qipki.crypto.constants.IOConstants;

public class CipherTest
        extends AbstractQi4jTest
{

    private static final String[] SAMPLES = new String[]{
        "Hello World",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla tristique dui vel leo porta commodo. Nam neque mauris, semper in rhoncus eget, fringilla in tellus. Nunc consequat felis eget turpis lacinia non mattis nunc mollis. Fusce nec quam mi. Fusce viverra, magna eu convallis aliquet, enim justo imperdiet eros, at ullamcorper eros orci in lorem. Fusce volutpat massa a turpis facilisis porta consequat lacus commodo. Pellentesque vulputate fermentum velit. Integer elementum ornare tortor quis consectetur. Cras vel orci sed nisl sollicitudin fringilla ac et libero. Nulla a eros est, nec volutpat mi. Curabitur vehicula mollis vulputate. Donec ligula erat, facilisis ut semper ac, lacinia vitae purus. Vivamus pharetra mauris eget tellus elementum elementum. Ut et justo purus, vitae elementum magna. Phasellus tortor orci, feugiat id venenatis sit amet, tempor id nisl. Donec venenatis enim vitae diam pulvinar lobortis."
    };

    @Override
    @SuppressWarnings( "unchecked" )
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        new CryptoEngineModuleAssembler().withWeakRandom().assemble( module );
    }

    @Test
    public void testAllBlockCipherAlgorithms()
            throws UnsupportedEncodingException
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
                    try {
                        System.out.println( ">> Generating Key" );
                        BlockCipher cipher = cipherFactory.newBlockCipher( eachAlgo, eachMode, eachPadding );
                        SecretKey key = symGenerator.generateSecretKey( new SymetricGeneratorParameters( eachAlgo, keySize ) );

                        for ( String eachSample : SAMPLES ) {

                            byte[] data = eachSample.getBytes( IOConstants.UTF_8 );
                            System.out.println( ">> Ciphering: '" + eachSample + "'" );
                            byte[] ciphered = cipher.cipher( data, key );
                            System.out.println( ">> Unciphering" );
                            byte[] deciphered = cipher.decipher( ciphered, key );
                            System.out.println( ">> Comparing" );
                            assertArrayEquals( data, deciphered );

                        }
                    } catch ( IllegalAlgorithmException ex ) {
                        System.out.println( ">> UNSUPPORTED " + ex.getMessage() );
                    }

                }
            }
        }
    }

}
