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
package org.qipki.ca.domain.cryptostore;

import java.io.File;
import java.security.KeyStore;

import org.qi4j.api.entity.EntityBuilder;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

import org.qipki.crypto.io.CryptIO;
import org.qipki.crypto.storage.KeyStoreType;

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
        private CryptoStoreFileService fileService;
        @Service
        private CryptIO cryptIO;

        @Override
        public CryptoStore create( String name, KeyStoreType storeType, char[] password )
        {
            EntityBuilder<CryptoStore> csBuilder = uowf.currentUnitOfWork().newEntityBuilder( CryptoStore.class );

            CryptoStore cryptoStore = csBuilder.instance();

            cryptoStore.name().set( name );
            cryptoStore.storeType().set( storeType );
            cryptoStore.password().set( password );

            cryptoStore = csBuilder.newInstance();

            // Create and save a new empty crypto store
            KeyStore keystore = cryptIO.createEmptyKeyStore( cryptoStore.storeType().get() );
            File keystoreFile = fileService.getKeyStoreFile( cryptoStore );
            cryptIO.writeKeyStore( keystore, password, keystoreFile );

            return cryptoStore;
        }

    }

}
