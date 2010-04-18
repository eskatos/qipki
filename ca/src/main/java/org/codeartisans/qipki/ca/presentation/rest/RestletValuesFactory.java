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
import java.util.Map;
import java.util.Set;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.GeneralSubtree;
import org.bouncycastle.asn1.x509.NameConstraints;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.bouncycastle.asn1.x509.PolicyQualifierInfo;
import static org.bouncycastle.asn1.x509.X509Extensions.*;
import org.codeartisans.qipki.ca.domain.ca.CA;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStore;
import org.codeartisans.qipki.ca.domain.x509.X509;
import org.codeartisans.qipki.commons.constants.KeyUsage;
import org.codeartisans.qipki.commons.constants.RevocationReason;
import org.codeartisans.qipki.commons.constants.X509GeneralName;
import org.codeartisans.qipki.commons.values.CommonValuesFactory;
import org.codeartisans.qipki.commons.values.rest.RestListValue;
import org.codeartisans.qipki.commons.values.rest.RestValue;
import org.codeartisans.qipki.commons.values.rest.CAValue;
import org.codeartisans.qipki.commons.values.rest.CryptoStoreValue;
import org.codeartisans.qipki.commons.values.rest.x509.ConstraintsExtensionsValue;
import org.codeartisans.qipki.commons.values.rest.x509.ConstraintsExtensionsValue.BasicConstraintsValue;
import org.codeartisans.qipki.commons.values.rest.x509.ConstraintsExtensionsValue.NameConstraintsValue;
import org.codeartisans.qipki.commons.values.rest.x509.ConstraintsExtensionsValue.PolicyConstraintValue;
import org.codeartisans.qipki.commons.values.rest.x509.ConstraintsExtensionsValue.PolicyConstraintsValue;
import org.codeartisans.qipki.commons.values.rest.x509.KeysExtensionsValue;
import org.codeartisans.qipki.commons.values.rest.x509.KeysExtensionsValue.AuthorityKeyIdentifierValue;
import org.codeartisans.qipki.commons.values.rest.x509.KeysExtensionsValue.CRLDistributionPointsValue;
import org.codeartisans.qipki.commons.values.rest.x509.KeysExtensionsValue.KeyUsagesValue;
import org.codeartisans.qipki.commons.values.rest.x509.KeysExtensionsValue.PrivateKeyUsageIntervalValue;
import org.codeartisans.qipki.commons.values.rest.x509.KeysExtensionsValue.SubjectKeyIdentifierValue;
import org.codeartisans.qipki.commons.values.rest.x509.NamesExtensionsValue;
import org.codeartisans.qipki.commons.values.rest.x509.NamesExtensionsValue.AlternativeNamesValue;
import org.codeartisans.qipki.commons.values.rest.x509.PoliciesExtensionsValue;
import org.codeartisans.qipki.commons.values.rest.x509.PoliciesExtensionsValue.CertificatePoliciesValue;
import org.codeartisans.qipki.commons.values.rest.x509.PoliciesExtensionsValue.PolicyInformationValue;
import org.codeartisans.qipki.commons.values.rest.x509.PoliciesExtensionsValue.PolicyMappingValue;
import org.codeartisans.qipki.commons.values.rest.x509.PoliciesExtensionsValue.PolicyMappingsValue;
import org.codeartisans.qipki.commons.values.rest.x509.PoliciesExtensionsValue.PolicyQualifierInfoValue;
import org.codeartisans.qipki.commons.values.rest.x509.X509DetailValue;
import org.codeartisans.qipki.commons.values.rest.x509.X509GeneralNameValue;
import org.codeartisans.qipki.commons.values.rest.x509.X509GeneralSubtreeValue;
import org.codeartisans.qipki.commons.values.rest.x509.X509Value;
import org.codeartisans.qipki.core.QiPkiFailure;
import org.codeartisans.qipki.core.crypto.CryptCodex;
import org.codeartisans.qipki.core.crypto.CryptoToolFactory;
import org.codeartisans.qipki.core.crypto.KeyInformation;
import org.codeartisans.qipki.core.crypto.X509ExtensionsReader;
import org.joda.time.Interval;
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

    CAValue ca( Reference parentRef, CA ca );

    CryptoStoreValue cryptoStore( Reference parentRef, CryptoStore ks );

    X509Value x509( Reference parentRef, X509 x509 );

    X509DetailValue x509Detail( Reference parentRef, X509 x509 );

    Iterable<RestValue> asValues( Reference parentRef, Iterable objects );

    RestListValue newListRepresentationValue( Reference parentRef, int start, Iterable<RestValue> list );

    abstract class Mixin
            implements RestletValuesFactory
    {

        private static final Logger LOGGER = LoggerFactory.getLogger( RestletValuesFactory.class );
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
                CryptCodex cryptcodex = cryptoToolFactory.newCryptCodexInstance();
                KeyInformation publicKeyInfo = cryptoToolFactory.newKeyInformationInstance( cert.getPublicKey() );
                ValueBuilder<X509DetailValue> x509DetailValueBuilder = vbf.newValueBuilder( X509DetailValue.class );

                X509DetailValue x509DetailValue = x509DetailValueBuilder.prototype();
                x509DetailValue.uri().set( appendIdentity( parentRef, x509 ) + "/detail" );
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
                x509DetailValue.hexSubjectUniqueIdentifier().set( cryptcodex.toHexString( cert.getSubjectUniqueID() ) );
                x509DetailValue.hexIssuerUniqueIdentifier().set( cryptcodex.toHexString( cert.getIssuerUniqueID() ) );
                x509DetailValue.md5Fingerprint().set( new Md5Hash( cert.getEncoded() ).toHex() );
                x509DetailValue.sha1Fingerprint().set( new Sha1Hash( cert.getEncoded() ).toHex() );
                x509DetailValue.sha256Fingerprint().set( new Sha256Hash( cert.getEncoded() ).toHex() );

                // Key informations
                x509DetailValue.keysExtensions().set( buildKeysExtensionsValue( cert ) );

                // Policies
                x509DetailValue.policiesExtensions().set( buildPoliciesExtensionsValue( cert ) );

                // Alternative Names
                x509DetailValue.namesExtensions().set( buildNamesExtensionsValue( cert ) );

                // Constraints
                x509DetailValue.constraintsExtensions().set( buildConstraintsExtensionsValue( cert ) );

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

        private KeysExtensionsValue buildKeysExtensionsValue( X509Certificate cert )
        {
            X509ExtensionsReader x509ExtReader = cryptoToolFactory.newX509ExtensionsReaderInstance();
            CryptCodex cryptcodex = cryptoToolFactory.newCryptCodexInstance();
            Set<KeyUsage> keyUsages = x509ExtReader.getKeyUsages( cert );
            byte[] subKeyId = x509ExtReader.getSubjectKeyIdentifier( cert );
            AuthorityKeyIdentifier authKeyId = x509ExtReader.getAuthorityKeyIdentifier( cert );
            Interval privKeyUsageInterval = x509ExtReader.getPrivateKeyUsagePeriod( cert );
            DistributionPoint[] crlDistPoints = x509ExtReader.getCRLDistributionPoints( cert );

            if ( keyUsages.isEmpty() && subKeyId == null && authKeyId == null && privKeyUsageInterval == null && crlDistPoints == null ) {
                return null;
            }
            ValueBuilder<KeysExtensionsValue> keyExtensionsBuilder = vbf.newValueBuilder( KeysExtensionsValue.class );
            KeysExtensionsValue keyExtensions = keyExtensionsBuilder.prototype();

            if ( !keyUsages.isEmpty() ) {
                KeyUsagesValue kuv = buildKeyUsagesValue(
                        cert.getCriticalExtensionOIDs().contains( KeyUsage.getId() ),
                        keyUsages );
                keyExtensions.keyUsages().set( kuv );
            }

            if ( subKeyId != null ) {
                SubjectKeyIdentifierValue skiv = buildSubjectKeyIdentifierValue(
                        cert.getCriticalExtensionOIDs().contains( SubjectKeyIdentifier.getId() ),
                        cryptcodex.toHexString( subKeyId ) );
                keyExtensions.subjectKeyIdentifier().set( skiv );
            }

            if ( authKeyId != null ) {
                AuthorityKeyIdentifierValue aki = buildAuthorityKeyIdentifierValue(
                        cert.getCriticalExtensionOIDs().contains( AuthorityKeyIdentifier.getId() ),
                        authKeyId );
                keyExtensions.authorityKeyIdentifier().set( aki );
            }

            if ( privKeyUsageInterval != null ) {
                PrivateKeyUsageIntervalValue pkuiv = buildPrivateKeyUsageIntervalValue(
                        cert.getCriticalExtensionOIDs().contains( PrivateKeyUsagePeriod.getId() ),
                        privKeyUsageInterval );
                keyExtensions.privateKeyUsageInterval().set( pkuiv );
            }

            if ( crlDistPoints != null ) {
                CRLDistributionPointsValue cdpv = buildCRLDistributionPointsValue(
                        cert.getCriticalExtensionOIDs().contains( CRLDistributionPoints.getId() ),
                        crlDistPoints );
                keyExtensions.crlDistributionPoints().set( cdpv );
            }

            return keyExtensionsBuilder.newInstance();
        }

        private PoliciesExtensionsValue buildPoliciesExtensionsValue( X509Certificate cert )
        {
            X509ExtensionsReader x509ExtReader = cryptoToolFactory.newX509ExtensionsReaderInstance();
            Set<PolicyInformation> certPolicies = x509ExtReader.getCertificatePolicies( cert );
            Set<X509ExtensionsReader.PolicyMapping> policyMappings = x509ExtReader.getPolicyMappings( cert );

            if ( certPolicies.isEmpty() && policyMappings.isEmpty() ) {
                return null;
            }

            ValueBuilder<PoliciesExtensionsValue> policiesExtensionsBuilder = vbf.newValueBuilder( PoliciesExtensionsValue.class );
            PoliciesExtensionsValue policiesExtensions = policiesExtensionsBuilder.prototype();

            if ( !certPolicies.isEmpty() ) {
                CertificatePoliciesValue cpv = buildCertificatePoliciesValue(
                        cert.getCriticalExtensionOIDs().contains( CertificatePolicies.getId() ),
                        certPolicies );
                policiesExtensions.certificatePolicies().set( cpv );
            }

            if ( !policyMappings.isEmpty() ) {
                PolicyMappingsValue pmv = buildPolicyMappingsValue(
                        cert.getCriticalExtensionOIDs().contains( PolicyMappings.getId() ),
                        policyMappings );
                policiesExtensions.policyMappings().set( pmv );
            }

            return policiesExtensionsBuilder.newInstance();
        }

        private NamesExtensionsValue buildNamesExtensionsValue( X509Certificate cert )
        {
            X509ExtensionsReader x509ExtReader = cryptoToolFactory.newX509ExtensionsReaderInstance();
            Map<X509GeneralName, String> subjectNames = x509ExtReader.asMap( x509ExtReader.getSubjectAlternativeNames( cert ) );
            Map<X509GeneralName, String> issuerNames = x509ExtReader.asMap( x509ExtReader.getIssuerAlternativeNames( cert ) );

            if ( subjectNames.isEmpty() && issuerNames.isEmpty() ) {
                return null;
            }

            ValueBuilder<NamesExtensionsValue> namesExtensionsBuilder = vbf.newValueBuilder( NamesExtensionsValue.class );
            NamesExtensionsValue namesExtensions = namesExtensionsBuilder.prototype();

            if ( !subjectNames.isEmpty() ) {
                AlternativeNamesValue san = buildAlternativeNamesValue(
                        cert.getCriticalExtensionOIDs().contains( SubjectAlternativeName.getId() ),
                        subjectNames );
                namesExtensions.subjectAlternativeNames().set( san );
            }

            if ( !issuerNames.isEmpty() ) {
                AlternativeNamesValue ian = buildAlternativeNamesValue(
                        cert.getCriticalExtensionOIDs().contains( IssuerAlternativeName.getId() ),
                        issuerNames );
                namesExtensions.issuerAlternativeNames().set( ian );
            }

            return namesExtensionsBuilder.newInstance();
        }

        private ConstraintsExtensionsValue buildConstraintsExtensionsValue( X509Certificate cert )
        {
            X509ExtensionsReader x509ExtReader = cryptoToolFactory.newX509ExtensionsReaderInstance();
            BasicConstraints basicConstraints = x509ExtReader.getBasicConstraints( cert );
            NameConstraints nameConstraints = x509ExtReader.getNameConstraints( cert );
            Set<X509ExtensionsReader.PolicyConstraint> policyConstraints = x509ExtReader.getPolicyConstraints( cert );

            if ( basicConstraints == null && nameConstraints == null && policyConstraints.isEmpty() ) {
                return null;
            }

            ValueBuilder<ConstraintsExtensionsValue> constraintsExtensionsBuilder = vbf.newValueBuilder( ConstraintsExtensionsValue.class );
            ConstraintsExtensionsValue constraintsExtensions = constraintsExtensionsBuilder.prototype();

            if ( basicConstraints != null ) {
                BasicConstraintsValue bcv = buildBasicConstraintsValue(
                        cert.getCriticalExtensionOIDs().contains( BasicConstraints.getId() ),
                        basicConstraints );
                constraintsExtensions.basicConstraints().set( bcv );
            }

            if ( nameConstraints != null ) {
                NameConstraintsValue ncv = buildNameConstraintsValue(
                        cert.getCriticalExtensionOIDs().contains( NameConstraints.getId() ),
                        nameConstraints );
                constraintsExtensions.nameConstraints().set( ncv );
            }

            if ( !policyConstraints.isEmpty() ) {
                PolicyConstraintsValue pcv = buildPolicyConstraintsValue(
                        cert.getCriticalExtensionOIDs().contains( PolicyConstraints.getId() ),
                        policyConstraints );
                constraintsExtensions.policyConstraints().set( pcv );
            }

            return constraintsExtensionsBuilder.newInstance();
        }

        private KeyUsagesValue buildKeyUsagesValue( boolean critical, Set<KeyUsage> keyUsages )
        {
            ValueBuilder<KeyUsagesValue> kuBuilder = vbf.newValueBuilder( KeyUsagesValue.class );
            KeyUsagesValue kuValue = kuBuilder.prototype();
            kuValue.critical().set( critical );
            kuValue.keyUsages().set( keyUsages );
            return kuBuilder.newInstance();
        }

        private SubjectKeyIdentifierValue buildSubjectKeyIdentifierValue( boolean critical, String hexKeyIdentifier )
        {
            ValueBuilder<SubjectKeyIdentifierValue> skiBuilder = vbf.newValueBuilder( SubjectKeyIdentifierValue.class );
            SubjectKeyIdentifierValue skiValue = skiBuilder.prototype();
            skiValue.critical().set( critical );
            skiValue.keyIdentifier().set( hexKeyIdentifier );
            return skiBuilder.newInstance();
        }

        private AuthorityKeyIdentifierValue buildAuthorityKeyIdentifierValue( boolean critical, AuthorityKeyIdentifier authKeyId )
        {
            CryptCodex cryptcodex = cryptoToolFactory.newCryptCodexInstance();
            X509ExtensionsReader x509ExtReader = cryptoToolFactory.newX509ExtensionsReaderInstance();
            ValueBuilder<AuthorityKeyIdentifierValue> akiBuilder = vbf.newValueBuilder( AuthorityKeyIdentifierValue.class );
            AuthorityKeyIdentifierValue akiValue = akiBuilder.prototype();
            akiValue.critical().set( critical );
            akiValue.keyIdentifier().set( cryptcodex.toHexString( authKeyId.getKeyIdentifier() ) );
            akiValue.serialNumber().set( authKeyId.getAuthorityCertSerialNumber().longValue() );
            akiValue.names().set( buildSetOfNames( x509ExtReader.asMap( authKeyId.getAuthorityCertIssuer() ) ) );
            return akiBuilder.newInstance();
        }

        private PrivateKeyUsageIntervalValue buildPrivateKeyUsageIntervalValue( boolean critical, Interval interval )
        {
            ValueBuilder<PrivateKeyUsageIntervalValue> pkuBuilder = vbf.newValueBuilder( PrivateKeyUsageIntervalValue.class );
            PrivateKeyUsageIntervalValue pkuValue = pkuBuilder.prototype();
            pkuValue.critical().set( critical );
            pkuValue.notBefore().set( interval.getStart().toDate() );
            pkuValue.notAfter().set( interval.getEnd().toDate() );
            return pkuBuilder.newInstance();
        }

        private CRLDistributionPointsValue buildCRLDistributionPointsValue( boolean critical, DistributionPoint[] crlDistPoints )
        {
            CryptCodex cryptcodex = cryptoToolFactory.newCryptCodexInstance();
            X509ExtensionsReader x509ExtReader = cryptoToolFactory.newX509ExtensionsReaderInstance();
            ValueBuilder<KeysExtensionsValue.CRLDistributionPointsValue> cdpBuilder = vbf.newValueBuilder( KeysExtensionsValue.CRLDistributionPointsValue.class );
            KeysExtensionsValue.CRLDistributionPointsValue cdpValue = cdpBuilder.prototype();
            cdpValue.critical().set( critical );
            Set<String> endpoints = new LinkedHashSet<String>();
            Set<RevocationReason> reasons = new LinkedHashSet<RevocationReason>();
            Set<X509GeneralNameValue> issuerNames = new LinkedHashSet<X509GeneralNameValue>();
            for ( DistributionPoint eachPoint : crlDistPoints ) {
                switch ( eachPoint.getDistributionPoint().getType() ) {
                    case DistributionPointName.FULL_NAME:
                        endpoints.addAll( x509ExtReader.asMap( ( GeneralNames ) eachPoint.getDistributionPoint().getName() ).values() );
                        break;
                    case DistributionPointName.NAME_RELATIVE_TO_CRL_ISSUER:
                    default:
                        endpoints.add( cryptcodex.toString( eachPoint.getDistributionPoint().getName() ) );
                }
                reasons.addAll( x509ExtReader.getRevocationReasons( eachPoint.getReasons() ) );
                issuerNames.addAll( buildSetOfNames( x509ExtReader.asMap( eachPoint.getCRLIssuer() ) ) );
            }
            cdpValue.endpoints().set( endpoints );
            cdpValue.reasons().set( reasons );
            cdpValue.issuerNames().set( issuerNames );
            return cdpBuilder.newInstance();
        }

        private CertificatePoliciesValue buildCertificatePoliciesValue( boolean critical, Set<PolicyInformation> certPolicies )
        {
            ValueBuilder<CertificatePoliciesValue> cpBuilder = vbf.newValueBuilder( CertificatePoliciesValue.class );
            CertificatePoliciesValue cpValue = cpBuilder.prototype();
            cpValue.critical().set( critical );
            Set<PolicyInformationValue> policyValues = new LinkedHashSet<PolicyInformationValue>();
            for ( PolicyInformation eachPolicy : certPolicies ) {
                ValueBuilder<PolicyInformationValue> pBuilder = vbf.newValueBuilder( PolicyInformationValue.class );
                PolicyInformationValue pValue = pBuilder.prototype();
                pValue.oid().set( eachPolicy.getPolicyIdentifier().getId() );
                for ( int idx = 0; idx < eachPolicy.getPolicyQualifiers().size(); idx++ ) {
                    PolicyQualifierInfo eachInfo = ( PolicyQualifierInfo ) eachPolicy.getPolicyQualifiers().getObjectAt( idx );
                    ValueBuilder<PolicyQualifierInfoValue> qualifierValueBuilder = vbf.newValueBuilder( PolicyQualifierInfoValue.class );
                    PolicyQualifierInfoValue qualifierValue = qualifierValueBuilder.prototype();
                    qualifierValue.oid().set( eachInfo.getPolicyQualifierId().getId() );
                    qualifierValue.qualifier().set( eachInfo.getQualifier().toString() ); // TODO Need better decoding ?
                    pValue.policyQualifiers().get().add( qualifierValueBuilder.newInstance() );
                }
                policyValues.add( pBuilder.newInstance() );
            }
            cpValue.policies().set( policyValues );
            return cpBuilder.newInstance();
        }

        private PolicyMappingsValue buildPolicyMappingsValue( boolean critical, Set<X509ExtensionsReader.PolicyMapping> policyMappings )
        {
            ValueBuilder<PolicyMappingsValue> pmBuilder = vbf.newValueBuilder( PolicyMappingsValue.class );
            PolicyMappingsValue pmValue = pmBuilder.prototype();
            pmValue.critical().set( critical );
            Set<PolicyMappingValue> mappingValues = new LinkedHashSet<PolicyMappingValue>();
            for ( X509ExtensionsReader.PolicyMapping eachMapping : policyMappings ) {
                ValueBuilder<PolicyMappingValue> mBuilder = vbf.newValueBuilder( PolicyMappingValue.class );
                PolicyMappingValue mValue = mBuilder.prototype();
                mValue.issuerDomainPolicyOID().set( eachMapping.getIssuerDomainPolicyOID() );
                mValue.subjectDomainPolicyOID().set( eachMapping.getSubjectDomainPolicyOID() );
                mappingValues.add( mBuilder.newInstance() );
            }
            pmValue.mappings().set( mappingValues );
            return pmBuilder.newInstance();
        }

        private AlternativeNamesValue buildAlternativeNamesValue( boolean critical, Map<X509GeneralName, String> altNames )
        {
            ValueBuilder<AlternativeNamesValue> anBuilder = vbf.newValueBuilder( AlternativeNamesValue.class );
            AlternativeNamesValue an = anBuilder.prototype();
            an.critical().set( critical );
            an.alternativeNames().set( buildSetOfNames( altNames ) );
            return anBuilder.newInstance();
        }

        private BasicConstraintsValue buildBasicConstraintsValue( boolean critical, BasicConstraints basicConstraints )
        {
            ValueBuilder<BasicConstraintsValue> bcBuilder = vbf.newValueBuilder( BasicConstraintsValue.class );
            BasicConstraintsValue bcValue = bcBuilder.prototype();
            bcValue.critical().set( critical );
            bcValue.subjectIsCA().set( basicConstraints.isCA() );
            bcValue.pathLenConstraint().set( basicConstraints.getPathLenConstraint().longValue() );
            return bcBuilder.newInstance();
        }

        private NameConstraintsValue buildNameConstraintsValue( boolean critical, NameConstraints nameConstraints )
        {
            ValueBuilder<NameConstraintsValue> ncBuilder = vbf.newValueBuilder( NameConstraintsValue.class );
            NameConstraintsValue ncValue = ncBuilder.prototype();
            ncValue.critical().set( critical );
            for ( int idx = 0; idx < nameConstraints.getPermittedSubtrees().size(); idx++ ) {
                GeneralSubtree eachSubtree = ( GeneralSubtree ) nameConstraints.getPermittedSubtrees().getObjectAt( idx );
                ncValue.permittedSubtrees().get().add( buildSubtree( eachSubtree ) );
            }
            for ( int idx = 0; idx < nameConstraints.getExcludedSubtrees().size(); idx++ ) {
                GeneralSubtree eachSubtree = ( GeneralSubtree ) nameConstraints.getExcludedSubtrees().getObjectAt( idx );
                ncValue.excludedSubtrees().get().add( buildSubtree( eachSubtree ) );
            }
            return ncBuilder.newInstance();
        }

        private PolicyConstraintsValue buildPolicyConstraintsValue( boolean critical, Set<X509ExtensionsReader.PolicyConstraint> policyConstraints )
        {
            ValueBuilder<PolicyConstraintsValue> pcBuilder = vbf.newValueBuilder( PolicyConstraintsValue.class );
            PolicyConstraintsValue pcValue = pcBuilder.prototype();
            pcValue.critical().set( critical );
            Set<PolicyConstraintValue> constrainsSet = new LinkedHashSet<PolicyConstraintValue>();
            for ( X509ExtensionsReader.PolicyConstraint eachConstraint : policyConstraints ) {
                ValueBuilder<PolicyConstraintValue> cBuilder = vbf.newValueBuilder( PolicyConstraintValue.class );
                PolicyConstraintValue cValue = cBuilder.prototype();
                cValue.requireExplicitPolicy().set( eachConstraint.getRequireExplicitPolicy() );
                cValue.inhibitPolicyMapping().set( eachConstraint.getInhibitPolicyMapping() );
                constrainsSet.add( cBuilder.newInstance() );
            }
            pcValue.constraints().set( constrainsSet );
            return pcBuilder.newInstance();
        }

        private X509GeneralSubtreeValue buildSubtree( GeneralSubtree subtree )
        {
            X509ExtensionsReader x509ExtReader = cryptoToolFactory.newX509ExtensionsReaderInstance();
            ValueBuilder<X509GeneralSubtreeValue> subtreeValueBuilder = vbf.newValueBuilder( X509GeneralSubtreeValue.class );
            X509GeneralSubtreeValue subtreeValue = subtreeValueBuilder.prototype();
            Map.Entry<X509GeneralName, String> baseName = x509ExtReader.asImmutableMapEntry( subtree.getBase() );
            subtreeValue.base().set( buildName( baseName ) );
            subtreeValue.minimum().set( subtree.getMinimum().longValue() );
            subtreeValue.maximum().set( subtree.getMaximum().longValue() );
            return subtreeValueBuilder.newInstance();
        }

        private Set<X509GeneralNameValue> buildSetOfNames( Map<X509GeneralName, String> names )
        {
            Set<X509GeneralNameValue> setOfNames = new LinkedHashSet<X509GeneralNameValue>();
            for ( Map.Entry<X509GeneralName, String> eachName : names.entrySet() ) {
                ValueBuilder<X509GeneralNameValue> nameBuilder = vbf.newValueBuilder( X509GeneralNameValue.class );
                X509GeneralNameValue name = nameBuilder.prototype();
                name.nameType().set( eachName.getKey() );
                name.nameValue().set( eachName.getValue() );
                setOfNames.add( nameBuilder.newInstance() );
            }
            return setOfNames;
        }

        private X509GeneralNameValue buildName( Map.Entry<X509GeneralName, String> entry )
        {
            ValueBuilder<X509GeneralNameValue> nameBuilder = vbf.newValueBuilder( X509GeneralNameValue.class );
            X509GeneralNameValue name = nameBuilder.prototype();
            name.nameType().set( entry.getKey() );
            name.nameValue().set( entry.getValue() );
            return nameBuilder.newInstance();
        }

    }

}
