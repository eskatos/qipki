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
package org.codeartisans.qipki.ca.presentation.rest;

import java.security.cert.X509Certificate;
import java.util.LinkedHashSet;
import java.util.Set;

import org.codeartisans.qipki.ca.domain.ca.CA;
import org.codeartisans.qipki.ca.domain.ca.profileassignment.X509ProfileAssignment;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStore;
import org.codeartisans.qipki.ca.domain.revocation.Revocation;
import org.codeartisans.qipki.ca.domain.x509.X509;
import org.codeartisans.qipki.ca.domain.x509profile.X509Profile;
import org.codeartisans.qipki.ca.presentation.rest.uribuilder.UriBuilder;
import org.codeartisans.qipki.commons.crypto.services.CryptoValuesFactory;
import org.codeartisans.qipki.commons.crypto.services.X509ExtensionsValueFactory;
import org.codeartisans.qipki.commons.rest.values.ApiURIsValue;
import org.codeartisans.qipki.commons.rest.values.representations.RestListValue;
import org.codeartisans.qipki.commons.rest.values.representations.RestValue;
import org.codeartisans.qipki.commons.rest.values.representations.CAValue;
import org.codeartisans.qipki.commons.rest.values.representations.CryptoStoreValue;
import org.codeartisans.qipki.commons.rest.values.representations.RevocationValue;
import org.codeartisans.qipki.commons.rest.values.representations.X509DetailValue;
import org.codeartisans.qipki.commons.rest.values.representations.X509ProfileAssignmentValue;
import org.codeartisans.qipki.commons.rest.values.representations.X509ProfileValue;
import org.codeartisans.qipki.commons.rest.values.representations.X509Value;
import org.codeartisans.qipki.crypto.objects.CryptObjectsFactory;
import org.codeartisans.qipki.crypto.objects.KeyInformation;
import org.codeartisans.qipki.crypto.x509.X509ExtensionsReader;

import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.value.ValueBuilder;
import org.qi4j.api.value.ValueBuilderFactory;

import org.restlet.data.Reference;

@Mixins( RestletValuesFactory.Mixin.class )
public interface RestletValuesFactory
        extends ServiceComposite
{

    ApiURIsValue apiURIs( Reference rootRef );

    CryptoStoreValue cryptoStore( Reference rootRef, CryptoStore ks );

    CAValue ca( Reference rootRef, CA ca );

    X509ProfileValue x509Profile( Reference rootRef, X509Profile x509Profile );

    X509Value x509( Reference rootRef, X509 x509 );

    X509DetailValue x509Detail( Reference rootRef, X509 x509 );

    RevocationValue revocation( Reference rootRef, Revocation revocation );

    Iterable<RestValue> asValues( Reference rootRef, Iterable<?> objects );

    RestListValue newListRepresentationValue( Reference listRef, int start, Iterable<RestValue> list );

    @SuppressWarnings( "PublicInnerClass" )
    abstract class Mixin
            implements RestletValuesFactory
    {

        @Structure
        private ValueBuilderFactory vbf;
        @Service
        private CryptoValuesFactory commonValuesFactory;
        @Service
        private X509ExtensionsValueFactory x509ExtensionsValueFactory;
        @Service
        private X509ExtensionsReader x509ExtReader;
        @Service
        private CryptObjectsFactory cryptoToolFactory;

        @Override
        public ApiURIsValue apiURIs( Reference rootRef )
        {
            ValueBuilder<ApiURIsValue> apiBuilder = vbf.newValueBuilder( ApiURIsValue.class );
            UriBuilder uriBuilder = new UriBuilder( rootRef );

            ApiURIsValue api = apiBuilder.prototype();

            api.uri().set( rootRef.toString() );
            api.cryptoStoreListUri().set( uriBuilder.cryptoStore().build() );
            api.caListUri().set( uriBuilder.ca().build() );
            api.x509ProfileListUri().set( uriBuilder.x509Profile().build() );
            api.x509ListUri().set( uriBuilder.x509().build() );

            return apiBuilder.newInstance();
        }

        @Override
        public CryptoStoreValue cryptoStore( Reference rootRef, CryptoStore cs )
        {
            ValueBuilder<CryptoStoreValue> ksValueBuilder = vbf.newValueBuilder( CryptoStoreValue.class );
            UriBuilder uriBuilder = new UriBuilder( rootRef );

            CryptoStoreValue ksValue = ksValueBuilder.prototype();

            ksValue.uri().set( uriBuilder.cryptoStore().withIdentity( cs.identity().get() ).build() );

            ksValue.name().set( cs.name().get() );
            ksValue.storeType().set( cs.storeType().get() );
            ksValue.password().set( cs.password().get() );

            return ksValueBuilder.newInstance();
        }

        @Override
        public CAValue ca( Reference rootRef, CA ca )
        {
            ValueBuilder<CAValue> caValueBuilder = vbf.newValueBuilder( CAValue.class );
            UriBuilder uriBuilder = new UriBuilder( rootRef );

            CAValue caValue = caValueBuilder.prototype();

            caValue.uri().set( uriBuilder.ca().withIdentity( ca.identity().get() ).build() );
            caValue.crlUri().set( uriBuilder.ca().withIdentity( ca.identity().get() ).crl().build() );
            caValue.cryptoStoreUri().set( uriBuilder.cryptoStore().withIdentity( ca.cryptoStore().get().identity().get() ).build() );

            for ( X509ProfileAssignment eachAllowedProfile : ca.allowedX509Profiles().toSet() ) {
                ValueBuilder<X509ProfileAssignmentValue> profileBuilder = vbf.newValueBuilder( X509ProfileAssignmentValue.class );

                X509ProfileAssignmentValue profile = profileBuilder.prototype();

                profile.keyEscrowPolicy().set( eachAllowedProfile.keyEscrowPolicy().get() );
                profile.x509ProfileUri().set( uriBuilder.x509Profile().withIdentity( eachAllowedProfile.x509Profile().get().identity().get() ).build() );

                caValue.allowedX509Profiles().get().add( profileBuilder.newInstance() );
            }

            caValue.name().set( ca.name().get() );

            return caValueBuilder.newInstance();
        }

        @Override
        public X509ProfileValue x509Profile( Reference rootRef, X509Profile x509Profile )
        {
            ValueBuilder<X509ProfileValue> x509ProfileValueBuilder = vbf.newValueBuilder( X509ProfileValue.class );
            UriBuilder uriBuilder = new UriBuilder( rootRef );

            X509ProfileValue x509ProfileValue = x509ProfileValueBuilder.prototype();
            x509ProfileValue.uri().set( uriBuilder.x509Profile().withIdentity( x509Profile.identity().get() ).build() );
            x509ProfileValue.name().set( x509Profile.name().get() );

            return x509ProfileValueBuilder.newInstance();
        }

        @Override
        public X509Value x509( Reference rootRef, X509 x509 )
        {
            ValueBuilder<X509Value> x509ValueBuilder = vbf.newValueBuilder( X509Value.class );
            UriBuilder uriBuilder = new UriBuilder( rootRef );

            X509Value x509Value = x509ValueBuilder.prototype();

            x509Value.uri().set( uriBuilder.x509().withIdentity( x509.identity().get() ).build() );
            x509Value.detailUri().set( uriBuilder.x509().withIdentity( x509.identity().get() ).detail().build() );
            x509Value.revocationUri().set( uriBuilder.x509().withIdentity( x509.identity().get() ).revocation().build() );
            x509Value.issuerUri().set( uriBuilder.ca().withIdentity( x509.issuer().get().identity().get() ).build() );
            x509Value.profileUri().set( uriBuilder.x509Profile().withIdentity( x509.profile().get().identity().get() ).build() );

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
        public X509DetailValue x509Detail( Reference rootRef, X509 x509 )
        {
            X509Certificate cert = x509.x509Certificate();
            KeyInformation publicKeyInfo = cryptoToolFactory.newKeyInformationInstance( cert.getPublicKey() );

            ValueBuilder<X509DetailValue> x509DetailValueBuilder = vbf.newValueBuilder( X509DetailValue.class );
            UriBuilder uriBuilder = new UriBuilder( rootRef );

            X509DetailValue x509DetailValue = x509DetailValueBuilder.prototype();

            x509DetailValue.uri().set( uriBuilder.x509().withIdentity( x509.identity().get() ).build() );
            x509DetailValue.detailUri().set( uriBuilder.x509().withIdentity( x509.identity().get() ).detail().build() );
            x509DetailValue.revocationUri().set( uriBuilder.x509().withIdentity( x509.identity().get() ).revocation().build() );
            x509DetailValue.issuerUri().set( uriBuilder.ca().withIdentity( x509.issuer().get().identity().get() ).build() );
            x509DetailValue.profileUri().set( uriBuilder.x509Profile().withIdentity( x509.profile().get().identity().get() ).build() );

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
        public RevocationValue revocation( Reference rootRef, Revocation revocation )
        {
            ValueBuilder<RevocationValue> revocationValueBuilder = vbf.newValueBuilder( RevocationValue.class );
            UriBuilder uriBuilder = new UriBuilder( rootRef );

            RevocationValue revocationValue = revocationValueBuilder.prototype();

            revocationValue.x509Uri().set( uriBuilder.x509().withIdentity( revocation.x509().get().identity().get() ).build() );
            revocationValue.uri().set( uriBuilder.x509().withIdentity( revocation.x509().get().identity().get() ).revocation().build() );

            revocationValue.reason().set( revocation.reason().get() );

            return revocationValueBuilder.newInstance();
        }

        @Override
        public Iterable<RestValue> asValues( Reference rootRef, Iterable<?> objects )
        {
            Set<RestValue> set = new LinkedHashSet<RestValue>();
            for ( Object eachObj : objects ) {
                if ( CA.class.isAssignableFrom( eachObj.getClass() ) ) {
                    set.add( ca( rootRef, ( CA ) eachObj ) );
                } else if ( CryptoStore.class.isAssignableFrom( eachObj.getClass() ) ) {
                    set.add( cryptoStore( rootRef, ( CryptoStore ) eachObj ) );
                } else if ( X509.class.isAssignableFrom( eachObj.getClass() ) ) {
                    set.add( x509( rootRef, ( X509 ) eachObj ) );
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
