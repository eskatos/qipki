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
