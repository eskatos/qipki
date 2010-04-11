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

import org.codeartisans.qipki.ca.application.roles.KeyStoreFactory;
import org.codeartisans.qipki.ca.domain.ca.CA;
import org.codeartisans.qipki.ca.domain.ca.CAFactory;
import org.codeartisans.qipki.ca.domain.keystore.KeyStoreEntity;
import org.codeartisans.qipki.commons.constants.KeyStoreType;
import org.codeartisans.qipki.commons.values.params.KeyStoreFactoryParamsValue;
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

    String TEST_KEYSTORE_NAME = "Test KeyStore";
    String ROOT_CA_NAME = "CN=root-test,OU=qipki,O=codeartisans";
    String USERS_CA_NAME = "CN=users-test,OU=qipki,O=codeartisans";
    String SERVICES_CA_NAME = "CN=services-test,OU=qipki,O=codeartisans";

    abstract class Mixin
            implements QiPkiCaFixtures
    {

        @Structure
        private UnitOfWorkFactory uowf;
        @Service
        private CAFactory caFactory;
        @Service
        private ParamsFactory paramsFactory;
        @Service
        private KeyStoreFactory ksFactory;

        @Override
        public void activate()
                throws Exception
        {
            UnitOfWork uow = uowf.newUnitOfWork();
            // Create a test keystore
            KeyStoreFactoryParamsValue ksParams = paramsFactory.createKeyStoreFactoryParams( TEST_KEYSTORE_NAME,
                                                                                             KeyStoreType.JKS,
                                                                                             "changeit".toCharArray() );
            KeyStoreEntity ks = ksFactory.create( ksParams );

            // Create some CAs
            CA root = caFactory.create( ROOT_CA_NAME );
            CA users = caFactory.create( USERS_CA_NAME );
            CA services = caFactory.create( SERVICES_CA_NAME );

            uow.complete();
        }

        @Override
        public void passivate()
                throws Exception
        {
        }

    }

}
