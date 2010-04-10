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
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.codeartisans.qipki.core.crypto.CryptGEN;
import org.codeartisans.qipki.core.crypto.CryptIO;
import org.joda.time.Duration;
import org.qi4j.api.composite.TransientBuilderFactory;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.injection.scope.This;
import org.qi4j.library.shiro.crypto.CryptoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CAMixin
        implements CABehavior
{

    private static final Logger LOGGER = LoggerFactory.getLogger( CAMixin.class );
    @This
    private CAState state;
    @Structure
    private TransientBuilderFactory tbf;

    @Override
    public X509Certificate certificate()
    {
        return state.x509().get().x509Certificate();
    }

    @Override
    public KeyPair keyPair()
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public X509Certificate sign( PKCS10CertificationRequest pkcs10 )
    {
        CryptGEN cryptgen = tbf.newTransient( CryptGEN.class );
        CryptIO cryptio = tbf.newTransient( CryptIO.class );
        try {
            LOGGER.debug( "Handling a PKCS10 Certificate Signing Request" );
            KeyPair keyPair = cryptgen.generateRSAKeyPair( 2048 );
            X509Extensions requestedExtensions = cryptio.extractRequestedExtensions( pkcs10 );
            return cryptgen.generateX509Certificate( keyPair.getPrivate(),
                                                     new X500Principal( "CN=" + state.identity().get() ),
                                                     BigInteger.probablePrime( 120, new SecureRandom() ),
                                                     pkcs10.getCertificationRequestInfo().getSubject(),
                                                     pkcs10.getPublicKey(),
                                                     Duration.ZERO,
                                                     requestedExtensions );

        } catch ( GeneralSecurityException ex ) {
            LOGGER.error( ex.getMessage(), ex );
            throw new CryptoException( "Unable to enroll pkcs10", ex );
        } catch ( IllegalStateException ex ) {
            LOGGER.error( ex.getMessage(), ex );
            throw new CryptoException( "Unable to enroll pkcs10", ex );
        }
    }

}
