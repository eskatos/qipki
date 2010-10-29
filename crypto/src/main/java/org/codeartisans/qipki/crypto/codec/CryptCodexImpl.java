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
package org.codeartisans.qipki.crypto.codec;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERString;
import org.bouncycastle.util.encoders.Base64;

import org.codeartisans.qipki.crypto.QiCryptoFailure;

public class CryptCodexImpl
        implements CryptCodex
{

    @Override
    public String toString( DEREncodable obj )
    {
        if ( obj instanceof DERString ) {
            return ( ( DERString ) obj ).getString();
        } else if ( obj instanceof DERInteger ) {
            return ( ( DERInteger ) obj ).getValue().toString();
        } else if ( obj instanceof ASN1TaggedObject ) {
            ASN1TaggedObject tagObj = ( ASN1TaggedObject ) obj;
            return "[" + tagObj.getTagNo() + "] " + toString( tagObj.getObject() );
        } else if ( obj instanceof ASN1Sequence ) {
            ASN1Sequence aObj = ( ASN1Sequence ) obj;
            StringBuilder sb = new StringBuilder( "[" );
            for ( int i = 0, len = aObj.size(); i < len; i++ ) {
                sb.append( toString( aObj.getObjectAt( i ) ) );
                if ( i != len - 1 ) {
                    sb.append( ", " );
                }
            }
            return sb.append( "]" ).toString();
        } else {
            return obj.toString();
        }
    }

    @Override
    public String toHexString( BigInteger bigInt )
    {
        return toHexString( bigInt.toByteArray() );
    }

    @Override
    public String toHexString( byte[] bytes )
    {
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < bytes.length; i++ ) {
            int hexaValue = 0xFF & bytes[i];
            sb.append( String.format( "%02x", hexaValue ) );
        }
        return sb.toString();
    }

    @Override
    public String toHexString( boolean[] bools )
    {
        StringBuilder sb = new StringBuilder( bools.length );
        for ( int idx = 0; idx < bools.length; idx++ ) {
            sb.append( bools[idx] ? "1" : "0" );
        }
        return Integer.toHexString( Integer.parseInt( sb.toString(), 2 ) );
    }

    @Override
    public String toBase64( byte[] bytes )
    {
        try {
            return new String( Base64.encode( bytes ), "UTF-8" );
        } catch ( UnsupportedEncodingException ex ) {
            throw new QiCryptoFailure( "Unable to encode data in Base64", ex );
        }
    }

}
