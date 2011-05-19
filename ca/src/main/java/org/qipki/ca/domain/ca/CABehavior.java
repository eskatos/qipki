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
package org.qipki.ca.domain.ca;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.PKCS10CertificationRequest;

import org.qipki.ca.domain.revocation.Revocation;
import org.qipki.ca.domain.x509.X509;
import org.qipki.ca.domain.x509profile.X509Profile;
import org.qipki.crypto.x509.DistinguishedName;
import org.qipki.crypto.x509.RevocationReason;

public interface CABehavior
{

    DistinguishedName distinguishedName();

    DistinguishedName issuerDistinguishedName();

    X509Certificate certificate();

    PrivateKey privateKey();

    X509Certificate sign( X509Profile x509profile, PKCS10CertificationRequest pkcs10 );

    Revocation revoke( X509 x509, RevocationReason reason );

}
