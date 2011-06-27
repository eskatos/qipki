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
import java.security.GeneralSecurityException;
import javax.crypto.Mac;

import org.qi4j.api.injection.scope.Service;

import org.qipki.crypto.CryptoContext;
import org.qipki.crypto.CryptoFailure;
import org.qipki.crypto.codec.CryptCodex;

public class MACImpl
        implements MAC
{

    private static final int BUFFER_SIZE = 128;
    private final CryptoContext cryptoContext;
    private final CryptCodex cryptCodex;

    public MACImpl( @Service CryptoContext cryptoContext, @Service CryptCodex cryptCodex )
    {
        this.cryptoContext = cryptoContext;
        this.cryptCodex = cryptCodex;
    }

    @Override
    public byte[] mac( InputStream data, MACParameters params )
    {
        try {
            Mac mac = Mac.getInstance( params.algorithm().jcaString(), cryptoContext.providerName() );
            mac.init( params.secretKey() );
            byte[] buffer = new byte[ BUFFER_SIZE ];
            int length = 0;
            while ( ( length = data.read( buffer ) ) != -1 ) {
                mac.update( buffer, 0, length );
            }
            return mac.doFinal();
        } catch ( IOException ex ) {
            throw new CryptoFailure( "Unable to read data to MAC with " + params.algorithm().jcaString(), ex );
        } catch ( GeneralSecurityException ex ) {
            throw new CryptoFailure( "Unable to MAC using " + params.algorithm().jcaString(), ex );
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
        return cryptCodex.toBase64String( mac( data, params ) );
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
        return cryptCodex.toBase64String( mac( data, params ) );
    }

}
