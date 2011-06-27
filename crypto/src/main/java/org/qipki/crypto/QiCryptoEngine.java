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
package org.qipki.crypto;

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
@Mixins( QiCryptoEngine.Mixin.class )
public interface QiCryptoEngine
        extends CryptoContext, Activatable, ServiceComposite
{

    @SuppressWarnings( "PublicInnerClass" )
    abstract class Mixin
            implements QiCryptoEngine
    {

        private static final Logger LOGGER = LoggerFactory.getLogger( QiCryptoEngine.Mixin.class );
        @This
        @Optional
        private QiCryptoConfiguration configuration;
        private String providerName;

        @Override
        public void activate()
                throws Exception
        {
            providerName = readProviderName();
            if ( readInsertProviderOnActivate() ) {
                if ( Security.getProvider( providerName ) == null ) {

                    Security.addProvider( readProviderClass().newInstance() );

                    if ( LOGGER.isDebugEnabled() ) {
                        LOGGER.debug( "Inserted {} with name: {}", readProviderClass(), readProviderName() );
                    }
                } else if ( LOGGER.isDebugEnabled() ) {
                    LOGGER.debug( "A Provider is already registered with the name {}. Doing nothing.", providerName );
                }
            }
            if ( readEnsureJCE() ) {
                // TODO
            }
        }

        @Override
        public void passivate()
                throws Exception
        {
            if ( readRemoveProviderOnPassivate() ) {
                if ( Security.getProvider( providerName ) == null ) {

                    Security.removeProvider( providerName );

                    if ( LOGGER.isDebugEnabled() ) {
                        LOGGER.debug( "Removed Provider named {}", readProviderName() );
                    }
                } else if ( LOGGER.isDebugEnabled() ) {
                    LOGGER.debug( "No Provider registered with the name {}. Doing nothing.", providerName );
                }
            }
        }

        @Override
        public String providerName()
        {
            return providerName;
        }

        private boolean readEnsureJCE()
        {
            if ( configuration == null ) {
                return true;
            }
            Boolean ensure = configuration.ensureJCE().get();
            if ( ensure == null ) {
                return true;
            }
            return ensure;
        }

        private boolean readInsertProviderOnActivate()
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

        private String readProviderName()
        {
            if ( readOverrideProvider() ) {
                return configuration.providerName().get();
            }
            return BouncyCastleProvider.PROVIDER_NAME;
        }

        private Class<? extends Provider> readProviderClass()
                throws ClassNotFoundException
        {
            if ( readOverrideProvider() ) {
                return ( Class<? extends Provider> ) Class.forName( configuration.providerClass().get() );
            }
            return BouncyCastleProvider.class;
        }

        private boolean readOverrideProvider()
        {
            if ( configuration == null || configuration.overrideProvider().get() == null || !configuration.overrideProvider().get() ) {
                return false;
            }
            return !StringUtils.isEmpty( configuration.providerName().get() )
                    && !StringUtils.isEmpty( configuration.providerClass().get() );
        }

        private boolean readRemoveProviderOnPassivate()
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
