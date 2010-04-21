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

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.codeartisans.qipki.ca.domain.ca.root.RootCAMixin;
import org.codeartisans.qipki.core.QiPkiFailure;
import org.codeartisans.qipki.core.crypto.x509.X509Generator;
import org.codeartisans.qipki.core.crypto.x509.X509ExtensionsReader;
import org.joda.time.Duration;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.This;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CAMixin
        implements CABehavior
{

    private static final Logger LOGGER = LoggerFactory.getLogger( RootCAMixin.class );
    @Service
    private X509Generator x509Generator;
    @Service
    private X509ExtensionsReader x509ExtReader;
    @This
    private CAState state;

    @Override
    public X509Certificate certificate()
    {
        try {
            return ( X509Certificate ) state.cryptoStore().get().loadKeyStore().getCertificate( state.identity().get() );
        } catch ( KeyStoreException ex ) {
            throw new QiPkiFailure( "Unable to load " + state.name().get() + " X509Certificate", ex );
        }
    }

    @Override
    public PrivateKey privateKey()
    {
        try {
            return ( PrivateKey ) state.cryptoStore().get().loadKeyStore().getKey( state.identity().get(), state.cryptoStore().get().password().get() );
        } catch ( GeneralSecurityException ex ) {
            throw new QiPkiFailure( "Unable to load " + state.name().get() + " PrivateKey", ex );
        }
    }

    @Override
    public X509Certificate sign( PKCS10CertificationRequest pkcs10 )
    {
        LOGGER.debug( "Handling a PKCS#10 Certificate Signing Request" );
        try {

            X509Extensions requestedExtensions = x509ExtReader.extractRequestedExtensions( pkcs10 );

            // TODO add Basic Constraints
            // TODO add CRL Distribution point !

            X509Certificate certificate = x509Generator.generateX509Certificate( privateKey(),
                                                                                 certificate().getSubjectX500Principal(),
                                                                                 BigInteger.probablePrime( 120, new SecureRandom() ),
                                                                                 pkcs10.getCertificationRequestInfo().getSubject(),
                                                                                 pkcs10.getPublicKey(),
                                                                                 Duration.standardDays( 365 ),
                                                                                 requestedExtensions );

            return certificate;

        } catch ( GeneralSecurityException ex ) {
            LOGGER.error( ex.getMessage(), ex );
            throw new QiPkiFailure( "Unable to enroll PKCS#10", ex );
        } catch ( IllegalStateException ex ) {
            LOGGER.error( ex.getMessage(), ex );
            throw new QiPkiFailure( "Unable to enroll PKCS#10", ex );
        }
    }

}
