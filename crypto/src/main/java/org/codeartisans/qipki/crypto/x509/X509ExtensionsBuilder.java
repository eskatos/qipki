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

import java.security.PublicKey;
import java.util.Map;
import java.util.Set;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.misc.NetscapeCertType;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;

public interface X509ExtensionsBuilder
{

    SubjectKeyIdentifier buildSubjectKeyIdentifier( PublicKey publicKey );

    AuthorityKeyIdentifier buildAuthorityKeyIdentifier( PublicKey publicKey );

    BasicConstraints buildNonCABasicConstraints();

    BasicConstraints buildCABasicConstraints( Long pathLen );

    KeyUsage buildKeyUsages( Set<org.codeartisans.qipki.crypto.x509.KeyUsage> keyUsages );

    ExtendedKeyUsage buildExtendedKeyUsage( Set<org.codeartisans.qipki.crypto.x509.ExtendedKeyUsage> extendedKeyUsages );

    NetscapeCertType buildNetscapeCertTypes( Set<org.codeartisans.qipki.crypto.x509.NetscapeCertType> netscapeCertTypes );

    CRLDistPoint buildCRLDistributionPoints( Map<X500Principal, Iterable<String>> crlDistPointsData );

    CRLDistPoint buildCRLDistributionPoints( X500Principal issuer, String... crlUris );

}
