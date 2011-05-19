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
package org.qipki.ca.domain.ca;

import org.qipki.ca.domain.ca.root.RootCA;
import org.qipki.ca.domain.ca.sub.SubCA;
import org.qipki.ca.domain.cryptostore.CryptoStore;
import org.qipki.commons.crypto.values.KeyPairSpecValue;
import org.qipki.crypto.x509.DistinguishedName;

import org.qi4j.api.sideeffect.SideEffectOf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CAFactorySideEffect
        extends SideEffectOf<CAFactory>
        implements CAFactory
{

    private static final Logger LOGGER = LoggerFactory.getLogger( CAFactorySideEffect.class );

    @Override
    public RootCA createRootCA( String name, Integer validityDays, DistinguishedName distinguishedName, KeyPairSpecValue keySpec, CryptoStore cryptoStore )
    {
        RootCA ca = result.createRootCA( name, validityDays, distinguishedName, keySpec, cryptoStore );
        LOGGER.debug( "New RootCA created: " + ca.name() );
        return null;
    }

    @Override
    public SubCA createSubCA( CA parentCA, String name, Integer validityDays, DistinguishedName distinguishedName, KeyPairSpecValue keySpec, CryptoStore cryptoStore )
    {
        SubCA ca = result.createSubCA( parentCA, name, validityDays, distinguishedName, keySpec, cryptoStore );
        LOGGER.debug( "New SubCA created: " + ca.name() );
        return null;
    }

}
