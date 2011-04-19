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
 * @see http://en.wikipedia.org/wiki/Symmetric_key_algorithms
 */
public enum SymetricAlgorithm
{
    /**
     * @see http://en.wikipedia.org/wiki/Advanced_Encryption_Standard
     */
    AES( "AES" ),
    /**
     * @see http://en.wikipedia.org/wiki/Blowfish_(cipher)
     */
    Blowfish( "Blowfish" ),
    /**
     * @see http://en.wikipedia.org/wiki/Data_Encryption_Standard
     */
    DES( "DES" ),
    /**
     * @see http://en.wikipedia.org/wiki/Triple_DES
     */
    TripleDES( "DESede" );
    private String algo;

    private SymetricAlgorithm( String algo )
    {
        this.algo = algo;
    }

    /**
     * @return The Java algorithm String
     */
    public String algoString()
    {
        return algo;
    }

}
