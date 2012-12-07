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
package org.qipki.crypto.algorithms;

/**
 * Hash based MAC algorithm.
 */
public enum HMACAlgorithm
    implements Algorithm
{

    /**
     * The HMAC-MD5 keyed-hashing algorithm as defined in RFC 2104: "HMAC:
     * Keyed-Hashing for Message Authentication" (February 1997).
     *
     * @see http://en.wikipedia.org/wiki/HMAC
     */
    HmacMD5( "HmacMD5" ),
    /**
     * The HMAC-SHA1 keyed-hashing algorithm as defined in RFC 2104: "HMAC:
     * Keyed-Hashing for Message Authentication" (February 1997).
     *
     * @see http://en.wikipedia.org/wiki/HMAC
     */
    HmacSHA1( "HmacSHA1" ),
    /**
     * The HmacSHA256 algorithm as defined in RFC 2104 "HMAC: Keyed-Hashing for
     * Message Authentication" (February 1997) with SHA-256 as the message
     * digest algorithm.
     *
     * @see http://en.wikipedia.org/wiki/HMAC
     */
    HmacSHA256( "HmacSHA256" ),
    /**
     * The HmacSHA384 algorithm as defined in RFC 2104 "HMAC: Keyed-Hashing for
     * Message Authentication" (February 1997) with SHA-384 as the message
     * digest algorithm.
     *
     * @see http://en.wikipedia.org/wiki/HMAC
     */
    HmacSHA384( "HmacSHA384" ),
    /**
     * The HmacSHA512 algorithm as defined in RFC 2104 "HMAC: Keyed-Hashing for
     * Message Authentication" (February 1997) with SHA-512 as the message
     * digest algorithm.
     *
     * @see http://en.wikipedia.org/wiki/HMAC
     */
    HmacSHA512( "HmacSHA512" ),
    /**
     * Message authentication code based on universal hashing, or UMAC, is a
     * type of message authentication code (MAC) calculated choosing a hash
     * function from a class of hash functions according to some secret
     * (random) process and applying it to the message. The resulting
     * digest or fingerprint is then encrypted to hide the identity of the hash
     * function used. A UMAC has provable cryptographic strength and is usually
     * a lot less computationally intensive than other MACs.
     *
     * @see http://en.wikipedia.org/wiki/UMAC
     */
    UMAC( "UCMAC" );
    protected String algo;

    private HMACAlgorithm( String algo )
    {
        this.algo = algo;
    }

    @Override
    public String jcaString()
    {
        return algo;
    }

}
