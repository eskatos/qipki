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
package org.codeartisans.qipki.ca.application.contexts.x509;

import java.io.StringReader;
import java.security.cert.X509Certificate;
import org.codeartisans.qipki.ca.domain.ca.CA;
import org.codeartisans.qipki.ca.domain.ca.CARepository;
import org.codeartisans.qipki.ca.domain.endentity.EndEntity;
import org.codeartisans.qipki.ca.domain.endentity.EndEntityFactory;
import org.codeartisans.qipki.ca.domain.x509.X509;
import org.codeartisans.qipki.ca.domain.x509.X509Factory;
import org.codeartisans.qipki.ca.domain.x509.X509Repository;
import org.codeartisans.qipki.ca.presentation.rest.resources.WrongParametersBuilder;
import org.codeartisans.qipki.commons.values.params.X509FactoryParamsValue;
import org.codeartisans.qipki.core.crypto.io.CryptIO;
import org.codeartisans.qipki.core.dci.Context;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.query.Query;
import org.qi4j.api.unitofwork.NoSuchEntityException;

public class X509ListContext
        extends Context
{

    @Service
    private CryptIO cryptIO;

    public Query<X509> list( int start )
    {
        return context.role( X509Repository.class ).findAllPaginated( start, 25 );
    }

    public X509 createX509( X509FactoryParamsValue params )
    {
        try {
            CA ca = context.role( CARepository.class ).findByIdentity( params.caIdentity().get() );
            X509Certificate cert = ca.sign( cryptIO.readPKCS10PEM( new StringReader( params.pemPkcs10().get() ) ) );
            X509 x509 = context.role( X509Factory.class ).create( cert, ca );
            EndEntity ee = context.role( EndEntityFactory.class ).create( x509 );
            return x509;

        } catch ( NoSuchEntityException ex ) {
            throw new WrongParametersBuilder().title( "Invalid CA identity: " + params.caIdentity().get() ).build( ex );
        }
    }

}
