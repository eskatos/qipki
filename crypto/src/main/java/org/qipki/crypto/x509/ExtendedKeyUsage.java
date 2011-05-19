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
