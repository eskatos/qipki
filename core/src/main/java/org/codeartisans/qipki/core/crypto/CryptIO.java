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

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.security.cert.X509Certificate;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.CertificationRequestInfo;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.Attribute;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PEMWriter;
import org.codeartisans.qipki.core.QiPkiFailure;
import org.qi4j.api.composite.TransientComposite;
import org.qi4j.api.mixin.Mixins;

@Mixins( CryptIO.Mixin.class )
public interface CryptIO
        extends TransientComposite
{

    PKCS10CertificationRequest readPKCS10PEM( Reader reader );

    CharSequence asPEM( X509Certificate certificate );

    CharSequence asPEM( PKCS10CertificationRequest pkcs10 );

    X509Extensions extractRequestedExtensions( PKCS10CertificationRequest pkcs10 );

    abstract class Mixin
            implements CryptIO
    {

        @Override
        public PKCS10CertificationRequest readPKCS10PEM( Reader reader )
        {
            try {
                return ( PKCS10CertificationRequest ) new PEMReader( reader ).readObject();
            } catch ( IOException ex ) {
                throw new IllegalArgumentException( "Unable to read pkcs10 from PEM", ex );
            }
        }

        @Override
        public CharSequence asPEM( X509Certificate certificate )
        {
            return asPEM( certificate.getClass().getSimpleName(), certificate );
        }

        @Override
        public CharSequence asPEM( PKCS10CertificationRequest pkcs10 )
        {
            return asPEM( pkcs10.getClass().getSimpleName(), pkcs10 );
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
                            certificateRequestExtensions = new X509Extensions( ( ASN1Sequence ) attributeValues.getObjectAt( 0 ) );

                            // No need to search any more.
                            break;
                        }
                    }
                }
            }
            return certificateRequestExtensions;
        }

    }

}
