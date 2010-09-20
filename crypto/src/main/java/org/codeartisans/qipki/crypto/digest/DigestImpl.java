/*
 * Copyright (c) 2010 Paul Merlin <paul@nosphere.org>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.codeartisans.qipki.crypto.digest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.codeartisans.qipki.crypto.QiCryptoFailure;
import org.codeartisans.qipki.crypto.codec.CryptCodex;

import org.qi4j.api.injection.scope.Service;

public class DigestImpl
        implements Digest
{

    private static final int BUFFER_SIZE = 128;
    private final CryptCodex cryptCodex;

    public DigestImpl( @Service CryptCodex cryptCodex )
    {
        this.cryptCodex = cryptCodex;
    }

    @Override
    public byte[] digest( InputStream data, DigestParameters params )
    {
        try {
            MessageDigest digest = MessageDigest.getInstance( params.algorithm().algoString(), BouncyCastleProvider.PROVIDER_NAME );
            if ( params.salt() != null ) {
                digest.update( params.salt() );
            }
            byte[] buffer = new byte[ BUFFER_SIZE ];
            int length = 0;
            while ( ( length = data.read( buffer ) ) != -1 ) {
                digest.update( buffer, 0, length );
            }
            byte[] hashed = digest.digest();
            for ( int idx = 1; idx < params.iterations(); idx++ ) {
                digest.reset();
                hashed = digest.digest( hashed );
            }
            return hashed;
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to read data to digest with " + params.algorithm().algoString(), ex );
        } catch ( GeneralSecurityException ex ) {
            throw new QiCryptoFailure( "Unable to digest using " + params.algorithm().algoString(), ex );
        }
    }

    @Override
    public String hexDigest( InputStream data, DigestParameters params )
    {
        return cryptCodex.toHexString( digest( data, params ) );
    }

    @Override
    public String base64Digest( InputStream data, DigestParameters params )
    {
        return cryptCodex.toBase64( digest( data, params ) );
    }

    @Override
    public byte[] digest( byte[] data, DigestParameters params )
    {
        return digest( new ByteArrayInputStream( data ), params );
    }

    @Override
    public String hexDigest( byte[] data, DigestParameters params )
    {
        return cryptCodex.toHexString( digest( data, params ) );
    }

    @Override
    public String base64Digest( byte[] data, DigestParameters params )
    {
        return cryptCodex.toBase64( digest( data, params ) );
    }

}
