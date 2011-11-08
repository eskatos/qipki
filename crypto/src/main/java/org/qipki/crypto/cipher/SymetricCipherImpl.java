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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.EnumSet;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.codeartisans.java.toolbox.io.IO;

import org.qipki.crypto.CryptoFailure;
import org.qipki.crypto.algorithms.BlockCipherModeOfOperation;
import org.qipki.crypto.algorithms.BlockCipherPadding;
import org.qipki.crypto.algorithms.IllegalAlgorithmException;
import org.qipki.crypto.algorithms.SymetricAlgorithm;
import org.qipki.crypto.jca.Transformation;
import org.qipki.crypto.random.Random;

public class SymetricCipherImpl
        implements SymetricCipher
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

    /* package */ SymetricCipherImpl( Random random, SymetricAlgorithm algo, BlockCipherModeOfOperation mode, BlockCipherPadding padding )
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
    public byte[] cipher( byte[] data, byte[] key )
    {
        InputStream in = new ByteArrayInputStream( data );
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {

            cipher( in, out, key );
            return out.toByteArray();

        } finally {
            IO.closeSilently( out );
            IO.closeSilently( in );
        }
    }

    @Override
    public void cipher( InputStream in, OutputStream out, Key key )
    {
        cipher( in, out, key.getEncoded() );
    }

    @Override
    public void cipher( InputStream in, OutputStream out, byte[] key )
    {
        try {

            Cipher cipher;
            if ( useIV() ) {
                byte[] iv = generateIV();
                cipher = buildCipher( Cipher.ENCRYPT_MODE, key, iv );
                out.write( iv );
            } else {
                cipher = buildCipher( Cipher.ENCRYPT_MODE, key );
            }
            process( cipher, in, out );

        } catch ( IOException ex ) {
            throw new CryptoFailure( ex.getMessage(), ex );
        } catch ( GeneralSecurityException ex ) {
            throw new CryptoFailure( ex.getMessage(), ex );
        }
    }

    @Override
    public byte[] decipher( byte[] ciphered, Key key )
    {
        return decipher( ciphered, key.getEncoded() );
    }

    @Override
    public byte[] decipher( byte[] data, byte[] key )
    {
        InputStream in = new ByteArrayInputStream( data );
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {

            decipher( in, out, key );
            return out.toByteArray();

        } finally {
            IO.closeSilently( out );
            IO.closeSilently( in );
        }
    }

    @Override
    public void decipher( InputStream in, OutputStream out, Key key )
    {
        decipher( in, out, key.getEncoded() );
    }

    @Override
    public void decipher( InputStream in, OutputStream out, byte[] key )
    {
        try {

            Cipher cipher;
            if ( useIV() ) {
                byte[] iv = new byte[ getIVBytesLength() ];
                int len = in.read( iv, 0, iv.length );
                if ( len != iv.length ) {
                    throw new CryptoFailure( "Unable to read IV, not enough bytes" );
                }
                cipher = buildCipher( Cipher.DECRYPT_MODE, key, iv );
            } else {
                cipher = buildCipher( Cipher.DECRYPT_MODE, key );
            }
            process( cipher, in, out );

        } catch ( IOException ex ) {
            throw new CryptoFailure( ex.getMessage(), ex );
        } catch ( GeneralSecurityException ex ) {
            throw new CryptoFailure( ex.getMessage(), ex );
        }
    }

    private void process( Cipher cipher, InputStream in, OutputStream out )
            throws IOException, IllegalBlockSizeException, BadPaddingException
    {
        int blockSize = cipher.getBlockSize();
        int outputSize = cipher.getOutputSize( blockSize );
        byte[] inBytes = new byte[ blockSize ];
        byte[] outBytes = new byte[ outputSize ];

        int inLen = 0;
        boolean done = false;
        while ( !done ) {
            inLen = in.read( inBytes );
            if ( inLen == blockSize ) {
                try {
                    int outLength = cipher.update( inBytes, 0, blockSize, outBytes );
                    out.write( outBytes, 0, outLength );
                } catch ( ShortBufferException ex ) {
                    throw new CryptoFailure( "The underlying cipher is a block cipher and the input data is too short to result in a new block.", ex );
                }
            } else {
                done = true;
            }
        }
        if ( inLen > 0 ) {
            outBytes = cipher.doFinal( inBytes, 0, inLen );
        } else {
            outBytes = cipher.doFinal();
        }
        out.write( outBytes );
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
