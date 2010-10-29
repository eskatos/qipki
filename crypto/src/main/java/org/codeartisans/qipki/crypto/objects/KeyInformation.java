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
package org.codeartisans.qipki.crypto.objects;

import java.security.Key;
import java.security.PublicKey;
import java.security.interfaces.DSAKey;
import java.security.interfaces.RSAKey;
import javax.crypto.interfaces.DHKey;

import org.codeartisans.qipki.crypto.QiCryptoFailure;

import org.qi4j.api.injection.scope.Uses;

public class KeyInformation
{

    private final Key key;

    public KeyInformation( @Uses PublicKey publicKey )
    {
        this.key = publicKey;
    }

    public String getKeyAlgorithm()
    {
        return key.getAlgorithm();
    }

    public String getASN1EncodingFormat()
    {
        return key.getFormat();
    }

    public int getKeySize()
    {
        if ( PublicKey.class.isAssignableFrom( key.getClass() ) ) {
            if ( RSAKey.class.isAssignableFrom( key.getClass() ) ) {
                return ( ( RSAKey ) key ).getModulus().bitLength();
            } else if ( DSAKey.class.isAssignableFrom( key.getClass() ) ) {
                return ( ( DSAKey ) key ).getParams().getP().bitLength();
            } else if ( DHKey.class.isAssignableFrom( key.getClass() ) ) {
                return ( ( DHKey ) key ).getParams().getP().bitLength();
            }
            throw new QiCryptoFailure( "Unsupported PublicKey type" );
        }
        throw new QiCryptoFailure( "Unsupported Key type" );
    }

}
