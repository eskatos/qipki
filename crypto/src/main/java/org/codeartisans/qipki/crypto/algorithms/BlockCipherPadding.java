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

public enum BlockCipherPadding
{

    // Sun JCE
    NoPadding( "NoPadding" ),
    PKCS5Padding( "PKCS5Padding" ),
    SSL3Padding( "SSL3Padding" ),
    ISO10126Padding( "ISO10126Padding" ),
    // Bouncy Castle
    PKCS7Padding( "PKCS7Padding" ),
    ISO10126d2Padding( "ISO10126d2Padding" ),
    ISO7816d4Padding( "ISO7816d4Padding" ),
    X932Padding( "X932Padding" ),
    ZeroBytePadding( "ZeroBytePadding" ),
    TBCPadding( "TBCPadding" );
    private String algo;

    private BlockCipherPadding( String algo )
    {
        this.algo = algo;
    }

    public String algoString()
    {
        return algo;
    }

}
