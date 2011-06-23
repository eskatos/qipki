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
package org.qipki.crypto.jca;

import org.qipki.crypto.algorithms.Algorithm;
import org.qipki.crypto.algorithms.BlockCipherModeOfOperation;
import org.qipki.crypto.algorithms.BlockCipherPadding;

/**
 * Represent a JCA "transformation".
 *
 * @see http://download.oracle.com/javase/6/docs/technotes/guides/security/crypto/CryptoSpec.html
 */
public final class Transformation
{

    private final Algorithm algo;
    private BlockCipherModeOfOperation mode;
    private BlockCipherPadding padding;

    public Transformation( Algorithm algo )
    {
        this.algo = algo;
    }

    public Transformation( Algorithm algo, BlockCipherModeOfOperation mode, BlockCipherPadding padding )
    {
        this.algo = algo;
        this.mode = mode;
        this.padding = padding;
    }

    public String jcaTransformation()
    {
        StringBuilder sb = new StringBuilder( algo.jcaString() );
        if ( mode != null && padding != null ) {
            sb.append( "/" ).append( mode.jcaString() ).append( "/" ).append( padding.jcaString() );
        }
        return sb.toString();
    }

    @Override
    public String toString()
    {
        return jcaTransformation();
    }

}
