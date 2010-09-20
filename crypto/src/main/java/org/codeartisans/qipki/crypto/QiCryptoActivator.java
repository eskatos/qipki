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
package org.codeartisans.qipki.crypto;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.Activatable;
import org.qi4j.api.service.ServiceComposite;

@Mixins( QiCryptoActivator.Mixin.class )
public interface QiCryptoActivator
        extends Activatable, ServiceComposite
{

    @SuppressWarnings( "PublicInnerClass" )
    abstract class Mixin
            implements QiCryptoActivator
    {

        @Override
        public void activate()
                throws Exception
        {
            if ( Security.getProvider( BouncyCastleProvider.PROVIDER_NAME ) == null ) {
                Security.addProvider( new BouncyCastleProvider() );
            }
        }

        @Override
        public void passivate()
                throws Exception
        {
            if ( Security.getProvider( BouncyCastleProvider.PROVIDER_NAME ) == null ) {
                Security.removeProvider( BouncyCastleProvider.PROVIDER_NAME );
            }
        }

    }

}
