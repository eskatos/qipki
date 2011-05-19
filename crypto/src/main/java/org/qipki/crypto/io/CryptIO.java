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
package org.qipki.crypto.io;

import java.io.Reader;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.PKCS10CertificationRequest;

import org.qipki.crypto.storage.KeyStoreType;

public interface CryptIO
{

    CharSequence asPEM( X509Certificate certificate );

    CharSequence asPEM( PKCS10CertificationRequest pkcs10 );

    CharSequence asPEM( X509CRL x509CRL );

    CharSequence asPEM( PublicKey pubKey );

    CharSequence asPEM( KeyPair keyPair );

    CharSequence asPEM( KeyPair keyPair, char[] password );

    X509Certificate readX509PEM( Reader stringReader );

    PKCS10CertificationRequest readPKCS10PEM( Reader reader );

    X509CRL readCRLPEM( Reader reader );

    KeyPair readKeyPairPEM( Reader reader );

    KeyStore base64DecodeKeyStore( String payload, KeyStoreType storeType, char[] password );

    String base64Encode( KeyStore keystore, char[] password );

    KeyStore createEmptyKeyStore( KeyStoreType storeType );

}
