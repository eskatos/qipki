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
package org.codeartisans.qipki.core.crypto.tools;

import java.io.Reader;
import java.security.KeyStore;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.codeartisans.qipki.commons.constants.KeyStoreType;

public interface CryptIO
{

    CharSequence asPEM( X509Certificate certificate );

    CharSequence asPEM( PKCS10CertificationRequest pkcs10 );

    CharSequence asPEM( X509CRL x509CRL );

    KeyStore base64DecodeKeyStore( String payload, KeyStoreType storeType, char[] password );

    String base64Encode( KeyStore keystore, char[] password );

    KeyStore createEmptyKeyStore( KeyStoreType storeType );

    X509Extensions extractRequestedExtensions( PKCS10CertificationRequest pkcs10 );

    PKCS10CertificationRequest readPKCS10PEM( Reader reader );

}
