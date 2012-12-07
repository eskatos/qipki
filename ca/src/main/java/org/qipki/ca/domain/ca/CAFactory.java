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
package org.qipki.ca.domain.ca;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.joda.time.Duration;
import org.qi4j.api.common.Optional;
import org.qi4j.api.entity.EntityBuilder;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;
import org.qipki.ca.domain.ca.root.RootCA;
import org.qipki.ca.domain.ca.sub.SubCA;
import org.qipki.ca.domain.crl.CRL;
import org.qipki.ca.domain.crl.CRLFactory;
import org.qipki.ca.domain.cryptostore.CryptoStore;
import org.qipki.commons.crypto.values.KeyPairSpecValue;
import org.qipki.core.QiPkiFailure;
import org.qipki.crypto.asymetric.AsymetricGenerator;
import org.qipki.crypto.asymetric.AsymetricGeneratorParameters;
import org.qipki.crypto.x509.DistinguishedName;
import org.qipki.crypto.x509.KeyUsage;
import org.qipki.crypto.x509.X509ExtensionHolder;
import org.qipki.crypto.x509.X509ExtensionsBuilder;
import org.qipki.crypto.x509.X509Generator;

@Mixins( CAFactory.Mixin.class )
public interface CAFactory
    extends ServiceComposite
{

    RootCA createRootCA( String name, Integer validityDays, DistinguishedName distinguishedName,
                         KeyPairSpecValue keySpec, CryptoStore cryptoStore,
                         @Optional String... crlDistPoints );

    SubCA createSubCA( CA parentCA,
                       String name, Integer validityDays, DistinguishedName distinguishedName,
                       KeyPairSpecValue keySpec, CryptoStore cryptoStore,
                       @Optional String... crlDistPoints );

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
        private CRLFactory crlFactory;

        @Override
        public RootCA createRootCA( String name, Integer validityDays, DistinguishedName distinguishedName,
                                    KeyPairSpecValue keySpec, CryptoStore cryptoStore,
                                    String... crlDistPoints )
        {
            try
            {
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
            }
            catch( GeneralSecurityException ex )
            {
                throw new QiPkiFailure( "Unable to create self signed keypair plus certificate", ex );
            }
        }

        @Override
        public SubCA createSubCA( CA parentCA,
                                  String name, Integer validityDays, DistinguishedName distinguishedName,
                                  KeyPairSpecValue keySpec, CryptoStore cryptoStore,
                                  String... crlDistPoints )
        {
            try
            {
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
            }
            catch( GeneralSecurityException ex )
            {
                throw new QiPkiFailure( "Unable to create self signed keypair plus certificate", ex );
            }
        }

        private void createCa( CA ca, String name, CryptoStore cryptoStore, KeyPair keyPair, X509Certificate cert )
            throws GeneralSecurityException
        {
            ca.name().set( name );
            ca.cryptoStore().set( cryptoStore );

            // Store in associated CryptoStore
            cryptoStore.storeCertifiedKeyPair( ca.identity().get(), keyPair.getPrivate(), cert );
            // Generate initial CRL
            {
                X509CRL x509CRL = x509Generator.generateX509CRL( cert, keyPair.getPrivate() );
                CRL crl = crlFactory.create( x509CRL );
                ca.crl().set( crl );
            }
        }

        private List<X509ExtensionHolder> generateCAExtensions( PublicKey subjectPubKey, PublicKey issuerPubKey )
        {
            List<X509ExtensionHolder> extensions = new ArrayList<X509ExtensionHolder>();
            SubjectKeyIdentifier subjectKeyID = x509ExtBuilder.buildSubjectKeyIdentifier( subjectPubKey );
            extensions.add( new X509ExtensionHolder( X509Extension.subjectKeyIdentifier, false, subjectKeyID ) );
            AuthorityKeyIdentifier authKeyID = x509ExtBuilder.buildAuthorityKeyIdentifier( issuerPubKey );
            extensions.add( new X509ExtensionHolder( X509Extension.authorityKeyIdentifier, false, authKeyID ) );
            BasicConstraints bc = x509ExtBuilder.buildCABasicConstraints( 0L );
            extensions.add( new X509ExtensionHolder( X509Extension.basicConstraints, true, bc ) );
            org.bouncycastle.asn1.x509.KeyUsage keyUsages = x509ExtBuilder.buildKeyUsages( EnumSet.of( KeyUsage.cRLSign, KeyUsage.keyCertSign ) );
            extensions.add( new X509ExtensionHolder( X509Extension.keyUsage, true, keyUsages ) );
            return extensions;
        }

    }

}
