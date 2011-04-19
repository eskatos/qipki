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

/**
 * Composite responsible for Provider registration according to its Configuration.
 */
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
