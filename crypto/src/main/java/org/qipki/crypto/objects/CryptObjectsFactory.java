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
package org.qipki.crypto.objects;

import java.security.PublicKey;

import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.qi4j.api.service.ServiceComposite;

@Mixins( CryptObjectsFactory.Mixin.class )
public interface CryptObjectsFactory
        extends ServiceComposite
{

    KeyInformation newKeyInformationInstance( PublicKey publicKey );

    @SuppressWarnings( "PublicInnerClass" )
    abstract class Mixin
            implements CryptObjectsFactory
    {

        @Structure
        private ObjectBuilderFactory obf;

        @Override
        public KeyInformation newKeyInformationInstance( PublicKey publicKey )
        {
            return obf.newObjectBuilder( KeyInformation.class ).use( publicKey ).newInstance();
        }

    }

}
