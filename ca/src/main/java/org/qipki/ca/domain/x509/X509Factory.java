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
package org.qipki.ca.domain.x509;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import javax.security.auth.x500.X500Principal;

import org.qipki.ca.domain.ca.CA;
import org.qipki.ca.domain.x509profile.X509Profile;
import org.qipki.commons.crypto.services.CryptoValuesFactory;
import org.qipki.core.QiPkiFailure;
import org.qipki.crypto.algorithms.DigestAlgorithm;
import org.qipki.crypto.digest.DigestParameters;
import org.qipki.crypto.digest.DigestService;
import org.qipki.crypto.io.CryptIO;

import org.qi4j.api.entity.EntityBuilder;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

@Mixins( X509Factory.Mixin.class )
public interface X509Factory
        extends ServiceComposite
{

    X509 create( X509Certificate cert, CA issuer, X509Profile profile );

    @SuppressWarnings( "PublicInnerClass" )
    abstract class Mixin
            implements X509Factory
    {

        @Structure
        private UnitOfWorkFactory uowf;
        @Service
        private CryptoValuesFactory commonValuesFactory;
        @Service
        private CryptIO cryptIO;
        @Service
        private DigestService digester;

        @Override
        public X509 create( X509Certificate cert, CA issuer, X509Profile profile )
        {
            try {

                EntityBuilder<X509> x509Builder = uowf.currentUnitOfWork().newEntityBuilder( X509.class );

                X509 x509 = x509Builder.instance();
                x509.pem().set( cryptIO.asPEM( cert ).toString() );
                x509.canonicalSubjectDN().set( cert.getSubjectX500Principal().getName( X500Principal.CANONICAL ) );
                x509.canonicalIssuerDN().set( cert.getIssuerX500Principal().getName( X500Principal.CANONICAL ) );
                x509.hexSerialNumber().set( cert.getSerialNumber().toString( 16 ) );
                x509.validityInterval().set( commonValuesFactory.buildValidityInterval( cert.getNotBefore(), cert.getNotAfter() ) );
                x509.md5Fingerprint().set( digester.hexDigest( cert.getEncoded(), new DigestParameters( DigestAlgorithm.MD5 ) ) );
                x509.sha1Fingerprint().set( digester.hexDigest( cert.getEncoded(), new DigestParameters( DigestAlgorithm.SHA_1 ) ) );
                x509.sha256Fingerprint().set( digester.hexDigest( cert.getEncoded(), new DigestParameters( DigestAlgorithm.SHA_256 ) ) );

                x509.issuer().set( issuer );
                x509.profile().set( profile );

                return x509Builder.newInstance();

            } catch ( CertificateEncodingException ex ) {
                throw new QiPkiFailure( "Unable to calculate X509Certificate fingerprints", ex );
            }
        }

    }

}
