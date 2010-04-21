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
package org.codeartisans.qipki.crypto.algorithms;

public enum BlockCipherPadding
{

    // Sun JCE
    NoPadding( "NoPadding" ),
    PKCS5Padding( "PKCS5Padding" ),
    SSL3Padding( "SSL3Padding" ),
    ISO10126Padding( "ISO10126Padding" ),
    // Bouncy Castle
    PKCS7Padding( "PKCS7Padding" ),
    ISO10126d2Padding( "ISO10126d2Padding" ),
    ISO7816d4Padding( "ISO7816d4Padding" ),
    X932Padding( "X932Padding" ),
    ZeroBytePadding( "ZeroBytePadding" ),
    TBCPadding( "TBCPadding" );
    private String algo;

    private BlockCipherPadding( String algo )
    {
        this.algo = algo;
    }

    public String algoString()
    {
        return algo;
    }

}
