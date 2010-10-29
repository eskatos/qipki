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
package org.codeartisans.qipki.ca.application.contexts.x509;

import org.codeartisans.qipki.ca.domain.escrowedkeypair.EscrowedKeyPair;
import org.codeartisans.qipki.ca.domain.escrowedkeypair.EscrowedKeyPairRepository;
import org.codeartisans.qipki.ca.domain.revocation.Revocation;
import org.codeartisans.qipki.ca.domain.x509.X509;
import org.codeartisans.qipki.core.dci.Context;
import org.codeartisans.qipki.crypto.x509.RevocationReason;

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

}
