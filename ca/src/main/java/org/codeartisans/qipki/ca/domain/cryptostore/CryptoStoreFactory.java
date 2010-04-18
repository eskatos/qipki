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
package org.codeartisans.qipki.ca.domain.cryptostore;

import java.security.KeyStore;
import org.codeartisans.qipki.commons.values.params.CryptoStoreFactoryParamsValue;
import org.codeartisans.qipki.core.crypto.tools.CryptIO;
import org.codeartisans.qipki.core.crypto.tools.CryptoToolFactory;
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
public interface CryptoStoreFactory
        extends ServiceComposite
{

    CryptoStore create( CryptoStoreFactoryParamsValue params );

    abstract class Mixin
            implements CryptoStoreFactory
    {

        @Structure
        private UnitOfWorkFactory uowf;
        @Service
        private CryptoToolFactory cryptoToolFactory;

        @Override
        public CryptoStore create( CryptoStoreFactoryParamsValue params )
        {
            CryptIO cryptio = cryptoToolFactory.newCryptIOInstance();
            EntityBuilder<CryptoStore> ksBuilder = uowf.currentUnitOfWork().newEntityBuilder( CryptoStore.class );

            CryptoStore ksEntity = ksBuilder.instance();

            ksEntity.name().set( params.name().get() );
            ksEntity.storeType().set( params.storeType().get() );
            ksEntity.password().set( params.password().get() );

            KeyStore keystore = cryptio.createEmptyKeyStore( ksEntity.storeType().get() );
            ksEntity.payload().set( cryptio.base64Encode( keystore, ksEntity.password().get() ) );

            return ksBuilder.newInstance();
        }

    }

}
