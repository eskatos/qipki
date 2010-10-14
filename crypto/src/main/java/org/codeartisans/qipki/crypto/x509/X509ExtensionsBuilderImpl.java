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

import org.codeartisans.qipki.crypto.QiCryptoFailure;

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
    public KeyUsage buildKeyUsages( Set<org.codeartisans.qipki.crypto.x509.KeyUsage> keyUsages )
    {
        return new KeyUsage( org.codeartisans.qipki.crypto.x509.KeyUsage.usage( keyUsages ) );
    }

    @Override
    public ExtendedKeyUsage buildExtendedKeyUsage( Set<org.codeartisans.qipki.crypto.x509.ExtendedKeyUsage> extendedKeyUsages )
    {
        return new ExtendedKeyUsage( org.codeartisans.qipki.crypto.x509.ExtendedKeyUsage.usage( extendedKeyUsages ) );
    }

    @Override
    public NetscapeCertType buildNetscapeCertTypes( Set<org.codeartisans.qipki.crypto.x509.NetscapeCertType> netscapeCertTypes )
    {
        return new NetscapeCertType( org.codeartisans.qipki.crypto.x509.NetscapeCertType.certTypes( netscapeCertTypes ) );
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
