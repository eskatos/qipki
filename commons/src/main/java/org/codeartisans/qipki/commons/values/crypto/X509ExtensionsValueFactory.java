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
import org.bouncycastle.asn1.misc.MiscObjectIdentifiers;
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
import org.codeartisans.qipki.commons.values.crypto.x509.BasicConstraintsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.NameConstraintsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.KeysExtensionsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.AuthorityKeyIdentifierValue;
import org.codeartisans.qipki.commons.values.crypto.x509.CRLDistributionPointsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.KeyUsagesValue;
import org.codeartisans.qipki.commons.values.crypto.x509.PrivateKeyUsageIntervalValue;
import org.codeartisans.qipki.commons.values.crypto.x509.SubjectKeyIdentifierValue;
import org.codeartisans.qipki.commons.values.crypto.x509.NamesExtensionsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.AlternativeNamesValue;
import org.codeartisans.qipki.commons.values.crypto.x509.CertificatePoliciesValue;
import org.codeartisans.qipki.commons.values.crypto.x509.CertificatePoliciesValue.PolicyInformationValue;
import org.codeartisans.qipki.commons.values.crypto.x509.CertificatePoliciesValue.PolicyQualifierInfoValue;
import org.codeartisans.qipki.commons.values.crypto.x509.ExtendedKeyUsagesValue;
import org.codeartisans.qipki.commons.values.crypto.x509.NetscapeCertTypesValue;
import org.codeartisans.qipki.commons.values.crypto.x509.PoliciesExtensionsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.PolicyConstraintsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.PolicyConstraintsValue.PolicyConstraintValue;
import org.codeartisans.qipki.commons.values.crypto.x509.PolicyMappingsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.PolicyMappingsValue.PolicyMappingValue;
import org.codeartisans.qipki.commons.values.crypto.x509.X509GeneralNameValue;
import org.codeartisans.qipki.commons.values.crypto.x509.X509GeneralSubtreeValue;
import org.codeartisans.qipki.crypto.codec.CryptCodex;
import org.codeartisans.qipki.crypto.x509.ExtendedKeyUsage;
import org.codeartisans.qipki.crypto.x509.KeyUsage;
import org.codeartisans.qipki.crypto.x509.NetscapeCertType;
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

    KeyUsagesValue buildKeyUsagesValue( boolean critical, Set<KeyUsage> keyUsages );

    ExtendedKeyUsagesValue buildExtendedKeyUsagesValue( boolean critical, Set<ExtendedKeyUsage> extendedKeyUsages );

    NetscapeCertTypesValue buildNetscapeCertTypesValue( boolean critical, Set<NetscapeCertType> netscapeCertTypes );

    BasicConstraintsValue buildBasicConstraintsValue( boolean critical, boolean subjectIsCA, long pathLenConstraint );

    PoliciesExtensionsValue buildPoliciesExtensionsValue( X509Certificate cert );

    NamesExtensionsValue buildNamesExtensionsValue( X509Certificate cert );

    ConstraintsExtensionsValue buildConstraintsExtensionsValue( X509Certificate cert );

    NameConstraintsValue buildNameConstraintsValue( boolean critical, Set<X509GeneralSubtreeValue> permittedSubtrees, Set<X509GeneralSubtreeValue> excludedSubtrees );

    X509GeneralNameValue buildGeneralName( X509GeneralName nameType, String value );

    X509GeneralSubtreeValue buildGeneralSubtree( X509GeneralName nameType, String nameValue, Long subtreeMinimum, Long subtreeMaximum );

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
            Set<ExtendedKeyUsage> extendedKeyUsages = x509ExtReader.getExtendedKeyUsages( cert );
            Set<NetscapeCertType> netscapeCertTypes = x509ExtReader.getNetscapeCertTypes( cert );
            byte[] subKeyId = x509ExtReader.getSubjectKeyIdentifier( cert );
            AuthorityKeyIdentifier authKeyId = x509ExtReader.getAuthorityKeyIdentifier( cert );
            Interval privKeyUsageInterval = x509ExtReader.getPrivateKeyUsagePeriod( cert );
            DistributionPoint[] crlDistPoints = x509ExtReader.getCRLDistributionPoints( cert );
            if ( keyUsages.isEmpty() && extendedKeyUsages.isEmpty() && netscapeCertTypes.isEmpty()
                    && subKeyId == null && authKeyId == null && privKeyUsageInterval == null && crlDistPoints == null ) {
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

            if ( !extendedKeyUsages.isEmpty() ) {
                ExtendedKeyUsagesValue ekuv = buildExtendedKeyUsagesValue(
                        cert.getCriticalExtensionOIDs().contains( ExtendedKeyUsage.getId() ),
                        extendedKeyUsages );
                keyExtensions.extendedKeyUsages().set( ekuv );
            }

            if ( !netscapeCertTypes.isEmpty() ) {
                NetscapeCertTypesValue nctv = buildNetscapeCertTypesValue(
                        cert.getCriticalExtensionOIDs().contains( MiscObjectIdentifiers.netscapeCertType.getId() ),
                        netscapeCertTypes );
                keyExtensions.netscapeCertTypes().set( nctv );
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
                        basicConstraints.isCA(),
                        basicConstraints.getPathLenConstraint().longValue() );
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

        @Override
        public KeyUsagesValue buildKeyUsagesValue( boolean critical, Set<KeyUsage> keyUsages )
        {
            ValueBuilder<KeyUsagesValue> kuBuilder = vbf.newValueBuilder( KeyUsagesValue.class );
            KeyUsagesValue kuValue = kuBuilder.prototype();
            kuValue.critical().set( critical );
            kuValue.keyUsages().set( keyUsages );
            return kuBuilder.newInstance();
        }

        @Override
        public ExtendedKeyUsagesValue buildExtendedKeyUsagesValue( boolean critical, Set<ExtendedKeyUsage> extendedKeyUsages )
        {
            ValueBuilder<ExtendedKeyUsagesValue> ekuBuilder = vbf.newValueBuilder( ExtendedKeyUsagesValue.class );
            ExtendedKeyUsagesValue ekuValue = ekuBuilder.prototype();
            ekuValue.critical().set( critical );
            ekuValue.extendedKeyUsages().set( extendedKeyUsages );
            return ekuBuilder.newInstance();
        }

        @Override
        public NetscapeCertTypesValue buildNetscapeCertTypesValue( boolean critical, Set<NetscapeCertType> netscapeCertTypes )
        {
            ValueBuilder<NetscapeCertTypesValue> nctBuilder = vbf.newValueBuilder( NetscapeCertTypesValue.class );
            NetscapeCertTypesValue nctValue = nctBuilder.prototype();
            nctValue.critical().set( critical );
            nctValue.netscapeCertTypes().set( netscapeCertTypes );
            return nctBuilder.newInstance();
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
            akiValue.names().set( buildSetOfGeneralNames( x509ExtReader.asMap( authKeyId.getAuthorityCertIssuer() ) ) );
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

        @SuppressWarnings( "SetReplaceableByEnumSet" )
        private CRLDistributionPointsValue buildCRLDistributionPointsValue( boolean critical, DistributionPoint[] crlDistPoints )
        {
            ValueBuilder<CRLDistributionPointsValue> cdpBuilder = vbf.newValueBuilder( CRLDistributionPointsValue.class );
            CRLDistributionPointsValue cdpValue = cdpBuilder.prototype();
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
                if ( eachPoint.getReasons() != null ) {
                    reasons.addAll( x509ExtReader.getRevocationReasons( eachPoint.getReasons() ) );
                }
                if ( eachPoint.getCRLIssuer() != null ) {
                    issuerNames.addAll( buildSetOfGeneralNames( x509ExtReader.asMap( eachPoint.getCRLIssuer() ) ) );
                }
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
            an.alternativeNames().set( buildSetOfGeneralNames( altNames ) );
            return anBuilder.newInstance();
        }

        @Override
        public BasicConstraintsValue buildBasicConstraintsValue( boolean critical, boolean subjectIsCA, long pathLenConstraint )
        {
            ValueBuilder<BasicConstraintsValue> bcBuilder = vbf.newValueBuilder( BasicConstraintsValue.class );
            BasicConstraintsValue bcValue = bcBuilder.prototype();
            bcValue.critical().set( critical );
            bcValue.subjectIsCA().set( subjectIsCA );
            bcValue.pathLengthConstraint().set( pathLenConstraint );
            return bcBuilder.newInstance();
        }

        private NameConstraintsValue buildNameConstraintsValue( boolean critical, NameConstraints nameConstraints )
        {
            LinkedHashSet<X509GeneralSubtreeValue> permittedSubtrees = new LinkedHashSet<X509GeneralSubtreeValue>();
            LinkedHashSet<X509GeneralSubtreeValue> excludedSubtrees = new LinkedHashSet<X509GeneralSubtreeValue>();
            for ( int idx = 0; idx < nameConstraints.getPermittedSubtrees().size(); idx++ ) {
                GeneralSubtree eachSubtree = ( GeneralSubtree ) nameConstraints.getPermittedSubtrees().getObjectAt( idx );
                permittedSubtrees.add( buildGeneralSubtree( eachSubtree ) );
            }
            for ( int idx = 0; idx < nameConstraints.getExcludedSubtrees().size(); idx++ ) {
                GeneralSubtree eachSubtree = ( GeneralSubtree ) nameConstraints.getExcludedSubtrees().getObjectAt( idx );
                excludedSubtrees.add( buildGeneralSubtree( eachSubtree ) );
            }
            return buildNameConstraintsValue( critical, permittedSubtrees, excludedSubtrees );
        }

        @Override
        public NameConstraintsValue buildNameConstraintsValue( boolean critical, Set<X509GeneralSubtreeValue> permittedSubtrees, Set<X509GeneralSubtreeValue> excludedSubtrees )
        {
            ValueBuilder<NameConstraintsValue> ncBuilder = vbf.newValueBuilder( NameConstraintsValue.class );
            NameConstraintsValue ncValue = ncBuilder.prototype();
            ncValue.critical().set( critical );
            ncValue.permittedSubtrees().set( permittedSubtrees );
            ncValue.excludedSubtrees().set( excludedSubtrees );
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

        private X509GeneralSubtreeValue buildGeneralSubtree( GeneralSubtree subtree )
        {
            Map.Entry<X509GeneralName, String> baseName = x509ExtReader.asImmutableMapEntry( subtree.getBase() );
            return buildGeneralSubtree( baseName.getKey(), baseName.getValue(), subtree.getMinimum().longValue(), subtree.getMaximum().longValue() );
        }

        @Override
        public X509GeneralSubtreeValue buildGeneralSubtree( X509GeneralName nameType, String nameValue, Long subtreeMinimum, Long subtreeMaximum )
        {
            ValueBuilder<X509GeneralSubtreeValue> subtreeValueBuilder = vbf.newValueBuilder( X509GeneralSubtreeValue.class );
            X509GeneralSubtreeValue subtreeValue = subtreeValueBuilder.prototype();
            subtreeValue.base().set( buildGeneralName( nameType, nameValue ) );
            subtreeValue.minimum().set( subtreeMinimum );
            subtreeValue.maximum().set( subtreeMaximum );
            return subtreeValueBuilder.newInstance();
        }

        private Set<X509GeneralNameValue> buildSetOfGeneralNames( Map<X509GeneralName, String> names )
        {
            Set<X509GeneralNameValue> setOfNames = new LinkedHashSet<X509GeneralNameValue>();
            for ( Map.Entry<X509GeneralName, String> eachName : names.entrySet() ) {
                setOfNames.add( buildGeneralName( eachName ) );
            }
            return setOfNames;
        }

        private X509GeneralNameValue buildGeneralName( Map.Entry<X509GeneralName, String> entry )
        {
            return buildGeneralName( entry.getKey(), entry.getValue() );
        }

        @Override
        public X509GeneralNameValue buildGeneralName( X509GeneralName nameType, String nameValue )
        {
            ValueBuilder<X509GeneralNameValue> nameBuilder = vbf.newValueBuilder( X509GeneralNameValue.class );
            X509GeneralNameValue name = nameBuilder.prototype();
            name.nameType().set( nameType );
            name.nameValue().set( nameValue );
            return nameBuilder.newInstance();
        }

    }

}
