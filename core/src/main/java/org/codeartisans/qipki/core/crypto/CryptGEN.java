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
package org.codeartisans.qipki.core.crypto;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Vector;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.Attribute;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.codeartisans.qipki.core.QiPkiFailure;
import org.codeartisans.qipki.core.crypto.constants.AsymetricAlgorithm;
import org.codeartisans.qipki.core.crypto.constants.SignatureAlgorithm;
import org.codeartisans.qipki.core.crypto.constants.TimeRelated;
import org.joda.time.DateTime;
import org.joda.time.Duration;

public class CryptGEN
{

    public KeyPair generateRSAKeyPair( int size )
    {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance( AsymetricAlgorithm.RSA, BouncyCastleProvider.PROVIDER_NAME );
            keyGen.initialize( size );
            return keyGen.generateKeyPair();
        } catch ( GeneralSecurityException ex ) {
            throw new QiPkiFailure( "Unable to generate RSA KeyPair", ex );
        }
    }

    public PKCS10CertificationRequest generatePKCS10( X500Principal distinguishedName, KeyPair keyPair )
    {
        try {
            return new PKCS10CertificationRequest( SignatureAlgorithm.SHA256withRSA,
                                                   distinguishedName, keyPair.getPublic(),
                                                   null,
                                                   keyPair.getPrivate(),
                                                   BouncyCastleProvider.PROVIDER_NAME );
        } catch ( GeneralSecurityException ex ) {
            throw new QiPkiFailure( "Unable to generate PKCS#10", ex );
        }
    }

    public PKCS10CertificationRequest generatePKCS10( X500Principal distinguishedName, KeyPair keyPair, GeneralNames subjectAlternativeNames )
    {
        try {
            return new PKCS10CertificationRequest( SignatureAlgorithm.SHA256withRSA,
                                                   distinguishedName, keyPair.getPublic(),
                                                   generateSANAttribute( subjectAlternativeNames ),
                                                   keyPair.getPrivate(),
                                                   BouncyCastleProvider.PROVIDER_NAME );
        } catch ( GeneralSecurityException ex ) {
            throw new QiPkiFailure( "Unable to generate PKCS#10", ex );
        }
    }

    public X509Certificate generateX509Certificate( PrivateKey privateKey,
                                                    X500Principal issuerDN,
                                                    BigInteger serialNumber,
                                                    X509Name subjectDN,
                                                    PublicKey publicKey,
                                                    Duration validity,
                                                    X509Extensions x509Extensions )
    {
        try {

            X509V3CertificateGenerator x509v3Generator = new X509V3CertificateGenerator();

            DateTime now = new DateTime();

            x509v3Generator.setSerialNumber( serialNumber );
            x509v3Generator.setSubjectDN( subjectDN );
            x509v3Generator.setIssuerDN( issuerDN );
            x509v3Generator.setNotBefore( now.minus( TimeRelated.CLOCK_SKEW_DURATION ).toDate() );
            x509v3Generator.setNotAfter( now.plus( validity ).minus( TimeRelated.CLOCK_SKEW_DURATION ).toDate() );
            x509v3Generator.setSignatureAlgorithm( SignatureAlgorithm.SHA256withRSA );
            x509v3Generator.setPublicKey( publicKey );

            if ( x509Extensions != null ) {
                // Adding requested X509Extensions
                Enumeration oids = x509Extensions.oids();
                while ( oids.hasMoreElements() ) {
                    DERObjectIdentifier eachOid = ( DERObjectIdentifier ) oids.nextElement();
                    X509Extension eachExtension = x509Extensions.getExtension( eachOid );
                    x509v3Generator.addExtension( eachOid,
                                                  eachExtension.isCritical(),
                                                  X509Extension.convertValueToObject( eachExtension ) );
                }
            }

            return x509v3Generator.generate( privateKey, BouncyCastleProvider.PROVIDER_NAME );

        } catch ( GeneralSecurityException ex ) {
            throw new QiPkiFailure( "Unable to generate X509Certificate", ex );
        } catch ( IllegalStateException ex ) {
            throw new QiPkiFailure( "Unable to generate X509Certificate", ex );
        }
    }

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

