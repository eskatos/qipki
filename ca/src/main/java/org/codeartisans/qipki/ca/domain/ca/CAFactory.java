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
package org.codeartisans.qipki.ca.domain.ca;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Base64;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStoreEntity;
import org.codeartisans.qipki.commons.values.KeySpecValue;
import org.codeartisans.qipki.core.QiPkiFailure;
import org.codeartisans.qipki.core.crypto.CryptGEN;
import org.codeartisans.qipki.core.crypto.CryptIO;
import org.joda.time.Duration;
import org.qi4j.api.composite.TransientBuilderFactory;
import org.qi4j.api.entity.EntityBuilder;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

@Mixins( CAFactory.Mixin.class )
public interface CAFactory
        extends ServiceComposite
{

    CAEntity createRootCA( String name, String distinguishedName, KeySpecValue keySpec, CryptoStoreEntity keyStore );

    CAEntity createSubCA( CAEntity parentCA, String name, String distinguishedName, KeySpecValue keySpec, CryptoStoreEntity keyStore );

    abstract class Mixin
            implements CAFactory
    {

        @Structure
        private UnitOfWorkFactory uowf;
        @Structure
        private TransientBuilderFactory tbf;

        @Override
        public CAEntity createRootCA( String name, String distinguishedName, KeySpecValue keySpec, CryptoStoreEntity keyStore )
        {
            try {
                CryptGEN cryptgen = tbf.newTransient( CryptGEN.class );
                CryptIO cryptio = tbf.newTransient( CryptIO.class );
                KeyPair keyPair = cryptgen.generateRSAKeyPair( keySpec.length().get() );
                X500Principal dn = new X500Principal( distinguishedName );
                PKCS10CertificationRequest pkcs10 = cryptgen.generatePKCS10( dn, keyPair );
                X509Certificate cert = cryptgen.generateX509Certificate( keyPair.getPrivate(),
                                                                         dn,
                                                                         BigInteger.probablePrime( 120, new SecureRandom() ),
                                                                         pkcs10.getCertificationRequestInfo().getSubject(),
                                                                         pkcs10.getPublicKey(),
                                                                         Duration.standardDays( 3650 ),
                                                                         cryptio.extractRequestedExtensions( pkcs10 ) );

                EntityBuilder<CAEntity> caBuilder = uowf.currentUnitOfWork().newEntityBuilder( CAEntity.class );
                CAEntity ca = caBuilder.instance();

                KeyStore ks = keyStore.loadKeyStore();
                ks.setEntry( ca.identity().get(),
                             new KeyStore.PrivateKeyEntry( keyPair.getPrivate(), new Certificate[]{ cert } ),
                             new KeyStore.PasswordProtection( keyStore.password().get() ) );

                ByteArrayOutputStream boas = new ByteArrayOutputStream();
                ks.store( boas, keyStore.password().get() );
                boas.flush();
                keyStore.payload().set( new String( Base64.encode( boas.toByteArray() ), "UTF-8" ) );

                ca.name().set( name );
                ca.cryptoStore().set( keyStore );

                return caBuilder.newInstance();
            } catch ( GeneralSecurityException ex ) {
                throw new QiPkiFailure( "Unable to create self signed keypair plus certificate", ex );
            } catch ( IOException ex ) {
                throw new QiPkiFailure( "Unable to create self signed keypair plus certificate", ex );
            }
        }

        @Override
        public CAEntity createSubCA( CAEntity parentCA, String name, String distinguishedName, KeySpecValue keySpec, CryptoStoreEntity keyStore )
        {
            EntityBuilder<CAEntity> caBuilder = uowf.currentUnitOfWork().newEntityBuilder( CAEntity.class );
            CAEntity ca = caBuilder.instance();
            ca.name().set( name );
            return caBuilder.newInstance();
        }

    }

}
