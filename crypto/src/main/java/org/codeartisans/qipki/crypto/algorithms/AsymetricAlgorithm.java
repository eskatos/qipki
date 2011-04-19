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
 * Asymetric cryptography algorithm.
 */
public enum AsymetricAlgorithm
{
    /**
     * @see http://en.wikipedia.org/wiki/RSA
     */
    RSA( "RSA" ),
    /**
     * @see http://en.wikipedia.org/wiki/ECDSA
     */
    ECDSA( "ECDSA" );
    private String algo;

    private AsymetricAlgorithm( String algo )
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
