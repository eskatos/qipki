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
package org.codeartisans.qipki.core.crypto.digest;

import org.codeartisans.qipki.core.crypto.algorithms.DigestAlgorithm;

public class DigestParameters
{

    private final DigestAlgorithm algo;
    private final byte[] salt;
    private final int iterations;

    public DigestParameters( DigestAlgorithm algo )
    {
        this( algo, null, 1 );
    }

    public DigestParameters( DigestAlgorithm algo, byte[] salt )
    {
        this( algo, salt, 1 );
    }

    public DigestParameters( DigestAlgorithm algo, int iterations )
    {
        this( algo, null, iterations );
    }

    public DigestParameters( DigestAlgorithm algo, byte[] salt, int iterations )
    {
        this.algo = algo;
        this.salt = salt;
        this.iterations = iterations;
    }

    public DigestAlgorithm algo()
    {
        return algo;
    }

    public int iterations()
    {
        return iterations;
    }

    public byte[] salt()
    {
        return salt;
    }

}
