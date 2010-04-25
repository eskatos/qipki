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

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.LinkedHashSet;
import java.util.Set;
import org.codeartisans.qipki.ca.domain.ca.CA;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStore;
import org.codeartisans.qipki.ca.domain.revocation.Revocation;
import org.codeartisans.qipki.ca.domain.x509.X509;
import org.codeartisans.qipki.commons.values.crypto.CryptoValuesFactory;
import org.codeartisans.qipki.commons.values.rest.ApiURIsValue;
import org.codeartisans.qipki.commons.values.rest.RestListValue;
import org.codeartisans.qipki.commons.values.rest.RestValue;
import org.codeartisans.qipki.commons.values.rest.CAValue;
import org.codeartisans.qipki.commons.values.rest.CryptoStoreValue;
import org.codeartisans.qipki.commons.values.rest.RevocationValue;
import org.codeartisans.qipki.commons.values.rest.X509DetailValue;
import org.codeartisans.qipki.commons.values.rest.X509Value;
import org.codeartisans.qipki.core.QiPkiFailure;
import org.codeartisans.qipki.core.crypto.objects.CryptObjectsFactory;
import org.codeartisans.qipki.core.crypto.objects.KeyInformation;
import org.codeartisans.qipki.core.crypto.x509.X509ExtensionsValueFactory;
import org.codeartisans.qipki.core.crypto.codec.CryptCodex;
import org.codeartisans.qipki.core.crypto.digest.DigestParameters;
import org.codeartisans.qipki.core.crypto.digest.DigestService;
import org.codeartisans.qipki.crypto.algorithms.DigestAlgorithm;
import org.qi4j.api.entity.Identity;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.value.ValueBuilder;
import org.qi4j.api.value.ValueBuilderFactory;
import org.restlet.data.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mixins( RestletValuesFactory.Mixin.class )
public interface RestletValuesFactory
        extends ServiceComposite
{

    ApiURIsValue apiURIs( Reference rootRef );

    CAValue ca( Reference parentRef, CA ca );

    CryptoStoreValue cryptoStore( Reference parentRef, CryptoStore ks );

    X509Value x509( Reference parentRef, X509 x509 );

    X509DetailValue x509Detail( Reference parentRef, X509 x509 );

    RevocationValue revocation( Reference parentRef, Revocation revocation );

    Iterable<RestValue> asValues( Reference parentRef, Iterable objects );

    RestListValue newListRepresentationValue( Reference parentRef, int start, Iterable<RestValue> list );

    abstract class Mixin
            implements RestletValuesFactory
    {

        private static final Logger LOGGER = LoggerFactory.getLogger( RestletValuesFactory.class );
        @Structure
        private ValueBuilderFactory vbf;
        @Service
        private CryptoValuesFactory commonValuesFactory;
        @Service
        private X509ExtensionsValueFactory x509ExtensionsValueFactory;
        @Service
        private CryptCodex cryptCodex;
        @Service
        private DigestService digester;
        @Service
        private CryptObjectsFactory cryptoToolFactory;

        @Override
        public ApiURIsValue apiURIs( Reference rootRef )
        {
            ValueBuilder<ApiURIsValue> apiBuilder = vbf.newValueBuilder( ApiURIsValue.class );
            ApiURIsValue api = apiBuilder.prototype();
            api.uri().set( rootRef.toString() );
            api.cryptoStoreListUri().set( rootRef.clone().addSegment( "cryptostore" ).toString() );
            api.cryptoStoreFactoryUri().set( rootRef.clone().addSegment( "cryptostore" ).addSegment( "factory" ).toString() );
            api.caListUri().set( rootRef.clone().addSegment( "ca" ).toString() );
            api.caFactoryUri().set( rootRef.clone().addSegment( "ca" ).addSegment( "factory" ).toString() );
            api.x509ListUri().set( rootRef.clone().addSegment( "x509" ).toString() );
            api.x509FactoryUri().set( rootRef.clone().addSegment( "x509" ).addSegment( "factory" ).toString() );
            return apiBuilder.newInstance();
        }

        @Override
        public CAValue ca( Reference parentRef, CA ca )
        {

            ValueBuilder<CAValue> caValueBuilder = vbf.newValueBuilder( CAValue.class );
            CAValue caValue = caValueBuilder.prototype();
            Reference uri = appendIdentity( parentRef, ca );
            caValue.uri().set( uri.toString() );
            caValue.crlUri().set( uri.addSegment( "crl" ).toString() );

            caValue.identity().set( ca.identity().get() );
            caValue.name().set( ca.name().get() );
            caValue.keystoreIdentity().set( ca.cryptoStore().get().identity().get() );

            return caValueBuilder.newInstance();
        }

        @Override
        public CryptoStoreValue cryptoStore( Reference parentRef, CryptoStore ks )
        {
            ValueBuilder<CryptoStoreValue> ksValueBuilder = vbf.newValueBuilder( CryptoStoreValue.class );
            CryptoStoreValue ksValue = ksValueBuilder.prototype();
            ksValue.uri().set( appendIdentity( parentRef, ks ).toString() );

            ksValue.identity().set( ks.identity().get() );
            ksValue.name().set( ks.name().get() );
            ksValue.storeType().set( ks.storeType().get() );
            ksValue.password().set( ks.password().get() );

            return ksValueBuilder.newInstance();
        }

        @Override
        public X509Value x509( Reference parentRef, X509 x509 )
        {
            ValueBuilder<X509Value> x509ValueBuilder = vbf.newValueBuilder( X509Value.class );
            X509Value x509Value = x509ValueBuilder.prototype();
            Reference uri = appendIdentity( parentRef, x509 );
            x509Value.uri().set( uri.toString() );
            x509Value.detailUri().set( uri.clone().addSegment( "detail" ).toString() );
            x509Value.revocationUri().set( uri.clone().addSegment( "revocation" ).toString() );

            x509Value.canonicalSubjectDN().set( x509.canonicalSubjectDN().get() );
            x509Value.canonicalIssuerDN().set( x509.canonicalIssuerDN().get() );
            x509Value.hexSerialNumber().set( x509.hexSerialNumber().get() );
            x509Value.validityInterval().set( commonValuesFactory.buildValidityInterval( x509.validityInterval().get().notBefore().get(),
                                                                                         x509.validityInterval().get().notAfter().get() ) );
            // TODO Add relations identities

            return x509ValueBuilder.newInstance();
        }

        @Override
        public X509DetailValue x509Detail( Reference parentRef, X509 x509 )
        {
            try {
                X509Certificate cert = x509.x509Certificate();
                KeyInformation publicKeyInfo = cryptoToolFactory.newKeyInformationInstance( cert.getPublicKey() );
                ValueBuilder<X509DetailValue> x509DetailValueBuilder = vbf.newValueBuilder( X509DetailValue.class );

                X509DetailValue x509DetailValue = x509DetailValueBuilder.prototype();
                Reference x509Uri = appendIdentity( parentRef, x509 );
                x509DetailValue.uri().set( x509Uri.toString() );
                x509DetailValue.detailUri().set( x509Uri.clone().addSegment( "detail" ).toString() );
                x509DetailValue.revocationUri().set( x509Uri.clone().addSegment( "revocation" ).toString() );
                x509DetailValue.canonicalSubjectDN().set( x509.canonicalSubjectDN().get() );
                x509DetailValue.canonicalIssuerDN().set( x509.canonicalIssuerDN().get() );
                x509DetailValue.hexSerialNumber().set( x509.hexSerialNumber().get() );
                x509DetailValue.validityInterval().set( commonValuesFactory.buildValidityInterval( x509.validityInterval().get().notBefore().get(),
                                                                                                   x509.validityInterval().get().notAfter().get() ) );
                // TODO Add relations identities

                x509DetailValue.certificateVersion().set( cert.getVersion() );
                x509DetailValue.signatureAlgorithm().set( cert.getSigAlgName() );
                x509DetailValue.publicKeyAlgorithm().set( publicKeyInfo.getKeyAlgorithm() );
                x509DetailValue.publicKeySize().set( publicKeyInfo.getKeySize() );
                if ( cert.getSubjectUniqueID() != null ) {
                    x509DetailValue.hexSubjectUniqueIdentifier().set( cryptCodex.toHexString( cert.getSubjectUniqueID() ) );
                }
                if ( cert.getIssuerUniqueID() != null ) {
                    x509DetailValue.hexIssuerUniqueIdentifier().set( cryptCodex.toHexString( cert.getIssuerUniqueID() ) );
                }
                x509DetailValue.md5Fingerprint().set( digester.hexDigest( cert.getEncoded(), new DigestParameters( DigestAlgorithm.MD5 ) ) );
                x509DetailValue.sha1Fingerprint().set( digester.hexDigest( cert.getEncoded(), new DigestParameters( DigestAlgorithm.SHA_1 ) ) );
                x509DetailValue.sha256Fingerprint().set( digester.hexDigest( cert.getEncoded(), new DigestParameters( DigestAlgorithm.SHA_256 ) ) );

                // Key informations
                x509DetailValue.keysExtensions().set( x509ExtensionsValueFactory.buildKeysExtensionsValue( cert ) );

                // Policies
                x509DetailValue.policiesExtensions().set( x509ExtensionsValueFactory.buildPoliciesExtensionsValue( cert ) );

                // Alternative Names
                x509DetailValue.namesExtensions().set( x509ExtensionsValueFactory.buildNamesExtensionsValue( cert ) );

                // Constraints
                x509DetailValue.constraintsExtensions().set( x509ExtensionsValueFactory.buildConstraintsExtensionsValue( cert ) );

                return x509DetailValueBuilder.newInstance();
            } catch ( CertificateEncodingException ex ) {
                throw new QiPkiFailure( "Unable to calculate X509Certificate fingerprints", ex );
            }
        }

        @Override
        public RevocationValue revocation( Reference parentRef, Revocation revocation )
        {
            ValueBuilder<RevocationValue> revocationValueBuilder = vbf.newValueBuilder( RevocationValue.class );
            RevocationValue revocationValue = revocationValueBuilder.prototype();
            Reference x509Uri = appendIdentity( parentRef, revocation.x509().get() );
            revocationValue.x509Uri().set( x509Uri.toString() );
            revocationValue.uri().set( x509Uri.addSegment( "revocation" ).toString() );
            revocationValue.reason().set( revocation.reason().get() );
            return revocationValueBuilder.newInstance();
        }

        @Override
        public Iterable<RestValue> asValues( Reference parentRef, Iterable objects )
        {
            Set<RestValue> set = new LinkedHashSet<RestValue>();
            for ( Object eachObj : objects ) {
                try {
                    set.add( ca( parentRef, ( CA ) eachObj ) );
                    continue;
                } catch ( ClassCastException ex ) {
                    LOGGER.trace( "Object is not a CA: {}", ex.getMessage() );
                }
                try {
                    set.add( cryptoStore( parentRef, ( CryptoStore ) eachObj ) );
                    continue;
                } catch ( ClassCastException ex ) {
                    LOGGER.trace( "Object is not a CryptoStore: {}", ex.getMessage() );
                }
                try {
                    set.add( x509( parentRef, ( X509 ) eachObj ) );
                    continue;
                } catch ( ClassCastException ex ) {
                    LOGGER.trace( "Object is not a X509: {}", ex.getMessage() );
                }
                throw new IllegalArgumentException( "Entity is of unsupported Type: " + eachObj );
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

        private Reference appendIdentity( Reference parentRef, Identity identity )
        {
            return parentRef.addSegment( identity.identity().get() );
        }

    }

}
