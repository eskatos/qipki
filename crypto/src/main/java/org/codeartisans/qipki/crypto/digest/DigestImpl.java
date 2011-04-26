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
            MessageDigest digest = MessageDigest.getInstance( params.algorithm().jcaString(), BouncyCastleProvider.PROVIDER_NAME );
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
            throw new QiCryptoFailure( "Unable to read data to digest with " + params.algorithm().jcaString(), ex );
        } catch ( GeneralSecurityException ex ) {
            throw new QiCryptoFailure( "Unable to digest using " + params.algorithm().jcaString(), ex );
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
