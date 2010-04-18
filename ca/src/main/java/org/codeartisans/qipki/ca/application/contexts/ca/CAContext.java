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
package org.codeartisans.qipki.ca.application.contexts.ca;

import java.security.cert.X509Certificate;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.codeartisans.qipki.ca.domain.ca.CA;
import org.codeartisans.qipki.ca.domain.endentity.EndEntity;
import org.codeartisans.qipki.ca.domain.endentity.EndEntityFactory;
import org.codeartisans.qipki.ca.domain.fragments.PKCS10Signer;
import org.codeartisans.qipki.ca.domain.x509.X509;
import org.codeartisans.qipki.ca.domain.x509.X509Factory;
import org.codeartisans.qipki.core.dci.Context;

public class CAContext
        extends Context
{

    public CA ca()
    {
        return context.role( CA.class );
    }

    public X509Certificate sign( PKCS10CertificationRequest pkcs10 )
    {
        X509Certificate cert = context.role( PKCS10Signer.class ).sign( pkcs10 );
        X509 x509 = context.role( X509Factory.class ).create( cert, context.role( CA.class ) );
        EndEntity ee = context.role( EndEntityFactory.class ).create( x509 );
        return cert;
    }

}
