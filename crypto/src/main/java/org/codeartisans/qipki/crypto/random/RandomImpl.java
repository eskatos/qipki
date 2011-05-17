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
package org.codeartisans.qipki.crypto.random;

import java.security.SecureRandom;

import org.codeartisans.java.toolbox.StringUtils;

import org.qi4j.api.common.Optional;
import org.qi4j.api.injection.scope.This;
import org.qi4j.api.service.Activatable;

public class RandomImpl
        implements Random, Activatable
{

    @This
    @Optional
    private RandomConfiguration configuration;
    private java.security.SecureRandom rand;

    @Override
    public void activate()
            throws Exception
    {
        rand = SecureRandom.getInstance( algorithm() );
        rand.setSeed( rand.generateSeed( seedSize() ) );
    }

    @Override
    public void passivate()
            throws Exception
    {
        rand = null;
    }

    private String algorithm()
    {
        if ( configuration != null ) {
            String algorithm = configuration.algorithm().get();
            if ( !StringUtils.isEmpty( algorithm ) ) {
                return algorithm;
            }
        }
        return "SHA1PRNG"; // TODO Add a config property to QiCryptoConfiguration with default algorithm, is this a good idea ?
    }

    private Integer seedSize()
    {
        if ( configuration != null ) {
            Integer seedSize = configuration.seedSize().get();
            if ( seedSize != null && seedSize > 0 ) {
                return seedSize;
            }
        }
        return 128;
    }

    @Override
    public void nextBytes( byte[] bytes )
    {
        rand.nextBytes( bytes );
    }

    @Override
    public int nextInt()
    {
        return rand.nextInt();
    }

    @Override
    public int nextInt( int n )
    {
        return rand.nextInt( n );
    }

    @Override
    public long nextLong()
    {
        return rand.nextLong();
    }

    @Override
    public boolean nextBoolean()
    {
        return rand.nextBoolean();
    }

    @Override
    public float nextFloat()
    {
        return rand.nextFloat();
    }

    @Override
    public double nextDouble()
    {
        return rand.nextDouble();
    }

    @Override
    public double nextGaussian()
    {
        return rand.nextGaussian();
    }

}
