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
package org.qipki.crypto.cipher;

import org.qipki.crypto.algorithms.BlockCipherModeOfOperation;
import org.qipki.crypto.algorithms.BlockCipherPadding;
import org.qipki.crypto.algorithms.SymetricAlgorithm;
import org.qipki.crypto.random.Random;
import org.qi4j.api.injection.scope.Service;

public class CipherFactoryImpl
        implements CipherFactory
{

    private final Random random;

    public CipherFactoryImpl( @Service Random random )
    {
        this.random = random;
    }

    @Override
    public BlockCipher newBlockCipher( SymetricAlgorithm algo, BlockCipherModeOfOperation mode, BlockCipherPadding padding )
    {
        return new BlockCipherImpl( random, algo, mode, padding );
    }

}
