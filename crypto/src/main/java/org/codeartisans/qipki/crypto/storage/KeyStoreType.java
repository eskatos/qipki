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
package org.codeartisans.qipki.crypto.storage;

import org.codeartisans.java.toolbox.exceptions.NullArgumentException;

public enum KeyStoreType
{

    JCEKS( StringValues.JCEKS ),
    JKS( StringValues.JKS ),
    PKCS12( StringValues.PKCS12 ),
    PKCS11( StringValues.PKCS11 );

    // Needed ?
    @SuppressWarnings( "PublicInnerClass" )
    public interface StringValues
    {

        String JCEKS = "JCEKS";
        String JKS = "JKS";
        String PKCS12 = "PKCS12";
        String PKCS11 = "PKCS11";
    }

    private String string;

    private KeyStoreType( String string )
    {
        this.string = string;
    }

    public String typeString()
    {
        return string;
    }

    public static KeyStoreType valueOfTypeString( String typeString )
    {
        NullArgumentException.ensureNotEmpty( "Type String", typeString );
        if ( StringValues.JCEKS.equalsIgnoreCase( typeString ) ) {
            return JCEKS;
        }
        if ( StringValues.JKS.equalsIgnoreCase( typeString ) ) {
            return JKS;
        }
        if ( StringValues.PKCS12.equalsIgnoreCase( typeString ) ) {
            return PKCS12;
        }
        if ( StringValues.PKCS11.equalsIgnoreCase( typeString ) ) {
            return PKCS11;
        }
        throw new IllegalArgumentException( "Unsupported KeyStoreType: " + typeString );
    }

}
