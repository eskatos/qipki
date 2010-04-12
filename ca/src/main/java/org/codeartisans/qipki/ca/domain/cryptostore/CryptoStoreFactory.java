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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import org.bouncycastle.util.encoders.Base64;
import org.codeartisans.qipki.commons.values.params.CryptoStoreFactoryParamsValue;
import org.codeartisans.qipki.core.QiPkiFailure;
import org.qi4j.api.entity.EntityBuilder;
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

    CryptoStoreEntity create( CryptoStoreFactoryParamsValue params );

    abstract class Mixin
            implements CryptoStoreFactory
    {

        @Structure
        private UnitOfWorkFactory uowf;

        @Override
        public CryptoStoreEntity create( CryptoStoreFactoryParamsValue params )
        {
            EntityBuilder<CryptoStoreEntity> ksBuilder = uowf.currentUnitOfWork().newEntityBuilder( CryptoStoreEntity.class );
            CryptoStoreEntity ksEntity = ksBuilder.instance();

            ksEntity.name().set( params.name().get() );
            ksEntity.storeType().set( params.storeType().get() );
            ksEntity.password().set( params.password().get() );

            setEmptyKeyStore( ksEntity );

            return ksBuilder.newInstance();
        }

        private void setEmptyKeyStore( CryptoStoreEntity ksEntity )
        {
            try {

                KeyStore keystore = KeyStore.getInstance( ksEntity.storeType().get() );
                keystore.load( null, null );

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                keystore.store( baos, ksEntity.password().get() );

                baos.flush();

                ksEntity.payload().set( new String( Base64.encode( baos.toByteArray() ), "UTF-8" ) );

            } catch ( IOException ex ) {
                throw new QiPkiFailure( "Unable to create " + ksEntity.storeType().get() + " KeyStore", ex );
            } catch ( GeneralSecurityException ex ) {
                throw new QiPkiFailure( "Unable to create " + ksEntity.storeType().get() + " KeyStore", ex );
            }
        }

    }

}
