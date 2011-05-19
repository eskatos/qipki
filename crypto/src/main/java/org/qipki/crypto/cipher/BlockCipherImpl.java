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

import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.EnumSet;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.qipki.crypto.QiCryptoFailure;
import org.qipki.crypto.algorithms.BlockCipherModeOfOperation;
import org.qipki.crypto.algorithms.BlockCipherPadding;
import org.qipki.crypto.algorithms.IllegalAlgorithmException;
import org.qipki.crypto.algorithms.SymetricAlgorithm;
import org.qipki.crypto.jca.Transformation;
import org.qipki.crypto.random.Random;

public class BlockCipherImpl
        implements BlockCipher
{

    private static final EnumSet<SymetricAlgorithm> NO_SIC_CIPHER_ALGS = EnumSet.of( SymetricAlgorithm.Blowfish,
                                                                                     SymetricAlgorithm.TripleDES,
                                                                                     SymetricAlgorithm.CAST_128,
                                                                                     SymetricAlgorithm.XTEA,
                                                                                     SymetricAlgorithm.TEA,
                                                                                     SymetricAlgorithm.DES );
    private final Random random;
    private final SymetricAlgorithm algo;
    private final BlockCipherModeOfOperation mode;
    private final BlockCipherPadding padding;

    /* package */ BlockCipherImpl( Random random, SymetricAlgorithm algo, BlockCipherModeOfOperation mode, BlockCipherPadding padding )
    {
        if ( mode == BlockCipherModeOfOperation.SIC && NO_SIC_CIPHER_ALGS.contains( algo ) ) {
            throw new IllegalAlgorithmException( "SIC-Mode cannot be used with " + algo.name() + " because it can become a twotime-pad if the blocksize of the cipher is too small." );
        }
        this.random = random;
        this.algo = algo;
        this.mode = mode;
        this.padding = padding;
    }

    @Override
    public byte[] cipher( byte[] data, Key key )
    {
        return cipher( data, key.getEncoded() );
    }

    @Override
    public byte[] decipher( byte[] ciphered, Key key )
    {
        return decipher( ciphered, key.getEncoded() );
    }

    @Override
    public byte[] cipher( byte[] data, byte[] key )
    {
        try {
            if ( useIV() ) {

                byte[] iv = generateIV();
                Cipher cipher = buildCipher( Cipher.ENCRYPT_MODE, key, iv );

                byte[] encrypted = cipher.doFinal( data );

                byte[] output = new byte[ iv.length + encrypted.length ];
                System.arraycopy( iv, 0, output, 0, iv.length );
                System.arraycopy( encrypted, 0, output, iv.length, encrypted.length );

                return output;

            } else {

                return buildCipher( Cipher.ENCRYPT_MODE, key ).doFinal( data );

            }
        } catch ( GeneralSecurityException ex ) {
            throw new QiCryptoFailure( ex.getMessage(), ex );
        }
    }

    @Override
    public byte[] decipher( byte[] data, byte[] key )
    {
        try {
            if ( useIV() ) {

                byte[] iv = new byte[ getIVBytesLength() ];
                System.arraycopy( data, 0, iv, 0, iv.length );

                int cipheredSize = data.length - iv.length;
                byte[] ciphered = new byte[ cipheredSize ];
                System.arraycopy( data, iv.length, ciphered, 0, cipheredSize );

                Cipher cipher = buildCipher( Cipher.DECRYPT_MODE, key, iv );
                return cipher.doFinal( ciphered );

            } else {

                Cipher cipher = buildCipher( Cipher.DECRYPT_MODE, key );
                return cipher.doFinal( data );

            }

        } catch ( GeneralSecurityException ex ) {
            throw new QiCryptoFailure( ex.getMessage(), ex );
        }
    }

    private boolean useIV()
    {
        switch ( mode ) {
            case ECB:
                return false;
            default:
                return true;
        }
    }

    private int getIVBytesLength()
    {
        switch ( algo ) {
            case Blowfish:
                return 8;
            case TripleDES:
            case CAST_128:
            case XTEA:
            case TEA:
                switch ( mode ) {
                    case CBC:
                        return 8;
                }
            case DES:
                switch ( mode ) {
                    case CBC:
                    case CFB:
                    case OFB:
                        return 8;
                }
            default:
                return 16;
        }
    }

    private byte[] generateIV()
    {
        byte[] ivBytes = new byte[ getIVBytesLength() ];
        random.nextBytes( ivBytes );
        return ivBytes;
    }

    private String buildAlgorithmString()
    {
        return new Transformation( algo, mode, padding ).jcaTransformation();
    }

    private Cipher buildCipher( int cipherMode, byte[] key, byte[] iv )
            throws GeneralSecurityException
    {
        Cipher cipher = Cipher.getInstance( buildAlgorithmString() );
        // FIXME Find a way to provide the SecureRandom
        cipher.init( cipherMode,
                     new SecretKeySpec( key, algo.jcaString() ),
                     new IvParameterSpec( iv ) );
        return cipher;
    }

    private Cipher buildCipher( int cipherMode, byte[] key )
            throws GeneralSecurityException
    {
        Cipher cipher = Cipher.getInstance( buildAlgorithmString() );
        // FIXME Find a way to provide the SecureRandom
        cipher.init( cipherMode,
                     new SecretKeySpec( key, algo.jcaString() ) );
        return cipher;
    }

}
