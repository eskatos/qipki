/*
 * Copyright (c) 2010 Paul Merlin <paul@nosphere.org>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.codeartisans.qipki.core.crypto.algorithms;

/**
 * See http://en.wikipedia.org/wiki/Block_cipher_modes_of_operation
 */
public interface BlockCipherModeOfOperation
{

    /**
     * The simplest of the encryption modes is the electronic codebook (ECB) mode.
     * The message is divided into blocks and each block is encrypted separately.
     */
    String ECB = "ECB";
    /**
     * Each block of plaintext is XORed with the previous ciphertext block before being encrypted.
     * This way, each ciphertext block is dependent on all plaintext blocks processed up to that point.
     * Also, to make each message unique, an initialization vector must be used in the first block.
     */
    String CBC = "CBC";
    /**
     * The propagating cipher-block chaining or plaintext cipher-block chaining[2] mode was designed to cause small
     * changes in the ciphertext to propagate indefinitely when decrypting, as well as when encrypting.
     */
    String PCBC = "PCBC";
    /**
     * Close relative of CBC, makes a block cipher into a self-synchronizing stream cipher.
     * Operation is very similar; in particular, CFB decryption is almost identical to CBC encryption performed
     * in reverse
     */
    String CFB = "CFB";
    /**
     * Makes a block cipher into a synchronous stream cipher. It generates keystream blocks, which are then XORed
     * with the plaintext blocks to get the ciphertext. Just as with other stream ciphers, flipping a bit in the
     * ciphertext produces a flipped bit in the plaintext at the same location.
     * This property allows many error correcting codes to function normally even when applied before encryption.
     */
    String OFB = "OFB";
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
    String SIC = "SIC";
}
