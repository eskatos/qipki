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
package org.qipki.crypto.storage;

import org.codeartisans.java.toolbox.exceptions.NullArgumentException;

public enum KeyStoreType
{

    JCEKS( StringValue.JCEKS, FileExtension.JCEKS ),
    /**
     * @see http://en.wikipedia.org/wiki/Jks
     */
    JKS( StringValue.JKS, FileExtension.JKS ),
    /**
     * @see http://en.wikipedia.org/wiki/PKCS12
     */
    PKCS12( StringValue.PKCS12, FileExtension.PKCS12 ),
    /**
     * @see http://en.wikipedia.org/wiki/PKCS11
     */
    PKCS11( StringValue.PKCS11, null );

    @SuppressWarnings( "PublicInnerClass" )
    public interface StringValue
    {

        String JCEKS = "JCEKS";
        String JKS = "JKS";
        String PKCS12 = "PKCS12";
        String PKCS11 = "PKCS11";
    }

    @SuppressWarnings( "PublicInnerClass" )
    public interface FileExtension
    {

        String JCEKS = "jceks";
        String JKS = "jks";
        String PKCS12 = "p12";
    }

    private String string;
    private String fileExtension;

    private KeyStoreType( String string, String fileExtension )
    {
        this.string = string;
        this.fileExtension = fileExtension;
    }

    /**
     * @see http://download.oracle.com/javase/6/docs/technotes/guides/security/crypto/CryptoSpec.html
     * @return The Java Keystore type String
     */
    public String typeString()
    {
        return string;
    }

    public String fileExtension()
    {
        return fileExtension;
    }

    public static KeyStoreType valueOfTypeString( String typeString )
    {
        NullArgumentException.ensureNotEmpty( "Type String", typeString );
        if ( StringValue.JCEKS.equalsIgnoreCase( typeString ) ) {
            return JCEKS;
        }
        if ( StringValue.JKS.equalsIgnoreCase( typeString ) ) {
            return JKS;
        }
        if ( StringValue.PKCS12.equalsIgnoreCase( typeString ) ) {
            return PKCS12;
        }
        if ( StringValue.PKCS11.equalsIgnoreCase( typeString ) ) {
            return PKCS11;
        }
        throw new IllegalArgumentException( "Unsupported KeyStoreType: " + typeString );
    }

}
