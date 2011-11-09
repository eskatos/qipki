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
package org.qipki.crypto.cipher;

import org.qipki.crypto.algorithms.BlockCipherModeOfOperation;
import org.qipki.crypto.algorithms.BlockCipherPadding;
import org.qipki.crypto.algorithms.SymetricAlgorithm;

public class SymetricCipherFactoryParameters
{

    public static final SymetricCipherFactoryParameters AES_CBC_PKCS5 = new SymetricCipherFactoryParameters( SymetricAlgorithm.AES, BlockCipherModeOfOperation.CBC, BlockCipherPadding.PKCS5 );
    public static final SymetricCipherFactoryParameters AES_CBC_PKCS7 = new SymetricCipherFactoryParameters( SymetricAlgorithm.AES, BlockCipherModeOfOperation.CBC, BlockCipherPadding.PKCS7 );
    public static final SymetricCipherFactoryParameters AES_SIC_PKCS5 = new SymetricCipherFactoryParameters( SymetricAlgorithm.AES, BlockCipherModeOfOperation.SIC, BlockCipherPadding.PKCS5 );
    public static final SymetricCipherFactoryParameters AES_SIC_PKCS7 = new SymetricCipherFactoryParameters( SymetricAlgorithm.AES, BlockCipherModeOfOperation.SIC, BlockCipherPadding.PKCS7 );
    private final SymetricAlgorithm algorithm;
    private final BlockCipherModeOfOperation mode;
    private final BlockCipherPadding padding;

    public SymetricCipherFactoryParameters( SymetricAlgorithm algorithm, BlockCipherModeOfOperation mode, BlockCipherPadding padding )
    {
        this.algorithm = algorithm;
        this.mode = mode;
        this.padding = padding;
    }

    public SymetricAlgorithm algorithm()
    {
        return algorithm;
    }

    public BlockCipherModeOfOperation mode()
    {
        return mode;
    }

    public BlockCipherPadding padding()
    {
        return padding;
    }

}
