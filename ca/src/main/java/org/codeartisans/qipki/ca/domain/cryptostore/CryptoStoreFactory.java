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
package org.codeartisans.qipki.ca.domain.cryptostore;

import java.security.KeyStore;

import org.codeartisans.qipki.crypto.io.CryptIO;
import org.codeartisans.qipki.crypto.storage.KeyStoreType;

import org.qi4j.api.entity.EntityBuilder;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

/**
 * TODO Handle PKCS11 KeyStores
 */
@Mixins( CryptoStoreFactory.Mixin.class )
@SuppressWarnings( "PublicInnerClass" )
public interface CryptoStoreFactory
        extends ServiceComposite
{

    CryptoStore create( String name, KeyStoreType storeType, char[] password );

    abstract class Mixin
            implements CryptoStoreFactory
    {

        @Structure
        private UnitOfWorkFactory uowf;
        @Service
        private CryptIO cryptIO;

        @Override
        public CryptoStore create( String name, KeyStoreType storeType, char[] password )
        {
            EntityBuilder<CryptoStore> ksBuilder = uowf.currentUnitOfWork().newEntityBuilder( CryptoStore.class );

            CryptoStore ksEntity = ksBuilder.instance();

            ksEntity.name().set( name );
            ksEntity.storeType().set( storeType );
            ksEntity.password().set( password );

            KeyStore keystore = cryptIO.createEmptyKeyStore( ksEntity.storeType().get() );
            ksEntity.payload().set( cryptIO.base64Encode( keystore, ksEntity.password().get() ) );

            return ksBuilder.newInstance();
        }

    }

}
