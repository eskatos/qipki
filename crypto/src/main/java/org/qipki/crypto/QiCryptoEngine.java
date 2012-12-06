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
import java.security.SecureRandom;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.codeartisans.java.toolbox.Strings;
import org.qi4j.api.common.Optional;
import org.qi4j.api.configuration.Configuration;
import org.qi4j.api.injection.scope.This;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceActivation;
import org.qi4j.api.service.ServiceComposite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Composite responsible for Provider registration according to its Configuration.
 */
@Mixins( QiCryptoEngine.Mixin.class )
public interface QiCryptoEngine
        extends CryptoContext, ServiceComposite, ServiceActivation
{

    abstract class Mixin
            implements QiCryptoEngine
    {

        private static final Logger LOGGER = LoggerFactory.getLogger( QiCryptoEngine.Mixin.class );
        @This
        @Optional
        private Configuration<QiCryptoConfiguration> configuration;
        private Boolean ensureJce;
        private Boolean insertProviderOnActivate;
        private Boolean removeProviderOnPassivate;
        private String providerName;
        private Class<? extends Provider> providerClass;
        private String randomAlgorithm;
        private Integer randomSeedSize;
        private SecureRandom secureRandom;

        @Override
        public void activateService()
                throws Exception
        {
            loadConfiguration();
            if ( insertProviderOnActivate ) {
                if ( Security.getProvider( providerName ) == null ) {

                    Security.addProvider( providerClass.newInstance() );

                    if ( LOGGER.isDebugEnabled() ) {
                        LOGGER.debug( "Inserted {} with name: {}", providerClass, providerName );
                    }
                } else if ( LOGGER.isDebugEnabled() ) {
                    LOGGER.debug( "A Provider is already registered with the name {}. Doing nothing.", providerName );
                }
            }
            secureRandom = SecureRandom.getInstance( randomAlgorithm );
            secureRandom.setSeed( secureRandom.generateSeed( randomSeedSize ) );
            if ( ensureJce ) {
                // TODO
            }
        }

        @Override
        public void passivateService()
                throws Exception
        {
            if ( removeProviderOnPassivate ) {
                if ( Security.getProvider( providerName ) == null ) {

                    Security.removeProvider( providerName );

                    if ( LOGGER.isDebugEnabled() ) {
                        LOGGER.debug( "Removed Provider named {}", providerName );
                    }
                } else if ( LOGGER.isDebugEnabled() ) {
                    LOGGER.debug( "No Provider registered with the name {}. Doing nothing.", providerName );
                }
            }
            ensureJce = null;
            insertProviderOnActivate = null;
            removeProviderOnPassivate = null;
            providerName = null;
            providerClass = null;
            randomAlgorithm = null;
            randomSeedSize = null;
            secureRandom = null;
        }

        private void loadConfiguration()
                throws ClassNotFoundException
        {
            configuration.refresh();
            QiCryptoConfiguration config = configuration.get();

            ensureJce = config.ensureJCE().get();
            if ( ensureJce == null ) {
                ensureJce = Boolean.TRUE;
            }

            insertProviderOnActivate = config.insertProviderOnActivate().get();
            if ( insertProviderOnActivate == null ) {
                insertProviderOnActivate = Boolean.TRUE;
            }
            removeProviderOnPassivate = config.removeProviderOnPassivate().get();
            if ( removeProviderOnPassivate == null ) {
                removeProviderOnPassivate = Boolean.TRUE;
            }

            providerName = config.providerName().get();
            String providerClassName = config.providerClass().get();
            if ( Strings.isEmpty( providerName ) || Strings.isEmpty( providerClassName ) ) {
                providerName = BouncyCastleProvider.PROVIDER_NAME;
                providerClass = BouncyCastleProvider.class;
            } else {
                providerClass = ( Class<? extends Provider> ) Class.forName( providerClassName );
            }

            randomAlgorithm = config.randomAlgorithm().get();
            if ( Strings.isEmpty( randomAlgorithm ) ) {
                randomAlgorithm = "SHA1PRNG";
            }

            randomSeedSize = config.randomSeedSize().get();
            if ( randomSeedSize == null ) {
                randomSeedSize = 128;
            }
        }

        @Override
        public String providerName()
        {
            return providerName;
        }

        @Override
        public SecureRandom random()
        {
            return secureRandom;
        }

    }

}
