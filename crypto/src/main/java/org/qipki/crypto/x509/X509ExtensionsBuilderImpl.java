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
package org.qipki.crypto.x509;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.misc.NetscapeCertType;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Name;

import org.qipki.crypto.QiCryptoFailure;

public class X509ExtensionsBuilderImpl
        implements X509ExtensionsBuilder
{

    @Override
    public SubjectKeyIdentifier buildSubjectKeyIdentifier( PublicKey publicKey )
    {
        try {
            ByteArrayInputStream octets = new ByteArrayInputStream( publicKey.getEncoded() );
            SubjectPublicKeyInfo spki = new SubjectPublicKeyInfo( ( ASN1Sequence ) new ASN1InputStream( octets ).readObject() );
            return new SubjectKeyIdentifier( spki );
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to build SubjectKeyIdentifier", ex );
        }
    }

    @Override
    public AuthorityKeyIdentifier buildAuthorityKeyIdentifier( PublicKey publicKey )
    {
        try {
            ByteArrayInputStream octets = new ByteArrayInputStream( publicKey.getEncoded() );
            SubjectPublicKeyInfo apki = new SubjectPublicKeyInfo( ( ASN1Sequence ) new ASN1InputStream( octets ).readObject() );
            return new AuthorityKeyIdentifier( apki );
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to build AuthorityKeyIdentifier", ex );
        }
    }

    @Override
    public BasicConstraints buildNonCABasicConstraints()
    {
        return new BasicConstraints( false );
    }

    @Override
    public BasicConstraints buildCABasicConstraints( Long pathLen )
    {
        return new BasicConstraints( pathLen.intValue() );
    }

    @Override
    public KeyUsage buildKeyUsages( Set<org.qipki.crypto.x509.KeyUsage> keyUsages )
    {
        return new KeyUsage( org.qipki.crypto.x509.KeyUsage.usage( keyUsages ) );
    }

    @Override
    public ExtendedKeyUsage buildExtendedKeyUsage( Set<org.qipki.crypto.x509.ExtendedKeyUsage> extendedKeyUsages )
    {
        return new ExtendedKeyUsage( org.qipki.crypto.x509.ExtendedKeyUsage.usage( extendedKeyUsages ) );
    }

    @Override
    public NetscapeCertType buildNetscapeCertTypes( Set<org.qipki.crypto.x509.NetscapeCertType> netscapeCertTypes )
    {
        return new NetscapeCertType( org.qipki.crypto.x509.NetscapeCertType.certTypes( netscapeCertTypes ) );
    }

    @Override
    public CRLDistPoint buildCRLDistributionPoints( X500Principal issuer, String... crlUris )
    {
        Map<X500Principal, Iterable<String>> crlDistPointsData = new HashMap<X500Principal, Iterable<String>>();
        crlDistPointsData.put( issuer, Arrays.asList( crlUris ) );
        return buildCRLDistributionPoints( crlDistPointsData );
    }

    @Override
    public CRLDistPoint buildCRLDistributionPoints( Map<X500Principal, Iterable<String>> crlDistPointsData )
    {
        List<DistributionPoint> distributionPoints = new ArrayList<DistributionPoint>();
        for ( Map.Entry<X500Principal, Iterable<String>> eachIssuerEntry : crlDistPointsData.entrySet() ) {

            GeneralName issuerName = new GeneralName( new X509Name( eachIssuerEntry.getKey().getName() ) );
            ASN1EncodableVector issuerVector = new ASN1EncodableVector();
            issuerVector.add( issuerName );
            GeneralNames issuerNames = new GeneralNames( new DERSequence( issuerVector ) );

            for ( String eachEndpoint : eachIssuerEntry.getValue() ) {

                GeneralName endpointName = new GeneralName( GeneralName.uniformResourceIdentifier, new DERIA5String( eachEndpoint ) );
                ASN1EncodableVector epVector = new ASN1EncodableVector();
                epVector.add( endpointName );
                GeneralNames endpointNames = new GeneralNames( new DERSequence( epVector ) );
                DistributionPointName dpn = new DistributionPointName( DistributionPointName.FULL_NAME, endpointNames );

                distributionPoints.add( new DistributionPoint( dpn, null, issuerNames ) );
            }
        }
        return new CRLDistPoint( distributionPoints.toArray( new DistributionPoint[ distributionPoints.size() ] ) );
    }

}
