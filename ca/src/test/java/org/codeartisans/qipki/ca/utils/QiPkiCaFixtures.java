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
package org.codeartisans.qipki.ca.utils;

import java.security.KeyStore;
import org.codeartisans.qipki.ca.domain.ca.CAEntity;
import org.codeartisans.qipki.ca.domain.ca.CAFactory;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStoreEntity;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStoreFactory;
import org.codeartisans.qipki.commons.constants.KeyStoreType;
import org.codeartisans.qipki.commons.values.KeySpecValue;
import org.codeartisans.qipki.commons.values.params.CryptoStoreFactoryParamsValue;
import org.codeartisans.qipki.commons.values.params.ParamsFactory;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.Activatable;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWork;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

@Mixins( QiPkiCaFixtures.Mixin.class )
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
        @Service
        private ParamsFactory paramsFactory;
        @Service
        private CAFactory caFactory;
        @Service
        private CryptoStoreFactory csFactory;

        @Override
        public void activate()
                throws Exception
        {
            UnitOfWork uow = uowf.newUnitOfWork();

            // Create a test keystore
            CryptoStoreFactoryParamsValue csParams = paramsFactory.createKeyStoreFactoryParams( KEYSTORE_NAME, KeyStoreType.JKS, "changeit".toCharArray() );
            CryptoStoreEntity cryptoStore = csFactory.create( csParams );
            // KeyStore keystore = cryptoStore.loadKeyStore(); // This call is here only to test CrytpoStoreBehavior
            
            // Create some test CAs
            KeySpecValue keySpec = paramsFactory.createKeySpec( "SHA256WITHRSA", 512 );
            CAEntity rootCa = caFactory.createRootCA( ROOT_CA_NAME, ROOT_CA_DN, keySpec, cryptoStore );
            CAEntity usersCa = caFactory.createRootCA( USERS_CA_NAME, USERS_CA_DN, keySpec, cryptoStore );
            CAEntity servicesCa = caFactory.createRootCA( SERVICES_CA_NAME, SERVICES_CA_DN, keySpec, cryptoStore );

            String rootId = rootCa.identity().get();

            uow.complete();

            

            uow = uowf.newUnitOfWork();

            cryptoStore = uow.get( cryptoStore );
            rootCa = uow.get( CAEntity.class, rootId );

            uow.complete();

        }

        @Override
        public void passivate()
                throws Exception
        {
        }

    }

}
