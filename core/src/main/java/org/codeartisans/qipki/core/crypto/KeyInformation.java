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
package org.codeartisans.qipki.core.crypto;

import java.security.Key;
import java.security.PublicKey;
import java.security.interfaces.DSAKey;
import java.security.interfaces.RSAKey;
import javax.crypto.interfaces.DHKey;
import org.codeartisans.qipki.core.QiPkiFailure;
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
            throw new QiPkiFailure( "Unsupported PublicKey type" );
        }
        throw new QiPkiFailure( "Unsupported Key type" );
    }

}
