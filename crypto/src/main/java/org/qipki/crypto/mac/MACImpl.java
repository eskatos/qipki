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
package org.qipki.crypto.mac;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import javax.crypto.Mac;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.macs.CBCBlockCipherMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.qi4j.api.injection.scope.Service;
import org.qipki.crypto.CryptoContext;
import org.qipki.crypto.CryptoFailure;
import org.qipki.crypto.algorithms.IllegalAlgorithmException;
import org.qipki.crypto.codec.CryptCodex;
import org.qipki.crypto.constants.IOConstants;
import org.qipki.crypto.jca.Transformation;

/**
 * @see http://www.developer.com/java/ent/article.php/3787701/Message-Authentication-Unlocking-the-Secrets-of-the-Java-Cryptography-Extensions.htm
 */
public class MACImpl
    implements MAC
{

    private final CryptoContext cryptoContext;
    private final CryptCodex cryptCodex;

    public MACImpl( @Service CryptoContext cryptoContext, @Service CryptCodex cryptCodex )
    {
        this.cryptoContext = cryptoContext;
        this.cryptCodex = cryptCodex;
    }

    @Override
    public byte[] mac( InputStream data, HMACParameters params )
    {
        switch( params.algorithm() )
        {
            case UMAC:
                throw new UnsupportedOperationException( "TODO Implement UMAC http://en.wikipedia.org/wiki/UMAC" );
            default:
                String jcaTransformation = params.algorithm().jcaString();
                try
                {
                    Mac mac = Mac.getInstance( jcaTransformation, cryptoContext.providerName() );
                    mac.init( params.secretKey() );
                    byte[] buffer = new byte[ IOConstants.INTERNAL_BUFFERS_SIZE ];
                    int length = 0;
                    while( ( length = data.read( buffer ) ) != -1 )
                    {
                        mac.update( buffer, 0, length );
                    }
                    return mac.doFinal();
                }
                catch( IOException ex )
                {
                    throw new CryptoFailure( "Unable to read data to MAC with " + jcaTransformation, ex );
                }
                catch( GeneralSecurityException ex )
                {
                    throw new CryptoFailure( "Unable to MAC using " + jcaTransformation, ex );
                }
        }
    }

    @Override
    public String hexMac( InputStream data, HMACParameters params )
    {
        return cryptCodex.toHexString( mac( data, params ) );
    }

    @Override
    public String base64Mac( InputStream data, HMACParameters params )
    {
        return cryptCodex.toBase64String( mac( data, params ) );
    }

    @Override
    public byte[] mac( byte[] data, HMACParameters params )
    {
        return mac( new ByteArrayInputStream( data ), params );
    }

    @Override
    public String hexMac( byte[] data, HMACParameters params )
    {
        return cryptCodex.toHexString( mac( data, params ) );
    }

    @Override
    public String base64Mac( byte[] data, HMACParameters params )
    {
        return cryptCodex.toBase64String( mac( data, params ) );
    }

    @Override
    public byte[] mac( String data, HMACParameters params )
    {
        try
        {
            return mac( data.getBytes( IOConstants.UTF_8 ), params );
        }
        catch( UnsupportedEncodingException ex )
        {
            throw new CryptoFailure( "UTF-8 not supported! o_O ", ex );
        }
    }

    @Override
    public String hexMac( String data, HMACParameters params )
    {
        return cryptCodex.toHexString( mac( data, params ) );
    }

    @Override
    public String base64Mac( String data, HMACParameters params )
    {
        return cryptCodex.toBase64String( mac( data, params ) );
    }

    @Override
    public byte[] mac( String data, String encoding, HMACParameters params )
        throws UnsupportedEncodingException
    {
        return mac( data.getBytes( encoding ), params );
    }

    @Override
    public String hexMac( String data, String encoding, HMACParameters params )
        throws UnsupportedEncodingException
    {
        return cryptCodex.toHexString( mac( data, encoding, params ) );
    }

    @Override
    public String base64Mac( String data, String encoding, HMACParameters params )
        throws UnsupportedEncodingException
    {
        return cryptCodex.toBase64String( mac( data, encoding, params ) );
    }

    @Override
    public byte[] mac( InputStream data, BCMACParameters params )
    {

        String jcaTransformation = new Transformation( params.macAlgorithm(),
                                                       params.blockCipherAlgorithm() ).jcaTransformation();

        org.bouncycastle.crypto.BlockCipher blockCipher;
        org.bouncycastle.crypto.Mac mac;
        switch( params.macAlgorithm() )
        {
            case CMAC:
                throw new IllegalAlgorithmException( jcaTransformation );
            case CBC_MAC:
                switch( params.blockCipherAlgorithm() )
                {
                    case AES:
                        blockCipher = new AESEngine();
                        mac = new CBCBlockCipherMac( blockCipher );
                        break;
                    default:
                        throw new IllegalAlgorithmException( jcaTransformation );
                }
                break;
            case Poly1305_AES:
            default:
                throw new IllegalAlgorithmException( jcaTransformation );
        }
        try
        {
            mac.init( new KeyParameter( params.secretKey().getEncoded() ) );
            byte[] buffer = new byte[ IOConstants.INTERNAL_BUFFERS_SIZE ];
            int length = 0;
            while( ( length = data.read( buffer ) ) != -1 )
            {
                mac.update( buffer, 0, length );
            }
            byte[] macBytes = new byte[ blockCipher.getBlockSize() / 2 ];
            mac.doFinal( macBytes, 0 );
            return macBytes;
        }
        catch( IOException ex )
        {
            throw new CryptoFailure( "Unable to read data to MAC with " + jcaTransformation, ex );
        }
    }

    @Override
    public String hexMac( InputStream data, BCMACParameters params )
    {
        return cryptCodex.toHexString( mac( data, params ) );
    }

    @Override
    public String base64Mac( InputStream data, BCMACParameters params )
    {
        return cryptCodex.toBase64String( mac( data, params ) );
    }

    @Override
    public byte[] mac( byte[] data, BCMACParameters params )
    {
        return mac( new ByteArrayInputStream( data ), params );
    }

    @Override
    public String hexMac( byte[] data, BCMACParameters params )
    {
        return cryptCodex.toHexString( mac( data, params ) );
    }

    @Override
    public String base64Mac( byte[] data, BCMACParameters params )
    {
        return cryptCodex.toBase64String( mac( data, params ) );
    }

    @Override
    public byte[] mac( String data, BCMACParameters params )
    {
        try
        {
            return mac( data.getBytes( IOConstants.UTF_8 ), params );
        }
        catch( UnsupportedEncodingException ex )
        {
            throw new CryptoFailure( "UTF-8 not supported! o_O ", ex );
        }
    }

    @Override
    public String hexMac( String data, BCMACParameters params )
    {
        return cryptCodex.toHexString( mac( data, params ) );
    }

    @Override
    public String base64Mac( String data, BCMACParameters params )
    {
        return cryptCodex.toBase64String( mac( data, params ) );
    }

    @Override
    public byte[] mac( String data, String encoding, BCMACParameters params )
        throws UnsupportedEncodingException
    {
        return mac( data.getBytes( encoding ), params );
    }

    @Override
    public String hexMac( String data, String encoding, BCMACParameters params )
        throws UnsupportedEncodingException
    {
        return cryptCodex.toHexString( mac( data, encoding, params ) );
    }

    @Override
    public String base64Mac( String data, String encoding, BCMACParameters params )
        throws UnsupportedEncodingException
    {
        return cryptCodex.toBase64String( mac( data, encoding, params ) );
    }

}
