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
package org.codeartisans.qipki.crypto.x509;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.misc.MiscObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.CertificationRequestInfo;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.Attribute;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.NameConstraints;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.bouncycastle.asn1.x509.PrivateKeyUsagePeriod;
import org.bouncycastle.asn1.x509.ReasonFlags;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.codeartisans.qipki.crypto.QiCryptoFailure;
import org.codeartisans.qipki.crypto.codec.CryptCodex;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.qi4j.api.injection.scope.Service;

public class X509ExtensionsReaderImpl
        implements X509ExtensionsReader
{

    private final CryptCodex cryptCodex;

    public X509ExtensionsReaderImpl( @Service CryptCodex cryptCodex )
    {
        this.cryptCodex = cryptCodex;
    }

    @Override
    public X509Extensions extractRequestedExtensions( PKCS10CertificationRequest pkcs10 )
    {
        final CertificationRequestInfo certificationRequestInfo = pkcs10.getCertificationRequestInfo();
        final ASN1Set attributesAsn1Set = certificationRequestInfo.getAttributes();
        if ( attributesAsn1Set == null ) {
            return null;
        }
        // The `Extension Request` attribute is contained within an ASN.1 Set,
        // usually as the first element.
        X509Extensions certificateRequestExtensions = null;
        for ( int i = 0; i < attributesAsn1Set.size(); ++i ) {
            // There should be only only one attribute in the set. (that is, only
            // the `Extension Request`, but loop through to find it properly)
            final DEREncodable derEncodable = attributesAsn1Set.getObjectAt( i );
            if ( derEncodable instanceof DERSequence ) {
                final Attribute attribute = new Attribute( ( DERSequence ) attributesAsn1Set.getObjectAt( i ) );

                if ( attribute.getAttrType().equals( PKCSObjectIdentifiers.pkcs_9_at_extensionRequest ) ) {
                    // The `Extension Request` attribute is present.
                    final ASN1Set attributeValues = attribute.getAttrValues();

                    // The X509Extensions are contained as a value of the ASN.1 Set.
                    // Assume that it is the first value of the set.
                    if ( attributeValues.size() >= 1 ) {
                        DEREncodable extensionsDEREncodable = attributeValues.getObjectAt( 0 );
                        ASN1Sequence extensionsASN1Sequence = ( ASN1Sequence ) extensionsDEREncodable;

                        certificateRequestExtensions = new X509Extensions( extensionsASN1Sequence );

                        Enumeration<?> e = certificateRequestExtensions.oids();
                        while ( e.hasMoreElements() ) {
                            DERObjectIdentifier oid = ( DERObjectIdentifier ) e.nextElement();
                            X509Extension ext = certificateRequestExtensions.getExtension( oid );
                            boolean critical = ext.isCritical();
                            ASN1OctetString value = ext.getValue();
                        }

                        // No need to search any more.
                        break;
                    }
                }
            }
        }
        return certificateRequestExtensions;
    }

    @Override
    public AuthorityKeyIdentifier getAuthorityKeyIdentifier( X509Certificate cert )
    {
        try {
            byte[] value = cert.getExtensionValue( X509Extensions.AuthorityKeyIdentifier.getId() );
            if ( value == null ) {
                return null;
            }
            return AuthorityKeyIdentifier.getInstance( ASN1Object.fromByteArray( value ) );
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to extract AuthorityKeyIdentifier from X509Certificate extensions", ex );
        }
    }

    @Override
    public byte[] getSubjectKeyIdentifier( X509Certificate cert )
    {
        try {
            byte[] value = cert.getExtensionValue( X509Extensions.SubjectKeyIdentifier.getId() );
            if ( value == null ) {
                return null;
            }
            return SubjectKeyIdentifier.getInstance( ASN1Object.fromByteArray( value ) ).getKeyIdentifier();
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to extract SubjectKeyIdentifier from X509Certificate extensions", ex );
        }
    }

    @Override
    @SuppressWarnings( "SetReplaceableByEnumSet" )
    public Set<KeyUsage> getKeyUsages( X509Certificate cert )
    {
        try {
            byte[] value = cert.getExtensionValue( X509Extensions.KeyUsage.getId() );
            if ( value == null ) {
                return Collections.emptySet();
            }
            int usages = org.bouncycastle.asn1.x509.KeyUsage.getInstance( ASN1Object.fromByteArray( value ) ).intValue();
            Set<KeyUsage> keyUsages = new LinkedHashSet<KeyUsage>();
            for ( KeyUsage eachPossibleUsage : KeyUsage.values() ) {
                if ( ( usages & eachPossibleUsage.usage() ) == eachPossibleUsage.usage() ) {
                    keyUsages.add( eachPossibleUsage );
                }
            }
            return keyUsages;
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to extract KeyUsages from X509Certificate extensions", ex );
        }
    }

    @Override
    @SuppressWarnings( "SetReplaceableByEnumSet" )
    public Set<ExtendedKeyUsage> getExtendedKeyUsages( X509Certificate cert )
    {
        try {
            byte[] value = cert.getExtensionValue( X509Extensions.ExtendedKeyUsage.getId() );
            if ( value == null ) {
                return Collections.emptySet();
            }
            org.bouncycastle.asn1.x509.ExtendedKeyUsage usages = org.bouncycastle.asn1.x509.ExtendedKeyUsage.getInstance( ASN1Object.fromByteArray( value ) );
            Set<ExtendedKeyUsage> keyUsages = new LinkedHashSet<ExtendedKeyUsage>();
            for ( ExtendedKeyUsage eachPossible : ExtendedKeyUsage.values() ) {
                if ( usages.hasKeyPurposeId( eachPossible.getKeyPurposeId() ) ) {
                    keyUsages.add( eachPossible );
                }
            }
            return keyUsages;
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to extract ExtendedKeyUsages from X509Certificate extensions", ex );
        }
    }

    @Override
    @SuppressWarnings( "SetReplaceableByEnumSet" )
    public Set<NetscapeCertType> getNetscapeCertTypes( X509Certificate cert )
    {
        try {
            byte[] value = cert.getExtensionValue( MiscObjectIdentifiers.netscapeCertType.getId() );
            if ( value == null ) {
                return Collections.emptySet();
            }
            int nctIntValue = new org.bouncycastle.asn1.misc.NetscapeCertType( ( DERBitString ) ASN1Object.fromByteArray( value ) ).intValue();
            Set<NetscapeCertType> netscapeCertTypes = new LinkedHashSet<NetscapeCertType>();
            for ( NetscapeCertType eachCertType : NetscapeCertType.values() ) {
                if ( ( nctIntValue & eachCertType.getIntValue() ) == eachCertType.getIntValue() ) {
                    netscapeCertTypes.add( eachCertType );
                }
            }
            return netscapeCertTypes;
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to extract NetscapeCertType from X509Certificate extensions", ex );
        }
    }

    @Override
    public String getNetscapeCertComment( X509Certificate cert )
    {
        try {
            byte[] value = cert.getExtensionValue( MiscObjectIdentifiers.netscapeCertComment.getId() );
            if ( value == null ) {
                return null;
            }
            return ASN1Object.fromByteArray( value ).toString();
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to extract NetscapeCertComment from X509Certificate extensions", ex );
        }
    }

    @Override
    public Interval getPrivateKeyUsagePeriod( X509Certificate cert )
    {
        try {
            byte[] value = cert.getExtensionValue( X509Extensions.PrivateKeyUsagePeriod.getId() );
            if ( value == null ) {
                return null;
            }
            PrivateKeyUsagePeriod privKeyUsagePeriod = PrivateKeyUsagePeriod.getInstance( ASN1Object.fromByteArray( value ) );
            SimpleDateFormat derDateFormat = new SimpleDateFormat( "yyyyMMddHHmmssz" );
            Date notBefore = derDateFormat.parse( privKeyUsagePeriod.getNotBefore().getTime() );
            Date notAfter = derDateFormat.parse( privKeyUsagePeriod.getNotAfter().getTime() );
            return new Interval( new DateTime( notBefore ), new DateTime( notAfter ) );
        } catch ( ParseException ex ) {
            throw new QiCryptoFailure( "Unable to extract PrivateKeyUsagePeriod from X509Certificate extensions", ex );
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to extract PrivateKeyUsagePeriod from X509Certificate extensions", ex );
        }
    }

    @Override
    public DistributionPoint[] getCRLDistributionPoints( X509Certificate cert )
    {
        try {
            byte[] value = cert.getExtensionValue( X509Extensions.CRLDistributionPoints.getId() );
            if ( value == null ) {
                return null;
            }
            CRLDistPoint crlDistPoints = CRLDistPoint.getInstance( ASN1Object.fromByteArray( value ) );
            return crlDistPoints.getDistributionPoints();
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to extract CRLDistributionPoints from X509Certificate extensions", ex );
        }

    }

    @Override
    public Set<PolicyInformation> getCertificatePolicies( X509Certificate cert )
    {
        try {
            byte[] value = cert.getExtensionValue( X509Extensions.CertificatePolicies.getId() );
            if ( value == null ) {
                return Collections.emptySet();
            }
            ASN1Sequence policiesSequence = ( ASN1Sequence ) ASN1Object.fromByteArray( value );
            Set<PolicyInformation> certPolicies = new LinkedHashSet<PolicyInformation>();
            for ( int idx = 0; idx < policiesSequence.size(); idx++ ) {
                PolicyInformation policy = PolicyInformation.getInstance( policiesSequence.getObjectAt( idx ) );
                certPolicies.add( policy );
            }
            return certPolicies;
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to extract CertificatePolicies from X509Certificate extensions", ex );
        }
    }

    // See swisscom_root_ca_1 in cacerts for an example
    @Override
    public Set<PolicyMapping> getPolicyMappings( X509Certificate cert )
    {
        try {
            byte[] value = cert.getExtensionValue( X509Extensions.PolicyMappings.getId() );
            if ( value == null ) {
                return Collections.emptySet();
            }
            ASN1Sequence mappingsSequence = ( ASN1Sequence ) ASN1Object.fromByteArray( value );
            Set<PolicyMapping> mappings = new LinkedHashSet<PolicyMapping>();
            for ( int idx = 0; idx < mappingsSequence.size(); idx++ ) {
                ASN1Sequence seq = ( ASN1Sequence ) mappingsSequence.getObjectAt( idx );
                PolicyMapping mapping = new PolicyMapping();
                if ( seq.size() > 0 ) {
                    mapping.setIssuerDomainPolicyOID( ( ( DERObjectIdentifier ) seq.getObjectAt( 0 ) ).getId() );
                }
                if ( seq.size() > 1 ) {
                    mapping.setIssuerDomainPolicyOID( ( ( DERObjectIdentifier ) seq.getObjectAt( 1 ) ).getId() );
                }
                mappings.add( mapping );
            }
            return mappings;
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to extract PolicyMappings from X509Certificate extensions", ex );
        }
    }

    @Override
    public GeneralNames getSubjectAlternativeNames( X509Certificate cert )
    {
        try {
            byte[] value = cert.getExtensionValue( X509Extensions.SubjectAlternativeName.getId() );
            if ( value == null ) {
                return null;
            }
            return GeneralNames.getInstance( ASN1Object.fromByteArray( ( ( ASN1OctetString ) ASN1Object.fromByteArray( value ) ).getOctets() ) );
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to extract SubjectAlternativeName from X509Certificate extensions", ex );
        }
    }

    @Override
    public GeneralNames getIssuerAlternativeNames( X509Certificate cert )
    {
        try {
            byte[] value = cert.getExtensionValue( X509Extensions.IssuerAlternativeName.getId() );
            if ( value == null ) {
                return null;
            }
            return GeneralNames.getInstance( ASN1Object.fromByteArray( ( ( ASN1OctetString ) ASN1Object.fromByteArray( value ) ).getOctets() ) );
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to extract IssuerAlternativeName from X509Certificate extensions", ex );
        }
    }

    @Override
    public BasicConstraints getBasicConstraints( X509Certificate cert )
    {
        try {
            byte[] value = cert.getExtensionValue( X509Extensions.BasicConstraints.getId() );
            if ( value == null ) {
                return null;
            }
            return BasicConstraints.getInstance( ASN1Object.fromByteArray( value ) );
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to extract BasicConstraints from X509Certificate extensions", ex );
        }
    }

    @Override
    public NameConstraints getNameConstraints( X509Certificate cert )
    {
        try {
            byte[] value = cert.getExtensionValue( X509Extensions.NameConstraints.getId() );
            if ( value == null ) {
                return null;
            }
            return new NameConstraints( ( ASN1Sequence ) ASN1Object.fromByteArray( value ) );
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to extract NameConstraints from X509Certificate extensions", ex );
        }
    }

    @Override
    public Set<PolicyConstraint> getPolicyConstraints( X509Certificate cert )
    {
        try {
            byte[] value = cert.getExtensionValue( X509Extensions.PolicyConstraints.getId() );
            if ( value == null ) {
                return Collections.emptySet();
            }
            ASN1Sequence constraintsSequence = ( ASN1Sequence ) ASN1Object.fromByteArray( value );
            Set<PolicyConstraint> constraints = new LinkedHashSet<PolicyConstraint>();
            for ( int idx = 0; idx < constraintsSequence.size(); idx++ ) {
                DERTaggedObject asn1Constraint = ( DERTaggedObject ) constraintsSequence.getObjectAt( idx );
                DERInteger skipCerts = new DERInteger( ( ( DEROctetString ) asn1Constraint.getObject() ).getOctets() );
                PolicyConstraint constraint = new PolicyConstraint();
                switch ( asn1Constraint.getTagNo() ) {
                    case 0:
                        constraint.setRequireExplicitPolicy( skipCerts.getValue().intValue() );
                        break;
                    case 1:
                        constraint.setInhibitPolicyMapping( skipCerts.getValue().intValue() );
                }
                constraints.add( constraint );
            }
            return constraints;
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to extract PolicyConstraints from X509Certificate extensions", ex );
        }
    }

    @Override
    @SuppressWarnings( "SetReplaceableByEnumSet" )
    public Set<RevocationReason> getRevocationReasons( ReasonFlags reasonFlags )
    {
        if ( reasonFlags == null ) {
            return Collections.emptySet();
        }
        int reasons = reasonFlags.intValue();
        Set<RevocationReason> revocationReasons = new LinkedHashSet<RevocationReason>();
        for ( RevocationReason eachPossibleReason : RevocationReason.values() ) {
            if ( ( reasons & eachPossibleReason.reason() ) == eachPossibleReason.reason() ) {
                revocationReasons.add( eachPossibleReason );
            }
        }
        return revocationReasons;
    }

    @Override
    @SuppressWarnings( "MapReplaceableByEnumMap" )
    public Map<X509GeneralName, String> asMap( GeneralNames generalNames )
    {
        if ( generalNames == null ) {
            return Collections.emptyMap();
        }
        Map<X509GeneralName, String> map = new LinkedHashMap<X509GeneralName, String>();
        for ( GeneralName eachGeneralName : generalNames.getNames() ) {
            Map.Entry<X509GeneralName, String> entry = asImmutableMapEntry( eachGeneralName );
            map.put( entry.getKey(), entry.getValue() );
        }
        return map;
    }

    @Override
    public Map.Entry<X509GeneralName, String> asImmutableMapEntry( GeneralName generalName )
    {
        int nameType = generalName.getTagNo();
        X509GeneralName x509GeneralName = null;
        String value = null;
        switch ( nameType ) {
            case GeneralName.otherName:
                ASN1Sequence otherName = ( ASN1Sequence ) generalName.getName();
                // String oid = ( ( DERObjectIdentifier ) otherName.getObjectAt( 0 ) ).getId();
                x509GeneralName = X509GeneralName.otherName;
                value = cryptCodex.toString( otherName.getObjectAt( 1 ) );
                break;
            case GeneralName.rfc822Name:
                x509GeneralName = X509GeneralName.rfc822Name;
                value = generalName.getName().toString();
                break;
            case GeneralName.dNSName:
                x509GeneralName = X509GeneralName.dNSName;
                value = generalName.getName().toString();
                break;
            case GeneralName.registeredID:
                x509GeneralName = X509GeneralName.registeredID;
                value = generalName.getName().toString();
                break;
            case GeneralName.x400Address:
                x509GeneralName = X509GeneralName.x400Address;
                value = generalName.getName().toString();
                break;
            case GeneralName.ediPartyName:
                x509GeneralName = X509GeneralName.ediPartyName;
                value = generalName.getName().toString();
                break;
            case GeneralName.directoryName:
                x509GeneralName = X509GeneralName.directoryName;
                value = new X500Principal( ( ( X509Name ) generalName.getName() ).toString() ).getName( X500Principal.CANONICAL );
                break;
            case GeneralName.uniformResourceIdentifier:
                x509GeneralName = X509GeneralName.uniformResourceIdentifier;
                value = generalName.getName().toString();
                break;
            case GeneralName.iPAddress: // What about IPv6 addresses ?
                ASN1OctetString iPAddress = ( ASN1OctetString ) generalName.getName();
                byte[] iPAddressBytes = iPAddress.getOctets();
                StringBuilder sb = new StringBuilder();
                for ( int idx = 0; idx < iPAddressBytes.length; idx++ ) {
                    sb.append( iPAddressBytes[idx] & 0xFF );
                    if ( idx + 1 < iPAddressBytes.length ) {
                        sb.append( "." );
                    }
                }
                x509GeneralName = X509GeneralName.iPAddress;
                value = sb.toString();
                break;
            default:
                x509GeneralName = X509GeneralName.unknownGeneralName;
                value = generalName.getName().toString();
        }
        final X509GeneralName finalName = x509GeneralName;
        final String finalValue = value;
        return new Map.Entry<X509GeneralName, String>()
        {

            @Override
            public X509GeneralName getKey()
            {
                return finalName;
            }

            @Override
            public String getValue()
            {
                return finalValue;
            }

            @Override
            public String setValue( String value )
            {
                throw new UnsupportedOperationException( "This Map.Entry is immutable." );
            }

        };
    }

}
