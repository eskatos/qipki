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
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.x509.CRLNumber;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V2CRLGenerator;
import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;
import org.codeartisans.qipki.ca.domain.ca.root.RootCA;
import org.codeartisans.qipki.ca.domain.ca.sub.SubCA;
import org.codeartisans.qipki.ca.domain.crl.CRL;
import org.codeartisans.qipki.ca.domain.crl.CRLFactory;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStore;
import org.codeartisans.qipki.commons.crypto.values.KeyPairSpecValue;
import org.codeartisans.qipki.core.QiPkiFailure;
import org.codeartisans.qipki.crypto.constants.Time;
import org.codeartisans.qipki.crypto.algorithms.SignatureAlgorithm;
import org.codeartisans.qipki.crypto.asymetric.AsymetricGenerator;
import org.codeartisans.qipki.crypto.asymetric.AsymetricGeneratorParameters;
import org.codeartisans.qipki.crypto.x509.X509Generator;
import org.codeartisans.qipki.crypto.io.CryptIO;
import org.codeartisans.qipki.crypto.x509.X509ExtensionsReader;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.qi4j.api.entity.EntityBuilder;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.sideeffect.SideEffects;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

/**
 * TODO Handle CRL nextUpdate
 */
@Mixins( CAFactory.Mixin.class )
@SideEffects( CAFactorySideEffect.class )
public interface CAFactory
        extends ServiceComposite
{

    RootCA createRootCA( String name, String distinguishedName, KeyPairSpecValue keySpec, CryptoStore cryptoStore );

    SubCA createSubCA( CA parentCA, String name, String distinguishedName, KeyPairSpecValue keySpec, CryptoStore cryptoStore );

    abstract class Mixin
            implements CAFactory
    {

        @Structure
        private UnitOfWorkFactory uowf;
        @Service
        private X509Generator x509Generator;
        @Service
        private X509ExtensionsReader x509ExtReader;
        @Service
        private AsymetricGenerator asymGenerator;
        @Service
        private CryptIO cryptIO;
        @Service
        private CRLFactory crlFactory;

        @Override
        public RootCA createRootCA( String name, String distinguishedName, KeyPairSpecValue keySpec, CryptoStore cryptoStore )
        {
            try {
                // Self signed CA
                KeyPair keyPair = asymGenerator.generateKeyPair( new AsymetricGeneratorParameters( keySpec.algorithm().get(), keySpec.length().get() ) );
                X500Principal dn = new X500Principal( distinguishedName );
                PKCS10CertificationRequest pkcs10 = x509Generator.generatePKCS10( dn, keyPair );
                X509Certificate cert = x509Generator.generateX509Certificate( keyPair.getPrivate(),
                                                                              dn,
                                                                              BigInteger.probablePrime( 120, new SecureRandom() ),
                                                                              pkcs10.getCertificationRequestInfo().getSubject(),
                                                                              pkcs10.getPublicKey(),
                                                                              Duration.standardDays( 3650 ),
                                                                              x509ExtReader.extractRequestedExtensions( pkcs10 ) );

                EntityBuilder<RootCA> caBuilder = uowf.currentUnitOfWork().newEntityBuilder( RootCA.class );
                RootCA ca = caBuilder.instance();
                ca.name().set( name );
                ca.cryptoStore().set( cryptoStore );

                // Store in associated CryptoStore
                {
                    KeyStore ks = cryptoStore.loadKeyStore();
                    ks.setEntry( ca.identity().get(),
                                 new KeyStore.PrivateKeyEntry( keyPair.getPrivate(), new Certificate[]{ cert } ),
                                 new KeyStore.PasswordProtection( cryptoStore.password().get() ) );
                    cryptoStore.payload().set( cryptIO.base64Encode( ks, cryptoStore.password().get() ) );
                }

                // Generate initial CRL
                {
                    X509CRL x509CRL = createInitialCRL( cert, keyPair.getPrivate() );
                    CRL crl = crlFactory.create( cryptIO.asPEM( x509CRL ).toString() );
                    ca.crl().set( crl );
                }

                return caBuilder.newInstance();

            } catch ( GeneralSecurityException ex ) {
                throw new QiPkiFailure( "Unable to create self signed keypair plus certificate", ex );
            }
        }

        // TODO move CRL creation crypto code into a crypto service
        private X509CRL createInitialCRL( X509Certificate caCert, PrivateKey caPrivKey )
                throws GeneralSecurityException
        {
            X509V2CRLGenerator crlGen = new X509V2CRLGenerator();
            crlGen.setIssuerDN( caCert.getSubjectX500Principal() );
            crlGen.setThisUpdate( new DateTime().minus( Time.CLOCK_SKEW ).toDate() );
            crlGen.setNextUpdate( new DateTime().minus( Time.CLOCK_SKEW ).plusHours( 12 ).toDate() );
            crlGen.setSignatureAlgorithm( SignatureAlgorithm.SHA256withRSA.algoString() );
            crlGen.addExtension( X509Extensions.AuthorityKeyIdentifier, false, new AuthorityKeyIdentifierStructure( caCert ) );
            crlGen.addExtension( X509Extensions.CRLNumber, false, new CRLNumber( BigInteger.ONE ) );
            return crlGen.generate( caPrivKey, BouncyCastleProvider.PROVIDER_NAME );
        }

        // TODO implement createSubCA
        @Override
        public SubCA createSubCA( CA parentCA, String name, String distinguishedName, KeyPairSpecValue keySpec, CryptoStore cryptoStore )
        {
            EntityBuilder<SubCA> caBuilder = uowf.currentUnitOfWork().newEntityBuilder( SubCA.class );
            SubCA ca = caBuilder.instance();
            ca.name().set( name );
            return caBuilder.newInstance();
        }

    }

}
