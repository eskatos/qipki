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
package org.qipki.crypto.digest;

import org.qipki.crypto.algorithms.DigestAlgorithm;

public class DigestParameters
{

    /**
     * One {@link DigestAlgorithm#MD5} iteration with no salt.
     */
    public static final DigestParameters MD5 = new DigestParameters( DigestAlgorithm.MD5 );
    /**
     * One {@link DigestAlgorithm#SHA_1} iteration with no salt.
     */
    public static final DigestParameters SHA_1 = new DigestParameters( DigestAlgorithm.SHA_1 );
    /**
     * One {@link DigestAlgorithm#SHA_256} iteration with no salt.
     */
    public static final DigestParameters SHA_256 = new DigestParameters( DigestAlgorithm.SHA_256 );
    /**
     * One {@link DigestAlgorithm#SHA_384} iteration with no salt.
     */
    public static final DigestParameters SHA_384 = new DigestParameters( DigestAlgorithm.SHA_384 );
    /**
     * One {@link DigestAlgorithm#SHA_512} iteration with no salt.
     */
    public static final DigestParameters SHA_512 = new DigestParameters( DigestAlgorithm.SHA_512 );
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

    public DigestAlgorithm algorithm()
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
