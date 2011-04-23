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
package org.codeartisans.qipki.crypto.symetric;

import org.codeartisans.qipki.crypto.algorithms.SymetricAlgorithm;

public class SymetricGeneratorParameters
{

    private final SymetricAlgorithm algo;
    private final int keySize;

    public SymetricGeneratorParameters( SymetricAlgorithm algo, int keySize )
    {
        this.algo = algo;
        this.keySize = keySize;
    }

    public SymetricAlgorithm algorithm()
    {
        return algo;
    }

    public int keySize()
    {
        return keySize;
    }

}
