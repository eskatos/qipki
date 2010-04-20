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

public enum SignatureAlgorithm
{

    // DSA currently just supports SHA-1.
    SHA1withDSA( "SHA1withDSA" ),
    // ECDSA is support with both the SHA-1 and SHA-2 family of digest algorithms.
    SHA1withECDSA( "SHA1withECDSA" ),
    SHA224withECDSA( "SHA224withECDSA" ),
    SHA256withECDSA( "SHA256withECDSA" ),
    SHA384withECDSA( "SHA384withECDSA" ),
    SHA512withECDSA( "SHA512withECDSA" ),
    // A variety of digests can be used to sign using the RSA algorithm
    MD2withRSA( "MD2withRSA" ),
    MD5withRSA( "MD5withRSA" ),
    SHA1withRSA( "SHA1withRSA" ),
    SHA224withRSA( "SHA224withRSA" ),
    SHA256withRSA( "SHA256withRSA" ),
    SHA384withRSA( "SHA384withRSA" ),
    SHA512withRSA( "SHA512withRSA" ),
    RIPEMD160withRSA( "RIPEMD160withRSA" ),
    RIPEMD128withRSA( "RIPEMD128withRSA" ),
    RIPEMD256withRSA( "RIPEMD256withRSA" );
    private String algo;

    private SignatureAlgorithm( String algo )
    {
        this.algo = algo;
    }

    public String algoString()
    {
        return algo;
    }

}
