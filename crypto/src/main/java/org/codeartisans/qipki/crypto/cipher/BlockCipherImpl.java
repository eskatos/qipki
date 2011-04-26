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

import java.security.Key;

import org.codeartisans.qipki.crypto.algorithms.BlockCipherModeOfOperation;
import org.codeartisans.qipki.crypto.algorithms.BlockCipherPadding;
import org.codeartisans.qipki.crypto.algorithms.SymetricAlgorithm;
import org.codeartisans.qipki.crypto.jca.Transformation;

public class BlockCipherImpl
        implements BlockCipher
{

    private final SymetricAlgorithm algo;
    private final BlockCipherModeOfOperation mode;
    private final BlockCipherPadding padding;

    /* package */ BlockCipherImpl( SymetricAlgorithm algo, BlockCipherModeOfOperation mode, BlockCipherPadding padding )
    {
        this.algo = algo;
        this.mode = mode;
        this.padding = padding;
    }

    @Override
    public byte[] cipher( byte[] data, Key key )
    {
        return cipher( data, key.getEncoded() );
    }

    @Override
    public byte[] decipher( byte[] ciphered, Key key )
    {
        return decipher( ciphered, key.getEncoded() );
    }

    @Override
    public byte[] cipher( byte[] data, byte[] key )
    {
        return data; // TODO Implement me
    }

    @Override
    public byte[] decipher( byte[] ciphered, byte[] key )
    {
        return ciphered; // TODO Implement me
    }

    private String buildAlgorithmString()
    {
        return new Transformation( algo, mode, padding ).jcaTransformation();
    }

}
