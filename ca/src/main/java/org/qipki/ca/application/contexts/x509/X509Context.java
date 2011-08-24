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
package org.qipki.ca.application.contexts.x509;

import java.security.cert.X509Certificate;

import org.bouncycastle.jce.PKCS10CertificationRequest;

import org.qipki.ca.domain.ca.CA;
import org.qipki.ca.domain.escrowedkeypair.EscrowedKeyPair;
import org.qipki.ca.domain.escrowedkeypair.EscrowedKeyPairRepository;
import org.qipki.ca.domain.revocation.Revocation;
import org.qipki.ca.domain.x509.X509;
import org.qipki.ca.domain.x509.X509Factory;
import org.qipki.ca.domain.x509profile.X509Profile;
import org.qipki.core.dci.Context;
import org.qipki.crypto.x509.RevocationReason;

public class X509Context
        extends Context
{

    public X509 x509()
    {
        return context.role( X509.class );
    }

    public Revocation revoke( RevocationReason reason )
    {
        X509 x509 = context.role( X509.class );
        return x509.issuer().get().revoke( x509, reason );
    }

    public EscrowedKeyPair recover()
    {
        X509 x509 = context.role( X509.class );
        return context.role( EscrowedKeyPairRepository.class ).findByX509Identity( x509.identity().get() );
    }

    public X509 renew( PKCS10CertificationRequest pkcs10 )
    {
        X509 oldX509 = context.role( X509.class );
        CA ca = oldX509.issuer().get();
        X509Profile profile = oldX509.profile().get();
        ca.revoke( oldX509, RevocationReason.superseded );
        X509Certificate certificate = ca.sign( profile, pkcs10 );
        return context.role( X509Factory.class ).create( certificate, ca, profile );
    }

}
