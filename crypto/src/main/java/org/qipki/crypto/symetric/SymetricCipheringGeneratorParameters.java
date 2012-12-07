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
package org.qipki.crypto.symetric;

import org.qipki.crypto.algorithms.BlockCipherAlgorithm;

public class SymetricCipheringGeneratorParameters
{

    public static final SymetricCipheringGeneratorParameters AES_128 = new SymetricCipheringGeneratorParameters( BlockCipherAlgorithm.AES, 128 );
    public static final SymetricCipheringGeneratorParameters AES_256 = new SymetricCipheringGeneratorParameters( BlockCipherAlgorithm.AES, 256 );
    private final BlockCipherAlgorithm algo;
    private final int keySize;

    public SymetricCipheringGeneratorParameters( BlockCipherAlgorithm algo, int keySize )
    {
        this.algo = algo;
        this.keySize = keySize;
    }

    public BlockCipherAlgorithm algorithm()
    {
        return algo;
    }

    public int keySize()
    {
        return keySize;
    }

}
