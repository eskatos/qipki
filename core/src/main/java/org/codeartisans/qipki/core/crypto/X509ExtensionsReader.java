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
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.PrivateKeyUsagePeriod;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.codeartisans.qipki.core.QiPkiFailure;
import org.codeartisans.qipki.commons.constants.KeyUsage;
import org.codeartisans.qipki.core.crypto.x509.X509OIDs;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.qi4j.api.injection.scope.Uses;

public class X509ExtensionsReader
{

    private final CryptCodex cryptCodex;

    public X509ExtensionsReader( @Uses CryptCodex cryptCodex )
    {
        this.cryptCodex = cryptCodex;
    }

    public AuthorityKeyIdentifier getAuthorityKeyIdentifier( X509Certificate cert )
    {
        try {
            byte[] value = cert.getExtensionValue( X509OIDs.X509Extensions.KeyRelated.AuthorityKeyIdentifier );
            if ( value == null ) {
                return null;
            }
            return AuthorityKeyIdentifier.getInstance( ASN1Object.fromByteArray( value ) );
        } catch ( IOException ex ) {
            throw new QiPkiFailure( "Unable to extract AuthorityKeyIdentifier from X509Certificate extensions", ex );
        }
    }

    public byte[] getSubjectKeyIdentifier( X509Certificate cert )
    {
        try {
            byte[] value = cert.getExtensionValue( X509OIDs.X509Extensions.KeyRelated.SubjectKeyIdentifier );
            if ( value == null ) {
                return null;
            }
            return SubjectKeyIdentifier.getInstance( ASN1Object.fromByteArray( value ) ).getKeyIdentifier();
        } catch ( IOException ex ) {
            throw new QiPkiFailure( "Unable to extract SubjectKeyIdentifier from X509Certificate extensions", ex );
        }
    }

    public Set<KeyUsage> getKeyUsages( X509Certificate cert )
    {
        try {
            byte[] value = cert.getExtensionValue( X509OIDs.X509Extensions.KeyRelated.KeyUsages );
            if ( value == null ) {
                return Collections.EMPTY_SET;
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
            throw new QiPkiFailure( "Unable to extract KeyUsages from X509Certificate extensions", ex );
        }
    }

    public Interval getPrivateKeyUsagePeriod( X509Certificate cert )
    {
        try {
            byte[] value = cert.getExtensionValue( X509OIDs.X509Extensions.KeyRelated.SubjectKeyIdentifier );
            if ( value == null ) {
                return null;
            }
            PrivateKeyUsagePeriod privKeyUsagePeriod = PrivateKeyUsagePeriod.getInstance( ASN1Object.fromByteArray( value ) );
            SimpleDateFormat derDateFormat = new SimpleDateFormat( "yyyyMMddHHmmssz" );
            Date notBefore = derDateFormat.parse( privKeyUsagePeriod.getNotBefore().getTime() );
            Date notAfter = derDateFormat.parse( privKeyUsagePeriod.getNotAfter().getTime() );
            return new Interval( new DateTime( notBefore ), new DateTime( notAfter ) );
        } catch ( ParseException ex ) {
            throw new QiPkiFailure( "Unable to extract PrivateKeyUsagePeriod from X509Certificate extensions", ex );
        } catch ( IOException ex ) {
            throw new QiPkiFailure( "Unable to extract PrivateKeyUsagePeriod from X509Certificate extensions", ex );
        }
    }

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
            throw new QiPkiFailure( "Unable to extract CRLDistributionPoints from X509Certificate extensions", ex );
        }

    }

    public Set<String> asStrings( GeneralNames generalNames )
    {
        if ( generalNames == null ) {
            return Collections.EMPTY_SET;
        }
        Set<String> strings = new LinkedHashSet<String>( generalNames.getNames().length );
        for ( GeneralName eachGeneralName : generalNames.getNames() ) {
            int nameType = eachGeneralName.getTagNo();
            switch ( nameType ) {
                case GeneralName.otherName:
                    ASN1Sequence otherName = ( ASN1Sequence ) eachGeneralName.getName();
                    String oid = ( ( DERObjectIdentifier ) otherName.getObjectAt( 0 ) ).getId();
                    String name = cryptCodex.toString( otherName.getObjectAt( 1 ) );
                    strings.add( "otherName(" + oid + "): " + name );
                    break;
                case GeneralName.rfc822Name:
                    strings.add( "rfc822Name: " + eachGeneralName.getName().toString() );
                    break;
                case GeneralName.dNSName:
                    strings.add( "dNSName: " + eachGeneralName.getName().toString() );
                    break;
                case GeneralName.registeredID:
                    strings.add( "registeredID: " + eachGeneralName.getName().toString() );
                    break;
                case GeneralName.x400Address:
                    strings.add( "x400Address: " + eachGeneralName.getName().toString() );
                    break;
                case GeneralName.ediPartyName:
                    strings.add( "ediPartyName: " + eachGeneralName.getName().toString() );
                    break;
                case GeneralName.directoryName:
                    strings.add( "directoryName: " + new X500Principal( ( ( X509Name ) eachGeneralName.getName() ).toString() ).getName( X500Principal.CANONICAL ) );
                    break;
                case GeneralName.uniformResourceIdentifier:
                    strings.add( "uniformResourceIdentifier: " + eachGeneralName.getName().toString() );
                    break;
                case GeneralName.iPAddress: // What about IPv6 addresses ?
                    ASN1OctetString iPAddress = ( ASN1OctetString ) eachGeneralName.getName();
                    byte[] iPAddressBytes = iPAddress.getOctets();
                    StringBuilder sb = new StringBuilder( "iPAddress: " );
                    for ( int idx = 0; idx < iPAddressBytes.length; idx++ ) {
                        sb.append( iPAddressBytes[idx] & 0xFF );
                        if ( idx + 1 < iPAddressBytes.length ) {
                            sb.append( "." );
                        }
                    }
                    strings.add( sb.toString() );
                default:
                    strings.add( "UnknownGeneralNameType(" + nameType + "): " + eachGeneralName.getName().toString() );
            }
        }

        return strings;
    }

}
