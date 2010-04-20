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
package org.codeartisans.qipki.ca.domain.x509;

import java.security.cert.X509Certificate;
import javax.security.auth.x500.X500Principal;
import org.codeartisans.qipki.ca.domain.ca.CA;
import org.codeartisans.qipki.commons.values.crypto.CryptoValuesFactory;
import org.codeartisans.qipki.core.crypto.tools.CryptIO;
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

    X509 create( X509Certificate cert, CA issuer );

    abstract class Mixin
            implements X509Factory
    {

        @Structure
        private UnitOfWorkFactory uowf;
        @Service
        private CryptoValuesFactory commonValuesFactory;
        @Service
        private CryptIO cryptIO;

        @Override
        public X509 create( X509Certificate cert, CA issuer )
        {
            EntityBuilder<X509> x509Builder = uowf.currentUnitOfWork().newEntityBuilder( X509.class );

            X509 x509 = x509Builder.instance();
            x509.pem().set( cryptIO.asPEM( cert ).toString() );
            x509.canonicalSubjectDN().set( cert.getSubjectX500Principal().getName( X500Principal.CANONICAL ) );
            x509.canonicalIssuerDN().set( cert.getIssuerX500Principal().getName( X500Principal.CANONICAL ) );
            x509.hexSerialNumber().set( cert.getSerialNumber().toString( 16 ) );
            x509.validityInterval().set( commonValuesFactory.buildValidityInterval( cert.getNotBefore(), cert.getNotAfter() ) );

            x509.issuer().set( issuer );

            return x509Builder.newInstance();
        }

    }

}
