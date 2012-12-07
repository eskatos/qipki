/*
 * Copyright (c) 2011, Paul Merlin. All Rights Reserved.
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

import org.qipki.crypto.algorithms.Algorithm;
import org.qipki.crypto.algorithms.BCMACAlgorithm;
import org.qipki.crypto.algorithms.HMACAlgorithm;

public class SymetricSigningGeneratorParameters
{

    public static final SymetricSigningGeneratorParameters HmacMD5_128 = new SymetricSigningGeneratorParameters( HMACAlgorithm.HmacMD5, 128 );
    public static final SymetricSigningGeneratorParameters HmacSHA1_128 = new SymetricSigningGeneratorParameters( HMACAlgorithm.HmacSHA1, 128 );
    public static final SymetricSigningGeneratorParameters HmacSHA256_256 = new SymetricSigningGeneratorParameters( HMACAlgorithm.HmacSHA256, 256 );
    public static final SymetricSigningGeneratorParameters HmacSHA384_384 = new SymetricSigningGeneratorParameters( HMACAlgorithm.HmacSHA384, 384 );
    public static final SymetricSigningGeneratorParameters HmacSHA512_512 = new SymetricSigningGeneratorParameters( HMACAlgorithm.HmacSHA512, 512 );
    private final Algorithm algorithm;
    private final int keySize;

    public SymetricSigningGeneratorParameters( HMACAlgorithm algorithm, int keySize )
    {
        this.algorithm = algorithm;
        this.keySize = keySize;
    }

    public SymetricSigningGeneratorParameters( BCMACAlgorithm algorithm, int keySize )
    {
        this.algorithm = algorithm;
        this.keySize = keySize;
    }

    public Algorithm algorithm()
    {
        return algorithm;
    }

    public int keySize()
    {
        return keySize;
    }

}
