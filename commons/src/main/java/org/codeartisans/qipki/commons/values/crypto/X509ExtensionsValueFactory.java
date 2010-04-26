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
package org.codeartisans.qipki.commons.values.crypto;

import java.security.cert.X509Certificate;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.GeneralSubtree;
import org.bouncycastle.asn1.x509.NameConstraints;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.bouncycastle.asn1.x509.PolicyQualifierInfo;
import org.codeartisans.qipki.commons.values.crypto.x509.ConstraintsExtensionsValue;
import static org.bouncycastle.asn1.x509.X509Extensions.*;
import org.codeartisans.qipki.commons.values.crypto.x509.ConstraintsExtensionsValue.BasicConstraintsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.ConstraintsExtensionsValue.NameConstraintsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.ConstraintsExtensionsValue.PolicyConstraintValue;
import org.codeartisans.qipki.commons.values.crypto.x509.ConstraintsExtensionsValue.PolicyConstraintsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.KeysExtensionsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.KeysExtensionsValue.AuthorityKeyIdentifierValue;
import org.codeartisans.qipki.commons.values.crypto.x509.KeysExtensionsValue.CRLDistributionPointsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.KeysExtensionsValue.KeyUsagesValue;
import org.codeartisans.qipki.commons.values.crypto.x509.KeysExtensionsValue.PrivateKeyUsageIntervalValue;
import org.codeartisans.qipki.commons.values.crypto.x509.KeysExtensionsValue.SubjectKeyIdentifierValue;
import org.codeartisans.qipki.commons.values.crypto.x509.NamesExtensionsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.NamesExtensionsValue.AlternativeNamesValue;
import org.codeartisans.qipki.commons.values.crypto.x509.PoliciesExtensionsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.PoliciesExtensionsValue.CertificatePoliciesValue;
import org.codeartisans.qipki.commons.values.crypto.x509.PoliciesExtensionsValue.PolicyInformationValue;
import org.codeartisans.qipki.commons.values.crypto.x509.PoliciesExtensionsValue.PolicyMappingValue;
import org.codeartisans.qipki.commons.values.crypto.x509.PoliciesExtensionsValue.PolicyMappingsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.PoliciesExtensionsValue.PolicyQualifierInfoValue;
import org.codeartisans.qipki.commons.values.crypto.x509.X509GeneralNameValue;
import org.codeartisans.qipki.commons.values.crypto.x509.X509GeneralSubtreeValue;
import org.codeartisans.qipki.crypto.codec.CryptCodex;
import org.codeartisans.qipki.crypto.x509.KeyUsage;
import org.codeartisans.qipki.crypto.x509.PolicyConstraint;
import org.codeartisans.qipki.crypto.x509.PolicyMapping;
import org.codeartisans.qipki.crypto.x509.RevocationReason;
import org.codeartisans.qipki.crypto.x509.X509ExtensionsReader;
import org.codeartisans.qipki.crypto.x509.X509GeneralName;
import org.joda.time.Interval;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.value.ValueBuilder;
import org.qi4j.api.value.ValueBuilderFactory;

// TODO Merge X509ExtensionsValueFactory into CryptoValuesFactory
@Mixins( X509ExtensionsValueFactory.Mixin.class )
public interface X509ExtensionsValueFactory
        extends ServiceComposite
{

    KeysExtensionsValue buildKeysExtensionsValue( X509Certificate cert );

    PoliciesExtensionsValue buildPoliciesExtensionsValue( X509Certificate cert );

    NamesExtensionsValue buildNamesExtensionsValue( X509Certificate cert );

    ConstraintsExtensionsValue buildConstraintsExtensionsValue( X509Certificate cert );

    abstract class Mixin
            implements X509ExtensionsValueFactory
    {

        @Structure
        private ValueBuilderFactory vbf;
        @Service
        private CryptCodex cryptCodex;
        @Service
        private X509ExtensionsReader x509ExtReader;

        @Override
        public KeysExtensionsValue buildKeysExtensionsValue( X509Certificate cert )
        {
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
                        cryptCodex.toHexString( subKeyId ) );
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

        @Override
        public PoliciesExtensionsValue buildPoliciesExtensionsValue( X509Certificate cert )
        {
            Set<PolicyInformation> certPolicies = x509ExtReader.getCertificatePolicies( cert );
            Set<PolicyMapping> policyMappings = x509ExtReader.getPolicyMappings( cert );

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

        @Override
        public NamesExtensionsValue buildNamesExtensionsValue( X509Certificate cert )
        {
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

        @Override
        public ConstraintsExtensionsValue buildConstraintsExtensionsValue( X509Certificate cert )
        {
            BasicConstraints basicConstraints = x509ExtReader.getBasicConstraints( cert );
            NameConstraints nameConstraints = x509ExtReader.getNameConstraints( cert );
            Set<PolicyConstraint> policyConstraints = x509ExtReader.getPolicyConstraints( cert );

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
            skiValue.hexKeyIdentifier().set( hexKeyIdentifier );
            return skiBuilder.newInstance();
        }

        private AuthorityKeyIdentifierValue buildAuthorityKeyIdentifierValue( boolean critical, AuthorityKeyIdentifier authKeyId )
        {
            ValueBuilder<AuthorityKeyIdentifierValue> akiBuilder = vbf.newValueBuilder( AuthorityKeyIdentifierValue.class );
            AuthorityKeyIdentifierValue akiValue = akiBuilder.prototype();
            akiValue.critical().set( critical );
            akiValue.hexKeyIdentifier().set( cryptCodex.toHexString( authKeyId.getKeyIdentifier() ) );
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
                        endpoints.add( cryptCodex.toString( eachPoint.getDistributionPoint().getName() ) );
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

        private PolicyMappingsValue buildPolicyMappingsValue( boolean critical, Set<PolicyMapping> policyMappings )
        {
            ValueBuilder<PolicyMappingsValue> pmBuilder = vbf.newValueBuilder( PolicyMappingsValue.class );
            PolicyMappingsValue pmValue = pmBuilder.prototype();
            pmValue.critical().set( critical );
            Set<PolicyMappingValue> mappingValues = new LinkedHashSet<PolicyMappingValue>();
            for ( PolicyMapping eachMapping : policyMappings ) {
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

        private PolicyConstraintsValue buildPolicyConstraintsValue( boolean critical, Set<PolicyConstraint> policyConstraints )
        {
            ValueBuilder<PolicyConstraintsValue> pcBuilder = vbf.newValueBuilder( PolicyConstraintsValue.class );
            PolicyConstraintsValue pcValue = pcBuilder.prototype();
            pcValue.critical().set( critical );
            Set<PolicyConstraintValue> constrainsSet = new LinkedHashSet<PolicyConstraintValue>();
            for ( PolicyConstraint eachConstraint : policyConstraints ) {
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
