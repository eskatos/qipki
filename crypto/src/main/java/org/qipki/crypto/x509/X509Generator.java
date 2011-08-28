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

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.List;

import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.jce.PKCS10CertificationRequest;

import org.joda.time.Duration;

public interface X509Generator
{

    PKCS10CertificationRequest generatePKCS10( DistinguishedName distinguishedName,
                                               KeyPair keyPair );

    PKCS10CertificationRequest generatePKCS10( DistinguishedName distinguishedName, KeyPair keyPair,
                                               GeneralNames subjectAlternativeNames );

    X509Certificate generateX509Certificate( PrivateKey privateKey,
                                             DistinguishedName issuerDN,
                                             BigInteger serialNumber,
                                             DistinguishedName subjectDN,
                                             PublicKey publicKey,
                                             Duration validity,
                                             List<X509ExtensionHolder> x509Extensions );

    X509CRL generateX509CRL( X509Certificate caCertificate, PrivateKey caPrivateKey );

    X509CRL updateX509CRL( X509Certificate caCertificate, PrivateKey caPrivateKey,
                           X509Certificate revokedCertificate, RevocationReason reason,
                           X509CRL previousCRL, BigInteger lastCRLNumber );

}
