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
package org.codeartisans.qipki.crypto.cipher;

import org.codeartisans.qipki.crypto.algorithms.BlockCipherModeOfOperation;
import org.codeartisans.qipki.crypto.algorithms.BlockCipherPadding;
import org.codeartisans.qipki.crypto.algorithms.SymetricAlgorithm;

public class CipherFactoryImpl
        implements CipherFactory
{

    @Override
    public BlockCipher newBlockCipher( SymetricAlgorithm algo, BlockCipherModeOfOperation mode, BlockCipherPadding padding )
    {
        return new BlockCipherImpl( algo, mode, padding );
    }

}
