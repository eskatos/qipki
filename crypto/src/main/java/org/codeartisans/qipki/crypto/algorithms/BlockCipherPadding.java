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
 * Block Cipher Padding Algorithms.
 * 
 * @see http://en.wikipedia.org/wiki/Block_cipher_modes_of_operation#Padding
 *
 * The NoPadding algorithm is not supported on purpose because it mandates that the input length is compatible with the
 * cipher algorithm. This decision tighten the whole api and so keep it simple.
 */
public enum BlockCipherPadding
        implements Algorithm
{

    ZERO_BYTE( "ZeroBytePadding" ),
    PKCS5( "PKCS5Padding" ),
    PKCS7( "PKCS7Padding" ),
    ISO10126( "ISO10126Padding" ),
    TBC( "TBCPadding" );
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
