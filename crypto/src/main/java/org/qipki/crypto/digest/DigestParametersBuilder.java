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

public class DigestParametersBuilder
{

    private final Digester digester;
    private DigestAlgorithm algorithm;
    private int saltLength = 0;
    private int iterations = 1;

    /* package */ DigestParametersBuilder( Digester digester )
    {
        this.digester = digester;
    }

    public DigestParametersBuilder using( DigestAlgorithm algorithm )
    {
        this.algorithm = algorithm;
        return this;
    }

    public DigestParametersBuilder salted( int length )
    {
        this.saltLength = length;
        return this;
    }

    public DigestParametersBuilder iterations( int iterations )
    {
        this.iterations = iterations;
        return this;
    }

    public DigestParameters build()
    {
        if( algorithm == null )
        {
            throw new IllegalStateException( "Cannot build digest parameters without an algorithm" );
        }
        return new DigestParameters( algorithm, digester.generateSalt( saltLength ), iterations );
    }

}
