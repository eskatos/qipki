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
 * @see http://en.wikipedia.org/wiki/HMAC
 */
public enum MACAlgorithm
{

    /**
     * The HMAC-MD5 keyed-hashing algorithm as defined in RFC 2104: "HMAC: Keyed-Hashing for Message Authentication" (February 1997).
     */
    HmacMD5( "HmacMD5" ),
    /**
     * The HMAC-SHA1 keyed-hashing algorithm as defined in RFC 2104: "HMAC: Keyed-Hashing for Message Authentication" (February 1997).
     */
    HmacSHA1( "HmacSHA1" ),
    /**
     * The HmacSHA256 algorithm as defined in RFC 2104 "HMAC: Keyed-Hashing for Message Authentication" (February 1997) with SHA-256 as the message digest algorithm.
     */
    HmacSHA256( "HmacSHA256" ),
    /**
     * The HmacSHA384 algorithm as defined in RFC 2104 "HMAC: Keyed-Hashing for Message Authentication" (February 1997) with SHA-384 as the message digest algorithm.
     */
    HmacSHA384( "HmacSHA384" ),
    /**
     * The HmacSHA512 algorithm as defined in RFC 2104 "HMAC: Keyed-Hashing for Message Authentication" (February 1997) with SHA-512 as the message digest algorithm.
     */
    HmacSHA512( "HmacSHA512" );
    private String algo;

    private MACAlgorithm( String algo )
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
