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
package org.qipki.crypto.mac;

import java.security.Key;
import org.qipki.crypto.algorithms.BCMACAlgorithm;
import org.qipki.crypto.algorithms.BlockCipherAlgorithm;

/**
 * Block Cipher based MAC parameters.
 */
public class BCMACParameters
{

    private final BCMACAlgorithm macAlgo;
    private final BlockCipherAlgorithm blockCipherAlgo;
    private final Key secretKey;

    public BCMACParameters( BCMACAlgorithm macAlgo, BlockCipherAlgorithm blockCipherAlgo, Key secretKey )
    {
        this.macAlgo = macAlgo;
        this.blockCipherAlgo = blockCipherAlgo;
        this.secretKey = secretKey;
    }

    public BCMACAlgorithm macAlgorithm()
    {
        return macAlgo;
    }

    public BlockCipherAlgorithm blockCipherAlgorithm()
    {
        return blockCipherAlgo;
    }

    public Key secretKey()
    {
        return secretKey;
    }

}
