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

/**
 * Represent a JCA "transformation".
 *
 * @see http://download.oracle.com/javase/6/docs/technotes/guides/security/crypto/CryptoSpec.html
 */
public final class Transformation
{

    private final Algorithm[] algorithms;

    public Transformation( Algorithm algo )
    {
        this.algorithms = new Algorithm[]
        {
            algo
        };
    }

    public Transformation( Algorithm algo0, Algorithm algo1 )
    {
        this.algorithms = new Algorithm[]
        {
            algo0, algo1
        };
    }

    public Transformation( Algorithm algo0, Algorithm algo1, Algorithm algo2 )
    {
        this.algorithms = new Algorithm[]
        {
            algo0, algo1, algo2
        };
    }

    public String jcaTransformation()
    {
        StringBuilder sb = new StringBuilder();
        for( int i = 0; i < algorithms.length; i++ )
        {
            Algorithm algo = algorithms[i];
            sb.append( algo.jcaString() );
            if( i < algorithms.length - 1 )
            {
                sb.append( "/" );
            }
        }
        return sb.toString();
    }

    @Override
    public String toString()
    {
        return jcaTransformation();
    }

}
