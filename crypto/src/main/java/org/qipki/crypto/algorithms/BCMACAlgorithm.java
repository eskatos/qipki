/*
 * Copyright 2012, Paul Merlin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.qipki.crypto.algorithms;

/**
 * Block Cipher based MAC algorithm.
 */
public enum BCMACAlgorithm
    implements Algorithm
{

    /**
     * In cryptography, a cipher block chaining message authentication code
     * (CBC-MAC), is a technique for constructing a message authentication code
     * from a block cipher. The message is encrypted with some block cipher
     * algorithm in CBC mode to create a chain of blocks such that each block
     * depends on the proper encryption of the previous block. This
     * interdependence ensures that a change to any of the plaintext bits will
     * cause the final encrypted block to change in a way that cannot be
     * predicted or counteracted without knowing the key to the block cipher.
     *
     * @see http://en.wikipedia.org/wiki/CBC-MAC
     */
    CBC_MAC( "CBCMac" ),
    /**
     * CMAC (Cipher-based MAC) is a block cipher-based message authentication
     * code algorithm. It may be used to provide assurance of the authenticity
     * and, hence, the integrity of binary data. This mode of operation fixes
     * security deficiencies of CBC-MAC (CBC-MAC is secure only for
     * fixed-length messages).
     *
     * @see http://en.wikipedia.org/wiki/CMAC
     */
    CMAC( "CMAC" ),
    /**
     * Poly1305-AES computes a 16-byte authenticator of a message of any
     * length, using a 16-byte nonce (unique message number) and a 32-byte
     * secret key. Attackers can't modify or forge messages if the message
     * sender transmits an authenticator along with each message and the
     * message receiver checks each authenticator.
     *
     * @see http://cr.yp.to/mac.html
     */
    Poly1305_AES( "Poly1305-AES" );
    protected String algo;

    private BCMACAlgorithm( String algo )
    {
        this.algo = algo;
    }

    @Override
    public String jcaString()
    {
        return algo;
    }

}
