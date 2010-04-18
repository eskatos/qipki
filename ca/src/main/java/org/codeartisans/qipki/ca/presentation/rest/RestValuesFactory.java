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
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.codeartisans.qipki.ca.domain.ca.CA;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStore;
import org.codeartisans.qipki.ca.domain.x509.X509;
import org.codeartisans.qipki.commons.values.CommonValuesFactory;
import org.codeartisans.qipki.commons.values.rest.RestListValue;
import org.codeartisans.qipki.commons.values.rest.RestValue;
import org.codeartisans.qipki.commons.values.rest.CAValue;
import org.codeartisans.qipki.commons.values.rest.CryptoStoreValue;
import org.codeartisans.qipki.commons.values.rest.x509.KeysExtensions;
import org.codeartisans.qipki.commons.values.rest.x509.X509DetailValue;
import org.codeartisans.qipki.commons.values.rest.x509.X509Value;
import org.codeartisans.qipki.core.QiPkiFailure;
import org.codeartisans.qipki.core.crypto.CryptCodex;
import org.codeartisans.qipki.core.crypto.CryptoToolFactory;
import org.codeartisans.qipki.core.crypto.X509ExtensionsReader;
import org.joda.time.Interval;
import org.joda.time.Period;
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

@Mixins( RestValuesFactory.Mixin.class )
public interface RestValuesFactory
        extends ServiceComposite
{

    CAValue ca( Reference parentRef, CA ca );

    CryptoStoreValue cryptoStore( Reference parentRef, CryptoStore ks );

    X509Value x509( Reference parentRef, X509 x509 );

    X509DetailValue x509Detail( Reference parentRef, X509 x509 );

    Iterable<RestValue> asValues( Reference parentRef, Iterable objects );

    RestListValue newListRepresentationValue( Reference parentRef, int start, Iterable<RestValue> list );

    abstract class Mixin
            implements RestValuesFactory
    {

        private static final Logger LOGGER = LoggerFactory.getLogger( RestValuesFactory.class );
        @Structure
        private ValueBuilderFactory vbf;
        @Service
        private CommonValuesFactory commonValuesFactory;
        @Service
        private CryptoToolFactory cryptoToolFactory;

        @Override
        public CAValue ca( Reference parentRef, CA ca )
        {
            ValueBuilder<CAValue> caValueBuilder = vbf.newValueBuilder( CAValue.class );
            CAValue caValue = caValueBuilder.prototype();
            caValue.uri().set( appendIdentity( parentRef, ca ) );

            caValue.name().set( ca.name().get() );
            caValue.keystoreIdentity().set( ca.cryptoStore().get().identity().get() );

            return caValueBuilder.newInstance();
        }

        @Override
        public CryptoStoreValue cryptoStore( Reference parentRef, CryptoStore ks )
        {
            ValueBuilder<CryptoStoreValue> ksValueBuilder = vbf.newValueBuilder( CryptoStoreValue.class );
            CryptoStoreValue ksValue = ksValueBuilder.prototype();
            ksValue.uri().set( appendIdentity( parentRef, ks ) );

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
            x509Value.uri().set( appendIdentity( parentRef, x509 ) );

            x509Value.canonicalSubjectDN().set( x509.canonicalSubjectDN().get() );
            x509Value.canonicalIssuerDN().set( x509.canonicalIssuerDN().get() );
            x509Value.hexSerialNumber().set( x509.hexSerialNumber().get() );
            x509Value.validityPeriod().set( commonValuesFactory.buildValidityPeriod( x509.validityPeriod().get().notBefore().get(),
                                                                                     x509.validityPeriod().get().notAfter().get() ) );
            // TODO Add relations identities

            return x509ValueBuilder.newInstance();
        }

        @Override
        public X509DetailValue x509Detail( Reference parentRef, X509 x509 )
        {
            try {
                CryptCodex cryptcodex = cryptoToolFactory.newCryptCodexInstance();
                X509ExtensionsReader x509ExtReader = cryptoToolFactory.newX509ExtensionsReaderInstance();
                ValueBuilder<X509DetailValue> x509DetailValueBuilder = vbf.newValueBuilder( X509DetailValue.class );

                X509DetailValue x509DetailValue = x509DetailValueBuilder.prototype();
                x509DetailValue.uri().set( appendIdentity( parentRef, x509 ) + "/detail" );
                x509DetailValue.canonicalSubjectDN().set( x509.canonicalSubjectDN().get() );
                x509DetailValue.canonicalIssuerDN().set( x509.canonicalIssuerDN().get() );
                x509DetailValue.hexSerialNumber().set( x509.hexSerialNumber().get() );
                x509DetailValue.validityPeriod().set( commonValuesFactory.buildValidityPeriod( x509.validityPeriod().get().notBefore().get(),
                                                                                               x509.validityPeriod().get().notAfter().get() ) );
                // TODO Add relations identities
                X509Certificate cert = x509.x509Certificate();
                x509DetailValue.certificateVersion().set( cert.getVersion() );
                x509DetailValue.signatureAlgorithm().set( cert.getSigAlgName() );
                x509DetailValue.hexSubjectUniqueIdentifier().set( cryptcodex.toHexString( cert.getSubjectUniqueID() ) );
                x509DetailValue.hexIssuerUniqueIdentifier().set( cryptcodex.toHexString( cert.getIssuerUniqueID() ) );
                x509DetailValue.md5Fingerprint().set( new Md5Hash( cert.getEncoded() ).toHex() );
                x509DetailValue.sha1Fingerprint().set( new Sha1Hash( cert.getEncoded() ).toHex() );
                x509DetailValue.sha256Fingerprint().set( new Sha256Hash( cert.getEncoded() ).toHex() );

                ValueBuilder<KeysExtensions> keyExtensionsBuilder = vbf.newValueBuilder( KeysExtensions.class );
                KeysExtensions keyExtensions = keyExtensionsBuilder.prototype();

                keyExtensions.keyUsages().get().addAll( x509ExtReader.getKeyUsages( cert ) );
                byte[] subKeyId = x509ExtReader.getSubjectKeyIdentifier( cert );
                if ( subKeyId != null ) {
                    keyExtensions.subjectKeyIdentifier().set( cryptcodex.toHexString( subKeyId ) );
                }
                AuthorityKeyIdentifier authKeyId = x509ExtReader.getAuthorityKeyIdentifier( cert );
                if ( authKeyId != null ) {
                    ValueBuilder<KeysExtensions.AuthorityKeyIdentifier> akiValueBuilder = vbf.newValueBuilder( KeysExtensions.AuthorityKeyIdentifier.class );
                    KeysExtensions.AuthorityKeyIdentifier akiValue = akiValueBuilder.prototype();
                    akiValue.keyIdentifier().set( cryptcodex.toHexString( authKeyId.getKeyIdentifier() ) );
                    akiValue.serialNumber().set( cryptcodex.toHexString( authKeyId.getAuthorityCertSerialNumber() ) );
                    akiValue.names().set( x509ExtReader.asStrings( authKeyId.getAuthorityCertIssuer() ) );
                    keyExtensions.authorityKeyIdentifier().set( akiValueBuilder.newInstance() );
                }
                Interval privKeyUsageInterval = x509ExtReader.getPrivateKeyUsagePeriod( cert );
                if ( privKeyUsageInterval != null ) {
                    keyExtensions.privateKeyUsagePeriod().set( commonValuesFactory.buildValidityPeriod( privKeyUsageInterval ) );
                }

                x509DetailValue.keysExtensions().set( keyExtensionsBuilder.newInstance() );

                return x509DetailValueBuilder.newInstance();
            } catch ( CertificateEncodingException ex ) {
                throw new QiPkiFailure( "Unable to calculate X509Certificate fingerprints", ex );
            }
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

        private String appendIdentity( Reference parentRef, Identity identity )
        {
            return parentRef.addSegment( identity.identity().get() ).toString();
        }

    }

}
