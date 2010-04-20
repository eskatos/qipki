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
package org.codeartisans.qipki.core.crypto.tools.x509;

import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.NameConstraints;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.bouncycastle.asn1.x509.ReasonFlags;
import org.codeartisans.qipki.commons.constants.KeyUsage;
import org.codeartisans.qipki.commons.constants.RevocationReason;
import org.codeartisans.qipki.commons.constants.X509GeneralName;
import org.joda.time.Interval;
import org.qi4j.api.common.Optional;

public interface X509ExtensionsReader
{

    Map.Entry<X509GeneralName, String> asImmutableMapEntry( GeneralName generalName );

    Map<X509GeneralName, String> asMap( @Optional GeneralNames generalNames );

    AuthorityKeyIdentifier getAuthorityKeyIdentifier( X509Certificate cert );

    BasicConstraints getBasicConstraints( X509Certificate cert );

    DistributionPoint[] getCRLDistributionPoints( X509Certificate cert );

    Set<PolicyInformation> getCertificatePolicies( X509Certificate cert );

    GeneralNames getIssuerAlternativeNames( X509Certificate cert );

    Set<KeyUsage> getKeyUsages( X509Certificate cert );

    NameConstraints getNameConstraints( X509Certificate cert );

    Set<PolicyConstraint> getPolicyConstraints( X509Certificate cert );

    Set<PolicyMapping> getPolicyMappings( X509Certificate cert );

    Interval getPrivateKeyUsagePeriod( X509Certificate cert );

    Set<RevocationReason> getRevocationReasons( ReasonFlags reasonFlags );

    GeneralNames getSubjectAlternativeNames( X509Certificate cert );

    byte[] getSubjectKeyIdentifier( X509Certificate cert );

}
