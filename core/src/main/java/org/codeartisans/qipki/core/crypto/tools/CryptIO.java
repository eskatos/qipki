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
package org.codeartisans.qipki.core.crypto.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchProviderException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.CertificationRequestInfo;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.Attribute;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.x509.extension.X509ExtensionUtil;
import org.codeartisans.qipki.core.constants.IOConstants;
import org.codeartisans.qipki.commons.constants.KeyStoreType;
import org.codeartisans.qipki.core.QiPkiFailure;

public class CryptIO
{

    public KeyStore createEmptyKeyStore( KeyStoreType storeType )
    {
        try {
            KeyStore keystore = getKeyStoreInstance( storeType );
            keystore.load( null, null );
            return keystore;
        } catch ( IOException ex ) {
            throw new QiPkiFailure( "Unable to create empty" + storeType + " KeyStore", ex );
        } catch ( GeneralSecurityException ex ) {
            throw new QiPkiFailure( "Unable to create empty" + storeType + " KeyStore", ex );
        }
    }

    public String base64Encode( KeyStore keystore, char[] password )
    {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            keystore.store( baos, password );
            baos.flush();
            return new String( Base64.encode( baos.toByteArray() ), IOConstants.UTF_8 );
        } catch ( IOException ex ) {
            throw new QiPkiFailure( "Unable to Base64 encode KeyStore", ex );
        } catch ( GeneralSecurityException ex ) {
            throw new QiPkiFailure( "Unable to Base64 encode KeyStore", ex );
        }
    }

    public KeyStore base64DecodeKeyStore( String payload, KeyStoreType storeType, char[] password )
    {
        try {
            KeyStore keystore = getKeyStoreInstance( storeType );
            keystore.load( new ByteArrayInputStream( Base64.decode( payload.getBytes( IOConstants.UTF_8 ) ) ), password );
            return keystore;
        } catch ( IOException ex ) {
            throw new QiPkiFailure( "Unable to Base64 decode KeyStore", ex );
        } catch ( GeneralSecurityException ex ) {
            throw new QiPkiFailure( "Unable to Base64 decode KeyStore", ex );
        }
    }

    public PKCS10CertificationRequest readPKCS10PEM( Reader reader )
    {
        try {
            return ( PKCS10CertificationRequest ) new PEMReader( reader ).readObject();
        } catch ( IOException ex ) {
            throw new IllegalArgumentException( "Unable to read PKCS#10 from PEM", ex );
        }
    }

    public CharSequence asPEM( X509Certificate certificate )
    {
        return asPEM( certificate.getClass().getSimpleName(), certificate );
    }

    public CharSequence asPEM( PKCS10CertificationRequest pkcs10 )
    {
        return asPEM( pkcs10.getClass().getSimpleName(), pkcs10 );
    }

    public CharSequence asPEM( X509CRL x509CRL )
    {
        return asPEM( x509CRL.getClass().getSimpleName(), x509CRL );
    }

    private CharSequence asPEM( String ilk, Object object )
    {
        try {
            StringWriter sw = new StringWriter();
            PEMWriter pemWriter = new PEMWriter( sw );
            pemWriter.writeObject( object );
            pemWriter.flush();
            return sw.getBuffer();
        } catch ( IOException ex ) {
            throw new QiPkiFailure( "Unable to write " + ilk + " as PEM", ex );
        }
    }

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

                        System.out.println( "#################################################################################################" );

                        System.out.println( extensionsDEREncodable );
                        System.out.println( extensionsASN1Sequence );

                        X509ExtensionUtil plop;

                        Enumeration enume = extensionsASN1Sequence.getObjects();
                        while ( enume.hasMoreElements() ) {
                            Object obj = enume.nextElement();
                            DEREncodable der = ( DEREncodable ) obj;
                            System.out.println( "\t" + obj );
                            System.out.println( "\t" + der );
                        }

                        System.out.println( "#################################################################################################" );

                        certificateRequestExtensions = new X509Extensions( extensionsASN1Sequence );

                        Enumeration e = certificateRequestExtensions.oids();
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

    private KeyStore getKeyStoreInstance( KeyStoreType storeType )
            throws KeyStoreException, NoSuchProviderException
    {
        if ( KeyStoreType.PKCS12 == storeType ) {
            return KeyStore.getInstance( storeType.asString(), BouncyCastleProvider.PROVIDER_NAME );
        }
        return KeyStore.getInstance( storeType.asString() );
    }

}
