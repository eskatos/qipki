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
package org.qipki.ca.http.presentation.rest;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.qipki.ca.domain.ca.CA;
import org.qipki.ca.domain.ca.profileassignment.X509ProfileAssignment;
import org.qipki.ca.domain.cryptostore.CryptoStore;
import org.qipki.ca.domain.escrowedkeypair.EscrowedKeyPair;
import org.qipki.ca.domain.revocation.Revocation;
import org.qipki.ca.domain.x509.X509;
import org.qipki.ca.domain.x509profile.X509Profile;
import org.qipki.ca.http.presentation.rest.uribuilder.CaUriBuilder;
import org.qipki.commons.crypto.services.CryptoValuesFactory;
import org.qipki.commons.crypto.services.X509ExtensionsValueFactory;
import org.qipki.commons.rest.values.CaApiURIsValue;
import org.qipki.commons.rest.values.representations.EscrowedKeyPairValue;
import org.qipki.commons.rest.values.representations.RestListValue;
import org.qipki.commons.rest.values.representations.RestValue;
import org.qipki.commons.rest.values.representations.CAValue;
import org.qipki.commons.rest.values.representations.CryptoStoreValue;
import org.qipki.commons.rest.values.representations.RevocationValue;
import org.qipki.commons.rest.values.representations.X509DetailValue;
import org.qipki.commons.rest.values.representations.X509ProfileAssignmentValue;
import org.qipki.commons.rest.values.representations.X509ProfileValue;
import org.qipki.commons.rest.values.representations.X509Value;
import org.qipki.crypto.objects.CryptObjectsFactory;
import org.qipki.crypto.objects.KeyInformation;
import org.qipki.crypto.x509.X509ExtensionsReader;

import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.value.ValueBuilder;
import org.qi4j.api.value.ValueBuilderFactory;
import org.qipki.ca.http.presentation.rest.api.RestApiService;

import org.restlet.data.Reference;

@Mixins( RestletValuesFactory.Mixin.class )
public interface RestletValuesFactory
        extends ServiceComposite
{

    CaApiURIsValue caApiURIs();

    CryptoStoreValue cryptoStore( CryptoStore ks );

    CAValue ca( CA ca );

    X509ProfileValue x509Profile( X509Profile x509Profile );

    X509Value x509( X509 x509 );

    X509DetailValue x509Detail( X509 x509 );

    RevocationValue revocation( Revocation revocation );

    EscrowedKeyPairValue escrowedKeyPair( EscrowedKeyPair escrowedKeyPair );

    Iterable<RestValue> asValues( Iterable<?> objects );

    RestListValue newListRepresentationValue( Reference listRef, int start, Iterable<RestValue> list );

    @SuppressWarnings( "PublicInnerClass" )
    abstract class Mixin
            implements RestletValuesFactory
    {

        @Structure
        private ValueBuilderFactory vbf;
        @Service
        private RestApiService restApi;
        @Service
        private CryptoValuesFactory commonValuesFactory;
        @Service
        private X509ExtensionsValueFactory x509ExtensionsValueFactory;
        @Service
        private X509ExtensionsReader x509ExtReader;
        @Service
        private CryptObjectsFactory cryptoToolFactory;

        @Override
        public CaApiURIsValue caApiURIs()
        {
            Reference rootRef = restApi.apiRootRef();

            ValueBuilder<CaApiURIsValue> caApiBuilder = vbf.newValueBuilder( CaApiURIsValue.class );
            CaUriBuilder caUriBuilder = new CaUriBuilder( rootRef );

            CaApiURIsValue caApi = caApiBuilder.prototype();

            // API Root
            caApi.uri().set( rootRef.toString() );

            // Tools
            caApi.cryptoInspectorUri().set( caUriBuilder.tools().cryptoInspector().build() );

            // Domain
            caApi.cryptoStoreListUri().set( caUriBuilder.cryptoStore().build() );
            caApi.caListUri().set( caUriBuilder.ca().build() );
            caApi.x509ProfileListUri().set( caUriBuilder.x509Profile().build() );
            caApi.x509ListUri().set( caUriBuilder.x509().build() );
            caApi.escrowedKeyPairListUri().set( caUriBuilder.escrowedKeyPair().build() );

            return caApiBuilder.newInstance();
        }

        @Override
        public CryptoStoreValue cryptoStore( CryptoStore cs )
        {
            ValueBuilder<CryptoStoreValue> ksValueBuilder = vbf.newValueBuilder( CryptoStoreValue.class );
            CaUriBuilder caUriBuilder = new CaUriBuilder( restApi.apiRootRef() );

            CryptoStoreValue ksValue = ksValueBuilder.prototype();

            ksValue.uri().set( caUriBuilder.cryptoStore().withIdentity( cs.identity().get() ).build() );

            ksValue.name().set( cs.name().get() );
            ksValue.storeType().set( cs.storeType().get() );

            return ksValueBuilder.newInstance();
        }

        @Override
        public CAValue ca( CA ca )
        {
            ValueBuilder<CAValue> caValueBuilder = vbf.newValueBuilder( CAValue.class );
            CaUriBuilder caUriBuilder = new CaUriBuilder( restApi.apiRootRef() );

            CAValue caValue = caValueBuilder.prototype();

            caValue.uri().set( caUriBuilder.ca().withIdentity( ca.identity().get() ).build() );
            caValue.exportUri().set( caUriBuilder.ca().withIdentity( ca.identity().get() ).export().build() );
            caValue.crlUri().set( caUriBuilder.ca().withIdentity( ca.identity().get() ).crl().build() );
            caValue.cryptoStoreUri().set( caUriBuilder.cryptoStore().withIdentity( ca.cryptoStore().get().identity().get() ).build() );

            caValue.crlDistPoints().set( ca.crlDistPoints().get() );

            for ( X509ProfileAssignment eachAllowedProfile : ca.allowedX509Profiles().toSet() ) {
                ValueBuilder<X509ProfileAssignmentValue> profileBuilder = vbf.newValueBuilder( X509ProfileAssignmentValue.class );

                X509ProfileAssignmentValue profile = profileBuilder.prototype();

                profile.keyEscrowPolicy().set( eachAllowedProfile.keyEscrowPolicy().get() );
                profile.x509ProfileUri().set( caUriBuilder.x509Profile().withIdentity( eachAllowedProfile.x509Profile().get().identity().get() ).build() );

                caValue.allowedX509Profiles().get().add( profileBuilder.newInstance() );
            }

            caValue.name().set( ca.name().get() );

            return caValueBuilder.newInstance();
        }

        @Override
        public X509ProfileValue x509Profile( X509Profile x509Profile )
        {
            ValueBuilder<X509ProfileValue> x509ProfileValueBuilder = vbf.newValueBuilder( X509ProfileValue.class );
            CaUriBuilder caUriBuilder = new CaUriBuilder( restApi.apiRootRef() );

            X509ProfileValue x509ProfileValue = x509ProfileValueBuilder.prototype();

            x509ProfileValue.uri().set( caUriBuilder.x509Profile().withIdentity( x509Profile.identity().get() ).build() );
            x509ProfileValue.name().set( x509Profile.name().get() );
            x509ProfileValue.validityDays().set( x509Profile.validityDays().get() );

            return x509ProfileValueBuilder.newInstance();
        }

        @Override
        public X509Value x509( X509 x509 )
        {
            ValueBuilder<X509Value> x509ValueBuilder = vbf.newValueBuilder( X509Value.class );
            CaUriBuilder caUriBuilder = new CaUriBuilder( restApi.apiRootRef() );

            X509Value x509Value = x509ValueBuilder.prototype();

            x509Value.uri().set( caUriBuilder.x509().withIdentity( x509.identity().get() ).build() );
            x509Value.pemUri().set( caUriBuilder.x509().withIdentity( x509.identity().get() ).pem().build() );
            x509Value.detailUri().set( caUriBuilder.x509().withIdentity( x509.identity().get() ).detail().build() );
            x509Value.revocationUri().set( caUriBuilder.x509().withIdentity( x509.identity().get() ).revocation().build() );
            x509Value.issuerUri().set( caUriBuilder.ca().withIdentity( x509.issuer().get().identity().get() ).build() );
            x509Value.profileUri().set( caUriBuilder.x509Profile().withIdentity( x509.profile().get().identity().get() ).build() );
            x509Value.recoveryUri().set( caUriBuilder.x509().withIdentity( x509.identity().get() ).recovery().build() );

            x509Value.canonicalSubjectDN().set( x509.canonicalSubjectDN().get() );
            x509Value.canonicalIssuerDN().set( x509.canonicalIssuerDN().get() );
            x509Value.hexSerialNumber().set( x509.hexSerialNumber().get() );
            x509Value.validityInterval().set( commonValuesFactory.buildValidityInterval( x509.validityInterval().get().notBefore().get(),
                                                                                         x509.validityInterval().get().notAfter().get() ) );
            x509Value.md5Fingerprint().set( x509.md5Fingerprint().get() );
            x509Value.sha1Fingerprint().set( x509.sha1Fingerprint().get() );
            x509Value.sha256Fingerprint().set( x509.sha256Fingerprint().get() );

            return x509ValueBuilder.newInstance();
        }

        @Override
        public X509DetailValue x509Detail( X509 x509 )
        {
            X509Certificate cert = x509.x509Certificate();
            KeyInformation publicKeyInfo = cryptoToolFactory.newKeyInformationInstance( cert.getPublicKey() );

            ValueBuilder<X509DetailValue> x509DetailValueBuilder = vbf.newValueBuilder( X509DetailValue.class );
            CaUriBuilder caUriBuilder = new CaUriBuilder( restApi.apiRootRef() );

            X509DetailValue x509DetailValue = x509DetailValueBuilder.prototype();

            x509DetailValue.uri().set( caUriBuilder.x509().withIdentity( x509.identity().get() ).build() );
            x509DetailValue.pemUri().set( caUriBuilder.x509().withIdentity( x509.identity().get() ).pem().build() );
            x509DetailValue.detailUri().set( caUriBuilder.x509().withIdentity( x509.identity().get() ).detail().build() );
            x509DetailValue.revocationUri().set( caUriBuilder.x509().withIdentity( x509.identity().get() ).revocation().build() );
            x509DetailValue.issuerUri().set( caUriBuilder.ca().withIdentity( x509.issuer().get().identity().get() ).build() );
            x509DetailValue.profileUri().set( caUriBuilder.x509Profile().withIdentity( x509.profile().get().identity().get() ).build() );
            x509DetailValue.recoveryUri().set( caUriBuilder.x509().withIdentity( x509.identity().get() ).recovery().build() );

            x509DetailValue.canonicalSubjectDN().set( x509.canonicalSubjectDN().get() );
            x509DetailValue.canonicalIssuerDN().set( x509.canonicalIssuerDN().get() );
            x509DetailValue.hexSerialNumber().set( x509.hexSerialNumber().get() );
            x509DetailValue.validityInterval().set( commonValuesFactory.buildValidityInterval( x509.validityInterval().get().notBefore().get(),
                                                                                               x509.validityInterval().get().notAfter().get() ) );
            x509DetailValue.md5Fingerprint().set( x509.md5Fingerprint().get() );
            x509DetailValue.sha1Fingerprint().set( x509.sha1Fingerprint().get() );
            x509DetailValue.sha256Fingerprint().set( x509.sha256Fingerprint().get() );
            x509DetailValue.certificateVersion().set( cert.getVersion() );
            x509DetailValue.signatureAlgorithm().set( cert.getSigAlgName() );
            x509DetailValue.publicKeyAlgorithm().set( publicKeyInfo.getKeyAlgorithm() );
            x509DetailValue.publicKeySize().set( publicKeyInfo.getKeySize() );

            x509DetailValue.netscapeCertComment().set( x509ExtReader.getNetscapeCertComment( cert ) );

            // Key informations
            x509DetailValue.keysExtensions().set( x509ExtensionsValueFactory.buildKeysExtensionsValue( cert ) );

            // Policies
            x509DetailValue.policiesExtensions().set( x509ExtensionsValueFactory.buildPoliciesExtensionsValue( cert ) );

            // Alternative Names
            x509DetailValue.namesExtensions().set( x509ExtensionsValueFactory.buildNamesExtensionsValue( cert ) );

            // Constraints
            x509DetailValue.constraintsExtensions().set( x509ExtensionsValueFactory.buildConstraintsExtensionsValue( cert ) );

            return x509DetailValueBuilder.newInstance();
        }

        @Override
        public RevocationValue revocation( Revocation revocation )
        {
            ValueBuilder<RevocationValue> revocationValueBuilder = vbf.newValueBuilder( RevocationValue.class );
            CaUriBuilder caUriBuilder = new CaUriBuilder( restApi.apiRootRef() );

            RevocationValue revocationValue = revocationValueBuilder.prototype();

            revocationValue.x509Uri().set( caUriBuilder.x509().withIdentity( revocation.x509().get().identity().get() ).build() );
            revocationValue.uri().set( caUriBuilder.x509().withIdentity( revocation.x509().get().identity().get() ).revocation().build() );

            revocationValue.reason().set( revocation.reason().get() );

            return revocationValueBuilder.newInstance();
        }

        @Override
        public EscrowedKeyPairValue escrowedKeyPair( EscrowedKeyPair escrowedKeyPair )
        {
            ValueBuilder<EscrowedKeyPairValue> escrowedKeyPairValueBuilder = vbf.newValueBuilder( EscrowedKeyPairValue.class );
            CaUriBuilder caUriBuilder = new CaUriBuilder( restApi.apiRootRef() );

            EscrowedKeyPairValue escrowedKeyPairValue = escrowedKeyPairValueBuilder.prototype();

            escrowedKeyPairValue.uri().set( caUriBuilder.escrowedKeyPair().withIdentity( escrowedKeyPair.identity().get() ).build() );
            escrowedKeyPairValue.recoveryUri().set( caUriBuilder.escrowedKeyPair().withIdentity( escrowedKeyPair.identity().get() ).pem().build() );
            List<String> x509sUris = new ArrayList<String>();
            for ( X509 eachX509 : escrowedKeyPair.x509s() ) {
                x509sUris.add( caUriBuilder.x509().withIdentity( eachX509.identity().get() ).build() );
            }
            escrowedKeyPairValue.x509sUris().set( x509sUris );

            escrowedKeyPairValue.algorithm().set( escrowedKeyPair.algorithm().get() );
            escrowedKeyPairValue.length().set( escrowedKeyPair.length().get() );

            return escrowedKeyPairValueBuilder.newInstance();
        }

        @Override
        public Iterable<RestValue> asValues( Iterable<?> objects )
        {
            Reference rootRef = restApi.apiRootRef();
            Set<RestValue> set = new LinkedHashSet<RestValue>();
            for ( Object eachObj : objects ) {
                if ( CA.class.isAssignableFrom( eachObj.getClass() ) ) {
                    set.add( ca( ( CA ) eachObj ) );
                } else if ( CryptoStore.class.isAssignableFrom( eachObj.getClass() ) ) {
                    set.add( cryptoStore( ( CryptoStore ) eachObj ) );
                } else if ( X509.class.isAssignableFrom( eachObj.getClass() ) ) {
                    set.add( x509( ( X509 ) eachObj ) );
                } else if ( EscrowedKeyPair.class.isAssignableFrom( eachObj.getClass() ) ) {
                    set.add( escrowedKeyPair( ( EscrowedKeyPair ) eachObj ) );
                } else {
                    throw new IllegalArgumentException( "Object is of unsupported Type: " + eachObj );
                }
            }
            return set;
        }

        @Override
        public RestListValue newListRepresentationValue( Reference listRef, int start, Iterable<RestValue> items )
        {
            ValueBuilder<RestListValue> builder = vbf.newValueBuilder( RestListValue.class );
            RestListValue listRepresentation = builder.prototype();
            listRepresentation.uri().set( listRef.toString() );
            listRepresentation.start().set( start );
            for ( RestValue eachItem : items ) {
                listRepresentation.items().get().add( eachItem );
            }
            return builder.newInstance();
        }

    }

}
