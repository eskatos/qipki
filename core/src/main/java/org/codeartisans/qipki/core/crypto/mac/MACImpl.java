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
package org.codeartisans.qipki.core.crypto.mac;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import javax.crypto.Mac;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.codeartisans.qipki.core.QiPkiFailure;
import org.codeartisans.qipki.core.crypto.codec.CryptCodex;
import org.qi4j.api.injection.scope.Service;

public class MACImpl
        implements MAC
{

    private static final int BUFFER_SIZE = 128;
    private final CryptCodex cryptCodex;

    public MACImpl( @Service CryptCodex cryptCodex )
    {
        this.cryptCodex = cryptCodex;
    }

    @Override
    public byte[] mac( InputStream data, MACParameters params )
    {
        try {
            Mac mac = Mac.getInstance( params.algorithm().algoString(), BouncyCastleProvider.PROVIDER_NAME );
            mac.init( params.secretKey() );
            byte[] buffer = new byte[ BUFFER_SIZE ];
            int length = 0;
            while ( ( length = data.read( buffer ) ) != -1 ) {
                mac.update( buffer, 0, length );
            }
            return mac.doFinal();
        } catch ( IOException ex ) {
            throw new QiPkiFailure( "Unable to read data to MAC with " + params.algorithm().algoString(), ex );
        } catch ( GeneralSecurityException ex ) {
            throw new QiPkiFailure( "Unable to MAC using " + params.algorithm().algoString(), ex );
        }
    }

    @Override
    public String hexMac( InputStream data, MACParameters params )
    {
        return cryptCodex.toHexString( mac( data, params ) );
    }

    @Override
    public String base64Mac( InputStream data, MACParameters params )
    {
        return cryptCodex.toBase64( mac( data, params ) );
    }

    @Override
    public byte[] mac( byte[] data, MACParameters params )
    {
        return mac( new ByteArrayInputStream( data ), params );
    }

    @Override
    public String hexMac( byte[] data, MACParameters params )
    {
        return cryptCodex.toHexString( mac( data, params ) );
    }

    @Override
    public String base64Mac( byte[] data, MACParameters params )
    {
        return cryptCodex.toBase64( mac( data, params ) );
    }

}
