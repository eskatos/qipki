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
package org.codeartisans.qipki.crypto.x509;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Vector;

import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.Attribute;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import org.codeartisans.qipki.crypto.QiCryptoFailure;
import org.codeartisans.qipki.crypto.algorithms.SignatureAlgorithm;
import org.codeartisans.qipki.crypto.constants.Time;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class X509GeneratorImpl
        implements X509Generator
{

    @Override
    public PKCS10CertificationRequest generatePKCS10( DistinguishedName distinguishedName, KeyPair keyPair )
    {
        try {
            return new PKCS10CertificationRequest( SignatureAlgorithm.SHA256withRSA.jcaString(),
                                                   distinguishedName.toX500Principal(), keyPair.getPublic(),
                                                   null,
                                                   keyPair.getPrivate(),
                                                   BouncyCastleProvider.PROVIDER_NAME );
        } catch ( GeneralSecurityException ex ) {
            throw new QiCryptoFailure( "Unable to generate PKCS#10", ex );
        }
    }

    @Override
    public PKCS10CertificationRequest generatePKCS10( DistinguishedName distinguishedName, KeyPair keyPair, GeneralNames subjectAlternativeNames )
    {
        try {
            return new PKCS10CertificationRequest( SignatureAlgorithm.SHA256withRSA.jcaString(),
                                                   distinguishedName.toX500Principal(), keyPair.getPublic(),
                                                   generateSANAttribute( subjectAlternativeNames ),
                                                   keyPair.getPrivate(),
                                                   BouncyCastleProvider.PROVIDER_NAME );
        } catch ( GeneralSecurityException ex ) {
            throw new QiCryptoFailure( "Unable to generate PKCS#10", ex );
        }
    }

    @Override
    public X509Certificate generateX509Certificate( PrivateKey privateKey,
                                                    DistinguishedName issuerDN,
                                                    BigInteger serialNumber,
                                                    DistinguishedName subjectDN,
                                                    PublicKey publicKey,
                                                    Duration validity,
                                                    List<X509ExtensionHolder> x509Extensions )
    {
        try {

            X509V3CertificateGenerator x509v3Generator = new X509V3CertificateGenerator();

            DateTime now = new DateTime();

            x509v3Generator.setSerialNumber( serialNumber );
            x509v3Generator.setSubjectDN( subjectDN.toX500Principal() );
            x509v3Generator.setIssuerDN( issuerDN.toX500Principal() );
            x509v3Generator.setNotBefore( now.minus( Time.CLOCK_SKEW ).toDate() );
            x509v3Generator.setNotAfter( now.plus( validity ).minus( Time.CLOCK_SKEW ).toDate() );
            x509v3Generator.setSignatureAlgorithm( SignatureAlgorithm.SHA256withRSA.jcaString() );
            x509v3Generator.setPublicKey( publicKey );

            for ( X509ExtensionHolder eachExtensionHolder : x509Extensions ) {
                x509v3Generator.addExtension( eachExtensionHolder.getDerOID(), eachExtensionHolder.isCritical(), eachExtensionHolder.getValue() );
            }

            return x509v3Generator.generate( privateKey, BouncyCastleProvider.PROVIDER_NAME );

        } catch ( GeneralSecurityException ex ) {
            throw new QiCryptoFailure( "Unable to generate X509Certificate", ex );
        } catch ( IllegalStateException ex ) {
            throw new QiCryptoFailure( "Unable to generate X509Certificate", ex );
        }
    }

    @SuppressWarnings( { "UseOfObsoleteCollectionType", "unchecked" } )
    private DERSet generateSANAttribute( GeneralNames subGeneralNames )
    {
        if ( subGeneralNames == null ) {
            return new DERSet();
        }
        Vector oids = new Vector();
        Vector values = new Vector();
        oids.add( X509Extensions.SubjectAlternativeName );
        values.add( new X509Extension( false, new DEROctetString( subGeneralNames ) ) );
        X509Extensions extensions = new X509Extensions( oids, values );
        Attribute attribute = new Attribute( PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, new DERSet( extensions ) );
        return new DERSet( attribute );
    }

}
