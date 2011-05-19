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
 * Block Cipher Mode Of Operation.
 * 
 * See http://en.wikipedia.org/wiki/Block_cipher_modes_of_operation
 */
public enum BlockCipherModeOfOperation
        implements Algorithm
{

    /**
     * Segmented Integer Counter (SIC) mode is also known as CTR mode (CM) and Integer Counter Mode (ICM).
     *
     * Turns a block cipher into a stream cipher. It generates the next keystream block by encrypting successive
     * values of a "counter". The counter can be any function which produces a sequence which is guaranteed not
     * to repeat for a long time, although an actual counter is the simplest and most popular.
     * The usage of a simple deterministic input function used to be controversial; critics argued that
     * "deliberately exposing a cryptosystem to a known systematic input represents an unnecessary risk."
     * By now, SIC mode is widely accepted, and problems resulting from the input function are recognized as a
     * weakness of the underlying block cipher instead of the SIC mode.
     * Nevertheless, there are specialised attacks like a Hardware Fault Attack that is based on the usage of a
     * simple counter function as input.
     * SIC mode has similar characteristics to OFB, but also allows a random access property during decryption.
     * SIC mode is well suited to operation on a multi-processor machine where blocks can be encrypted in parallel.
     */
    SIC( "SIC" ),
    /**
     * Each block of plaintext is XORed with the previous ciphertext block before being encrypted.
     * This way, each ciphertext block is dependent on all plaintext blocks processed up to that point.
     * Also, to make each message unique, an initialization vector must be used in the first block.
     */
    CBC( "CBC" ),
    /**
     * Close relative of CBC, makes a block cipher into a self-synchronizing stream cipher.
     * Operation is very similar; in particular, CFB decryption is almost identical to CBC encryption performed
     * in reverse
     */
    CFB( "CFB" ),
    /**
     * Makes a block cipher into a synchronous stream cipher. It generates keystream blocks, which are then XORed
     * with the plaintext blocks to get the ciphertext. Just as with other stream ciphers, flipping a bit in the
     * ciphertext produces a flipped bit in the plaintext at the same location.
     * This property allows many error correcting codes to function normally even when applied before encryption.
     */
    OFB( "OFB" ),
    /**
     * The simplest of the encryption modes is the electronic codebook (ECB) mode.
     * The message is divided into blocks and each block is encrypted separately.
     *
     * @deprecated This mode do not support initialization vectors which are considered mandatory for good encryption.
     */
    @Deprecated
    ECB( "ECB" );
    private String jcaString;

    private BlockCipherModeOfOperation( String jcaString )
    {
        this.jcaString = jcaString;
    }

    @Override
    public String jcaString()
    {
        return jcaString;
    }

}
