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

import java.security.Provider;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.codeartisans.java.toolbox.StringUtils;

import org.qi4j.api.common.Optional;
import org.qi4j.api.injection.scope.This;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.Activatable;
import org.qi4j.api.service.ServiceComposite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mixins( QiCryptoActivator.Mixin.class )
public interface QiCryptoActivator
        extends Activatable, ServiceComposite
{

    @SuppressWarnings( "PublicInnerClass" )
    abstract class Mixin
            implements QiCryptoActivator
    {

        private static final Logger LOGGER = LoggerFactory.getLogger( QiCryptoActivator.Mixin.class );
        @This
        @Optional
        private QiCryptoConfiguration configuration;

        @Override
        public void activate()
                throws Exception
        {
            if ( insertProviderOnActivate() ) {
                String providerName = providerName();
                if ( Security.getProvider( providerName ) == null ) {

                    Security.addProvider( providerClass().newInstance() );

                    if ( LOGGER.isDebugEnabled() ) {
                        LOGGER.debug( "Inserted {} with name: {}", providerClass(), providerName() );
                    }
                } else if ( LOGGER.isDebugEnabled() ) {
                    LOGGER.debug( "A Provider is already registered with the name {}. Doing nothing.", providerName );
                }
            }
        }

        @Override
        public void passivate()
                throws Exception
        {
            if ( removeProviderOnPassivate() ) {
                String providerName = providerName();
                if ( Security.getProvider( providerName ) == null ) {

                    Security.removeProvider( providerName );

                    if ( LOGGER.isDebugEnabled() ) {
                        LOGGER.debug( "Removed Provider named {}", providerName() );
                    }
                } else if ( LOGGER.isDebugEnabled() ) {
                    LOGGER.debug( "No Provider registered with the name {}. Doing nothing.", providerName );
                }
            }
        }

        private boolean overrideProvider()
        {
            if ( configuration == null || configuration.overrideProvider().get() == null || !configuration.overrideProvider().get() ) {
                return false;
            }
            return !StringUtils.isEmpty( configuration.providerName().get() )
                    && !StringUtils.isEmpty( configuration.providerClass().get() );
        }

        private String providerName()
        {
            if ( overrideProvider() ) {
                return configuration.providerName().get();
            }
            return BouncyCastleProvider.PROVIDER_NAME;
        }

        private Class<? extends Provider> providerClass()
                throws ClassNotFoundException
        {
            if ( overrideProvider() ) {
                return ( Class<? extends Provider> ) Class.forName( configuration.providerClass().get() );
            }
            return BouncyCastleProvider.class;
        }

        private boolean insertProviderOnActivate()
        {
            if ( configuration == null ) {
                return true;
            }
            Boolean insert = configuration.insertProviderOnActivate().get();
            if ( insert == null ) {
                return true;
            }
            return insert;
        }

        private boolean removeProviderOnPassivate()
        {
            if ( configuration == null ) {
                return true;
            }
            Boolean remove = configuration.removeProviderOnPassivate().get();
            if ( remove == null ) {
                return true;
            }
            return remove;
        }

    }

}
