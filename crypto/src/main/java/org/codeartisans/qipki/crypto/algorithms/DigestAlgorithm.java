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
 * @see http://en.wikipedia.org/wiki/Hash_algorithm
 */
public enum DigestAlgorithm
        implements Algorithm
{

    /**
     * @see http://en.wikipedia.org/wiki/MD2_(cryptography)
     */
    @Deprecated
    MD2( "MD2" ),
    /**
     * @see http://en.wikipedia.org/wiki/MD5
     */
    MD5( "MD5" ),
    /**
     * @see http://en.wikipedia.org/wiki/SHA-1
     */
    SHA_1( "SHA-1" ),
    /**
     * @see http://en.wikipedia.org/wiki/SHA-256
     */
    SHA_256( "SHA-256" ),
    /**
     * @see http://en.wikipedia.org/wiki/SHA-384
     */
    SHA_384( "SHA-384" ),
    /**
     * @see http://en.wikipedia.org/wiki/SHA-512
     */
    SHA_512( "SHA-512" );
    private String jcaString;

    private DigestAlgorithm( String algo )
    {
        this.jcaString = algo;
    }

    @Override
    public String jcaString()
    {
        return jcaString;
    }

}
