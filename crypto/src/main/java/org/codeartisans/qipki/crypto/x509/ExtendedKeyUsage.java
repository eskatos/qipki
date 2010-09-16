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

import java.util.Set;
import java.util.Vector;

import org.bouncycastle.asn1.x509.KeyPurposeId;

public enum ExtendedKeyUsage
{

    /**
     * Indicates that a certificate can be used as an SSL server certificate.
     */
    serverAuth( KeyPurposeId.id_kp_serverAuth ),
    /**
     * Indicates that a certificate can be used as an SSL client certificate.
     */
    clientAuth( KeyPurposeId.id_kp_clientAuth ),
    /**
     * Indicates that a certificate can be used for code signing.
     */
    codeSigning( KeyPurposeId.id_kp_codeSigning ),
    /**
     * Indicates that a certificate can be used for protecting email (signing, encryption, key agreement).
     */
    emailProtection( KeyPurposeId.id_kp_emailProtection ),
    ipsecEndSystem( KeyPurposeId.id_kp_ipsecEndSystem ),
    ipsecTunnel( KeyPurposeId.id_kp_ipsecTunnel ),
    ipsecUser( KeyPurposeId.id_kp_ipsecUser ),
    /**
     * Indicates that a certificate can be used to bind the hash of an object to a time from a trusted time source.
     */
    timeStamping( KeyPurposeId.id_kp_timeStamping ),
    /**
     * Indicates that a X509Certificates corresponding private key may be used by an authority to sign OCSP-Responses.
     */
    OCSPSigning( KeyPurposeId.id_kp_OCSPSigning ),
    dvcs( KeyPurposeId.id_kp_dvcs ),
    sbgpCertAAServerAuth( KeyPurposeId.id_kp_sbgpCertAAServerAuth ),
    scvp_responder( KeyPurposeId.id_kp_scvp_responder ),
    eapOverPPP( KeyPurposeId.id_kp_eapOverPPP ),
    eapOverLAN( KeyPurposeId.id_kp_eapOverLAN ),
    scvpServer( KeyPurposeId.id_kp_scvpServer ),
    scvpClient( KeyPurposeId.id_kp_scvpClient ),
    ipsecIKE( KeyPurposeId.id_kp_ipsecIKE ),
    capwapAC( KeyPurposeId.id_kp_capwapAC ),
    capwapWTP( KeyPurposeId.id_kp_capwapWTP ),
    smartcardlogon( KeyPurposeId.id_kp_smartcardlogon );
    private KeyPurposeId keyPurposeId;

    private ExtendedKeyUsage( KeyPurposeId keyPurposeId )
    {
        this.keyPurposeId = keyPurposeId;
    }

    public KeyPurposeId getKeyPurposeId()
    {
        return keyPurposeId;
    }

    public static Vector<KeyPurposeId> usage( Set<ExtendedKeyUsage> extKeyUsages )
    {
        Vector<KeyPurposeId> keyPurposes = new Vector<KeyPurposeId>( extKeyUsages.size() );
        for ( ExtendedKeyUsage eachExtKeyUsage : extKeyUsages ) {
            keyPurposes.add( eachExtKeyUsage.keyPurposeId );
        }
        return keyPurposes;
    }

}
