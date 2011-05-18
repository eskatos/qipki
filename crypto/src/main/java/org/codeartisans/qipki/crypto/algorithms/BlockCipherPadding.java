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
package org.codeartisans.qipki.crypto.algorithms;

/**
 * @see http://en.wikipedia.org/wiki/Block_cipher_modes_of_operation#Padding
 */
public enum BlockCipherPadding
        implements Algorithm
{

    // SSL3( "SSL3Padding" ), // Removed in Java 6 ?
    // ISO10126D2( "ISO10126d2Padding" ), // Unknown
    // ISO7816D4( "ISO7816d4Padding" ), // Unknown
    // X932( "X932Padding" ), // Unknown
    NO_PADDING( "NoPadding" ), // Need parameters
    ISO10126( "ISO10126Padding" ), // Need parameters
    PKCS5( "PKCS5Padding" ), // Need parameters
    PKCS7( "PKCS7Padding" ), // Need IV
    ZERO_BYTE( "ZeroBytePadding" ), // Need IV
    TBC( "TBCPadding" ); // Need IV
    private String jcaString;

    private BlockCipherPadding( String jcaString )
    {
        this.jcaString = jcaString;
    }

    @Override
    public String jcaString()
    {
        return jcaString;
    }

}
