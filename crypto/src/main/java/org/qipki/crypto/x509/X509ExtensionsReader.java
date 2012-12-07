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

import java.security.cert.X509Certificate;
import java.util.List;
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
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.joda.time.Interval;
import org.qi4j.api.common.Optional;

public interface X509ExtensionsReader
{

    List<X509ExtensionHolder> extractRequestedExtensions( PKCS10CertificationRequest pkcs10 );

    Map.Entry<X509GeneralName, String> asImmutableMapEntry( GeneralName generalName );

    Map<X509GeneralName, String> asMap( @Optional GeneralNames generalNames );

    AuthorityKeyIdentifier getAuthorityKeyIdentifier( X509Certificate cert );

    BasicConstraints getBasicConstraints( X509Certificate cert );

    DistributionPoint[] getCRLDistributionPoints( X509Certificate cert );

    Set<PolicyInformation> getCertificatePolicies( X509Certificate cert );

    GeneralNames getIssuerAlternativeNames( X509Certificate cert );

    Set<KeyUsage> getKeyUsages( X509Certificate cert );

    Set<ExtendedKeyUsage> getExtendedKeyUsages( X509Certificate cert );

    Set<NetscapeCertType> getNetscapeCertTypes( X509Certificate cert );

    String getNetscapeCertComment( X509Certificate cert );

    NameConstraints getNameConstraints( X509Certificate cert );

    Set<PolicyConstraint> getPolicyConstraints( X509Certificate cert );

    Set<PolicyMapping> getPolicyMappings( X509Certificate cert );

    Interval getPrivateKeyUsagePeriod( X509Certificate cert );

    Set<RevocationReason> getRevocationReasons( ReasonFlags reasonFlags );

    GeneralNames getSubjectAlternativeNames( X509Certificate cert );

    byte[] getSubjectKeyIdentifier( X509Certificate cert );

}
