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
package org.qipki.ca.tests;

import org.qipki.ca.application.contexts.RootContext;
import org.qipki.ca.application.contexts.ca.CAListContext;
import org.qipki.ca.domain.ca.CA;
import org.qipki.ca.domain.cryptostore.CryptoStore;
import org.qipki.commons.crypto.services.CryptoValuesFactory;
import org.qipki.commons.crypto.values.KeyPairSpecValue;
import org.qipki.core.dci.InteractionContext;
import org.qipki.crypto.algorithms.AsymetricAlgorithm;
import org.qipki.crypto.storage.KeyStoreType;

import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.qi4j.api.query.Query;
import org.qi4j.api.service.Activatable;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWork;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

@Mixins( QiPkiCaFixtures.Mixin.class )
@SuppressWarnings( "PublicInnerClass" )
public interface QiPkiCaFixtures
        extends Activatable, ServiceComposite
{

    String KEYSTORE_NAME = "Test KeyStore";
    String ROOT_CA_NAME = "RootCA";
    String ROOT_CA_DN = "CN=root-test,OU=qipki,O=codeartisans";
    String USERS_CA_NAME = "UsersCA";
    String USERS_CA_DN = "CN=users-test,OU=qipki,O=codeartisans";
    String SERVICES_CA_NAME = "ServicesCA";
    String SERVICES_CA_DN = "CN=services-test,OU=qipki,O=codeartisans";

    abstract class Mixin
            implements QiPkiCaFixtures
    {

        @Structure
        private UnitOfWorkFactory uowf;
        @Structure
        private ObjectBuilderFactory obf;
        @Service
        private CryptoValuesFactory cryptoValuesFactory;

        @Override
        public void activate()
                throws Exception
        {
            UnitOfWork uow = uowf.newUnitOfWork();
            RootContext rootCtx = newRootContext();

            // Do we need to create fixtures
            Query<CryptoStore> csList = rootCtx.cryptoStoreListContext().list( 0 );
            boolean createFixtures = true;
            for ( CryptoStore eachCS : csList ) {
                if ( KEYSTORE_NAME.equals( eachCS.name().get() ) ) {
                    createFixtures = false;
                    break;
                }
            }

            if ( createFixtures ) {

                // Create a test keystore
                CryptoStore cryptoStore = rootCtx.cryptoStoreListContext().createCryptoStore( KEYSTORE_NAME, KeyStoreType.JKS, "changeit".toCharArray() );

                // Create some test CAs
                CAListContext caListCtx = rootCtx.caListContext();
                KeyPairSpecValue keySpec = cryptoValuesFactory.createKeySpec( AsymetricAlgorithm.RSA, 512 );
                CA rootCa = caListCtx.createRootCA( cryptoStore.identity().get(), ROOT_CA_NAME, 1, ROOT_CA_DN, keySpec );
                CA usersCa = caListCtx.createRootCA( cryptoStore.identity().get(), USERS_CA_NAME, 1, USERS_CA_DN, keySpec );
                CA servicesCa = caListCtx.createRootCA( cryptoStore.identity().get(), SERVICES_CA_NAME, 1, SERVICES_CA_DN, keySpec );

                cryptoStore.getX509Certificate( rootCa.identity().get() ); // This call is here only to test CryptoStoreBehavior

                String cryptoStoreId = cryptoStore.identity().get();
                String rootId = rootCa.identity().get();

                uow.complete();


                uow = uowf.newUnitOfWork();
                rootCtx = newRootContext();

                cryptoStore = rootCtx.cryptoStoreContext( cryptoStoreId ).cryptoStore();
                rootCa = rootCtx.caContext( rootId ).ca();

            }

            uow.complete();

        }

        @Override
        public void passivate()
                throws Exception
        {
        }

        protected final RootContext newRootContext()
        {
            return obf.newObjectBuilder( RootContext.class ).use( new InteractionContext() ).newInstance();
        }

    }

}
