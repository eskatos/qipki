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
package org.codeartisans.qipki.ca.http.utils;

import java.security.KeyStore;

import org.codeartisans.qipki.ca.application.contexts.RootContext;
import org.codeartisans.qipki.ca.application.contexts.ca.CAListContext;
import org.codeartisans.qipki.ca.domain.ca.CA;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStore;
import org.codeartisans.qipki.commons.crypto.services.CryptoValuesFactory;
import org.codeartisans.qipki.commons.crypto.values.KeyPairSpecValue;
import org.codeartisans.qipki.core.dci.InteractionContext;
import org.codeartisans.qipki.crypto.algorithms.AsymetricAlgorithm;
import org.codeartisans.qipki.crypto.storage.KeyStoreType;

import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.object.ObjectBuilderFactory;
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

            // Create a test keystore
            CryptoStore cryptoStore = rootCtx.cryptoStoreListContext().createCryptoStore( KEYSTORE_NAME, KeyStoreType.JKS, "changeit".toCharArray() );
            KeyStore keystore = cryptoStore.loadKeyStore(); // This call is here only to test CryptoStoreBehavior

            // Create some test CAs
            CAListContext caListCtx = rootCtx.caListContext();
            KeyPairSpecValue keySpec = cryptoValuesFactory.createKeySpec( AsymetricAlgorithm.RSA, 512 );
            CA rootCa = caListCtx.createRootCA( cryptoStore.identity().get(), ROOT_CA_NAME, 1, ROOT_CA_DN, keySpec );
            CA usersCa = caListCtx.createRootCA( cryptoStore.identity().get(), USERS_CA_NAME, 1, USERS_CA_DN, keySpec );
            CA servicesCa = caListCtx.createRootCA( cryptoStore.identity().get(), SERVICES_CA_NAME, 1, SERVICES_CA_DN, keySpec );

            String cryptoStoreId = cryptoStore.identity().get();
            String rootId = rootCa.identity().get();

            uow.complete();


            uow = uowf.newUnitOfWork();
            rootCtx = newRootContext();

            cryptoStore = rootCtx.cryptoStoreContext( cryptoStoreId ).cryptoStore();
            rootCa = rootCtx.caContext( rootId ).ca();

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
