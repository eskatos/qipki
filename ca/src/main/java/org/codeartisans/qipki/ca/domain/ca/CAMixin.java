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
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.codeartisans.qipki.core.QiPkiFailure;
import org.codeartisans.qipki.core.crypto.CryptGEN;
import org.codeartisans.qipki.core.crypto.CryptIO;
import org.codeartisans.qipki.core.crypto.CryptoToolFactory;
import org.joda.time.Duration;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.This;
import org.qi4j.library.shiro.crypto.CryptoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CAMixin
        implements CABehavior
{

    private static final Logger LOGGER = LoggerFactory.getLogger( CAMixin.class );
    @Service
    private CryptoToolFactory cryptoToolFactory;
    @This
    private CAEntity state;

    @Override
    public X509Certificate certificate()
    {
        try {

            KeyStore ks = state.cryptoStore().get().loadKeyStore();
            return ( X509Certificate ) ks.getCertificate( state.identity().get() );

        } catch ( KeyStoreException ex ) {
            throw new QiPkiFailure( "Unable to load " + state.name().get() + " X509Certificate", ex );
        }
    }

    @Override
    public PrivateKey privateKey()
    {
        try {

            KeyStore ks = state.cryptoStore().get().loadKeyStore();
            return ( PrivateKey ) ks.getKey( state.identity().get(), state.cryptoStore().get().password().get() );

        } catch ( GeneralSecurityException ex ) {
            throw new QiPkiFailure( "Unable to load " + state.name().get() + " PrivateKey", ex );
        }
    }

    @Override
    public X509Certificate sign( PKCS10CertificationRequest pkcs10 )
    {
        LOGGER.debug( "Handling a PKCS#10 Certificate Signing Request" );
        try {

            CryptGEN cryptgen = cryptoToolFactory.newCryptGENInstance();
            CryptIO cryptio = cryptoToolFactory.newCryptIOInstance();

            X509Extensions requestedExtensions = cryptio.extractRequestedExtensions( pkcs10 );

            // TODO add CRL Distribution point !

            X509Certificate certificate = cryptgen.generateX509Certificate( privateKey(),
                                                                            certificate().getSubjectX500Principal(),
                                                                            BigInteger.probablePrime( 120, new SecureRandom() ),
                                                                            pkcs10.getCertificationRequestInfo().getSubject(),
                                                                            pkcs10.getPublicKey(),
                                                                            Duration.standardDays( 365 ),
                                                                            requestedExtensions );

            return certificate;

        } catch ( GeneralSecurityException ex ) {
            LOGGER.error( ex.getMessage(), ex );
            throw new CryptoException( "Unable to enroll PKCS#10", ex );
        } catch ( IllegalStateException ex ) {
            LOGGER.error( ex.getMessage(), ex );
            throw new CryptoException( "Unable to enroll PKCS#10", ex );
        }
    }

}
