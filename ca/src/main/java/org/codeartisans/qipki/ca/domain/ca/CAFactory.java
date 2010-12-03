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
package org.codeartisans.qipki.ca.domain.ca;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLNumber;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
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
import org.codeartisans.qipki.crypto.x509.DistinguishedName;
import org.codeartisans.qipki.crypto.x509.KeyUsage;
import org.codeartisans.qipki.crypto.x509.X509ExtensionHolder;
import org.codeartisans.qipki.crypto.x509.X509ExtensionsBuilder;

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

    RootCA createRootCA( String name, Integer validityDays, DistinguishedName distinguishedName, KeyPairSpecValue keySpec, CryptoStore cryptoStore );

    SubCA createSubCA( CA parentCA, String name, Integer validityDays, DistinguishedName distinguishedName, KeyPairSpecValue keySpec, CryptoStore cryptoStore );

    @SuppressWarnings( "PublicInnerClass" )
    abstract class Mixin
            implements CAFactory
    {

        @Structure
        private UnitOfWorkFactory uowf;
        @Service
        private X509Generator x509Generator;
        @Service
        private X509ExtensionsBuilder x509ExtBuilder;
        @Service
        private AsymetricGenerator asymGenerator;
        @Service
        private CryptIO cryptIO;
        @Service
        private CRLFactory crlFactory;

        @Override
        public RootCA createRootCA( String name, Integer validityDays, DistinguishedName distinguishedName, KeyPairSpecValue keySpec, CryptoStore cryptoStore )
        {
            try {
                // Self signed CA
                KeyPair keyPair = asymGenerator.generateKeyPair( new AsymetricGeneratorParameters( keySpec.algorithm().get(), keySpec.length().get() ) );
                PKCS10CertificationRequest pkcs10 = x509Generator.generatePKCS10( distinguishedName, keyPair );
                List<X509ExtensionHolder> extensions = generateCAExtensions( pkcs10.getPublicKey(), pkcs10.getPublicKey() );
                X509Certificate cert = x509Generator.generateX509Certificate( keyPair.getPrivate(),
                                                                              distinguishedName,
                                                                              BigInteger.probablePrime( 120, new SecureRandom() ),
                                                                              distinguishedName,
                                                                              pkcs10.getPublicKey(),
                                                                              Duration.standardDays( validityDays ),
                                                                              extensions );

                EntityBuilder<RootCA> caBuilder = uowf.currentUnitOfWork().newEntityBuilder( RootCA.class );
                RootCA ca = caBuilder.instance();

                createCa( ca, name, cryptoStore, keyPair, cert );

                return caBuilder.newInstance();

            } catch ( GeneralSecurityException ex ) {
                throw new QiPkiFailure( "Unable to create self signed keypair plus certificate", ex );
            }
        }

        @Override
        public SubCA createSubCA( CA parentCA, String name, Integer validityDays, DistinguishedName distinguishedName, KeyPairSpecValue keySpec, CryptoStore cryptoStore )
        {
            try {
                // Sub CA
                KeyPair keyPair = asymGenerator.generateKeyPair( new AsymetricGeneratorParameters( keySpec.algorithm().get(), keySpec.length().get() ) );
                PKCS10CertificationRequest pkcs10 = x509Generator.generatePKCS10( distinguishedName, keyPair );
                List<X509ExtensionHolder> extensions = generateCAExtensions( pkcs10.getPublicKey(), parentCA.certificate().getPublicKey() );
                X509Certificate cert = x509Generator.generateX509Certificate( parentCA.privateKey(),
                                                                              parentCA.distinguishedName(),
                                                                              BigInteger.probablePrime( 120, new SecureRandom() ),
                                                                              distinguishedName,
                                                                              pkcs10.getPublicKey(),
                                                                              Duration.standardDays( validityDays ),
                                                                              extensions );

                EntityBuilder<SubCA> caBuilder = uowf.currentUnitOfWork().newEntityBuilder( SubCA.class );
                SubCA ca = caBuilder.instance();

                createCa( ca, name, cryptoStore, keyPair, cert );
                ca.issuer().set( parentCA );

                return caBuilder.newInstance();
            } catch ( GeneralSecurityException ex ) {
                throw new QiPkiFailure( "Unable to create self signed keypair plus certificate", ex );
            }
        }

        private void createCa( CA ca, String name, CryptoStore cryptoStore, KeyPair keyPair, X509Certificate cert )
                throws GeneralSecurityException
        {
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
        }

        private List<X509ExtensionHolder> generateCAExtensions( PublicKey subjectPubKey, PublicKey issuerPubKey )
        {
            List<X509ExtensionHolder> extensions = new ArrayList<X509ExtensionHolder>();
            SubjectKeyIdentifier subjectKeyID = x509ExtBuilder.buildSubjectKeyIdentifier( subjectPubKey );
            extensions.add( new X509ExtensionHolder( X509Extensions.SubjectKeyIdentifier, false, subjectKeyID ) );
            AuthorityKeyIdentifier authKeyID = x509ExtBuilder.buildAuthorityKeyIdentifier( issuerPubKey );
            extensions.add( new X509ExtensionHolder( X509Extensions.AuthorityKeyIdentifier, false, authKeyID ) );
            BasicConstraints bc = x509ExtBuilder.buildCABasicConstraints( 0L );
            extensions.add( new X509ExtensionHolder( X509Extensions.BasicConstraints, true, bc ) );
            org.bouncycastle.asn1.x509.KeyUsage keyUsages = x509ExtBuilder.buildKeyUsages( EnumSet.of( KeyUsage.cRLSign, KeyUsage.keyCertSign ) );
            extensions.add( new X509ExtensionHolder( X509Extensions.KeyUsage, true, keyUsages ) );

            return extensions;
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

    }

}
