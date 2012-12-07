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

import org.bouncycastle.asn1.x509.CRLReason;

public enum RevocationReason
{

    unspecified( CRLReason.unspecified ),
    keyCompromise( CRLReason.keyCompromise ),
    cACompromise( CRLReason.cACompromise ),
    affiliationChanged( CRLReason.affiliationChanged ),
    superseded( CRLReason.superseded ),
    cessationOfOperation( CRLReason.cessationOfOperation ),
    certificateHold( CRLReason.certificateHold ),
    removeFromCRL( CRLReason.removeFromCRL ),
    privilegeWithdrawn( CRLReason.privilegeWithdrawn ),
    aACompromise( CRLReason.aACompromise );
    private int reason;

    private RevocationReason( int reason )
    {
        this.reason = reason;
    }

    public int reason()
    {
        return reason;
    }

    public static RevocationReason valueOf( int reason )
    {
        switch( reason )
        {
            case CRLReason.unspecified:
                return unspecified;
            case CRLReason.keyCompromise:
                return keyCompromise;
            case CRLReason.cACompromise:
                return cACompromise;
            case CRLReason.affiliationChanged:
                return affiliationChanged;
            case CRLReason.superseded:
                return superseded;
            case CRLReason.cessationOfOperation:
                return cessationOfOperation;
            case CRLReason.certificateHold:
                return certificateHold;
            case CRLReason.removeFromCRL:
                return removeFromCRL;
            case CRLReason.privilegeWithdrawn:
                return privilegeWithdrawn;
            case CRLReason.aACompromise:
                return aACompromise;
            default:
                throw new IllegalArgumentException( "Unknown revocation reason: " + reason );
        }
    }

}
