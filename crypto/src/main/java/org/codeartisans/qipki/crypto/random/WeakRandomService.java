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

import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;

/**
 * Weak RandomService meant for testing purpose only.
 */
@Mixins( WeakRandomService.Mixin.class )
public interface WeakRandomService
        extends Random, ServiceComposite
{

    class Mixin
            implements Random
    {

        private final java.util.Random random;

        public Mixin()
        {
            random = new java.util.Random();
        }

        @Override
        public void nextBytes( byte[] bytes )
        {
            random.nextBytes( bytes );
        }

        @Override
        public int nextInt()
        {
            return random.nextInt();
        }

        @Override
        public int nextInt( int n )
        {
            return random.nextInt( n );
        }

        @Override
        public long nextLong()
        {
            return random.nextLong();
        }

        @Override
        public boolean nextBoolean()
        {
            return random.nextBoolean();
        }

        @Override
        public float nextFloat()
        {
            return random.nextFloat();
        }

        @Override
        public double nextDouble()
        {
            return random.nextDouble();
        }

        @Override
        public double nextGaussian()
        {
            return random.nextGaussian();
        }

    }

}
