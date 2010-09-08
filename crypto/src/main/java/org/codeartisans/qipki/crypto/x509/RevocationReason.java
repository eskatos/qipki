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
package org.codeartisans.qipki.crypto.x509;

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
        switch ( reason ) {
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
