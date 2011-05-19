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
 * Symetric Key Algorithm.
 * 
 * @see http://en.wikipedia.org/wiki/Symmetric_key_algorithms
 */
public enum SymetricAlgorithm
        implements Algorithm
{

    /**
     * @see http://en.wikipedia.org/wiki/Rijndael
     */
    Rijndael( "RIJNDAEL" ),
    /**
     * @see http://en.wikipedia.org/wiki/Serpent_(cipher)
     */
    Serpent( "SERPENT" ),
    /**
     * @see http://en.wikipedia.org/wiki/Advanced_Encryption_Standard
     */
    AES( "AES" ),
    /**
     * @see http://en.wikipedia.org/wiki/Camellia_(cipher)
     */
    Camellia( "CAMELLIA" ),
    /**
     * @see http://en.wikipedia.org/wiki/Blowfish_(cipher)
     */
    Blowfish( "Blowfish" ),
    /**
     * @see http://en.wikipedia.org/wiki/Triple_DES
     */
    TripleDES( "DESede" ),
    /**
     * @see http://en.wikipedia.org/wiki/Twofish
     */
    TwoFish( "Twofish" ),
    /**
     * @see http://en.wikipedia.org/wiki/CAST-128
     */
    CAST_128( "CAST5" ),
    /**
     * @see http://en.wikipedia.org/wiki/CAST-256
     */
    CAST_256( "CAST6" ),
    /**
     * @see http://en.wikipedia.org/wiki/XTEA
     */
    XTEA( "XTEA" ),
    /**
     * @see http://en.wikipedia.org/wiki/Tiny_Encryption_Algorithm
     */
    @Deprecated
    TEA( "TEA" ),
    /**
     * @see http://en.wikipedia.org/wiki/Data_Encryption_Standard
     */
    @Deprecated
    DES( "DES" );
    private String jcaString;

    private SymetricAlgorithm( String algo )
    {
        this.jcaString = algo;
    }

    @Override
    public String jcaString()
    {
        return jcaString;
    }

}
