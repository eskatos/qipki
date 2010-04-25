/*
 * Copyright (c) 2010 Paul Merlin <paul@nosphere.org>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.codeartisans.qipki.ca.domain.ca;

import org.codeartisans.qipki.ca.domain.ca.root.RootCA;
import org.codeartisans.qipki.ca.domain.ca.sub.SubCA;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStore;
import org.codeartisans.qipki.commons.values.crypto.KeyPairSpecValue;
import org.qi4j.api.sideeffect.SideEffectOf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CAFactorySideEffect
        extends SideEffectOf<CAFactory>
        implements CAFactory
{

    private static final Logger LOGGER = LoggerFactory.getLogger( CAFactorySideEffect.class );

    @Override
    public RootCA createRootCA( String name, String distinguishedName, KeyPairSpecValue keySpec, CryptoStore cryptoStore )
    {
        RootCA ca = result.createRootCA( name, distinguishedName, keySpec, cryptoStore );
        LOGGER.debug( "New RootCA created: " + ca.name() );
        return null;
    }

    @Override
    public SubCA createSubCA( CA parentCA, String name, String distinguishedName, KeyPairSpecValue keySpec, CryptoStore cryptoStore )
    {
        SubCA ca = result.createSubCA( parentCA, name, distinguishedName, keySpec, cryptoStore );
        LOGGER.debug( "New SubCA created: " + ca.name() );
        return null;
    }

}
