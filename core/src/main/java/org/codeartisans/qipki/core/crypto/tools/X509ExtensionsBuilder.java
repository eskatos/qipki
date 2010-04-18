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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.PublicKey;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.codeartisans.qipki.core.QiPkiFailure;

public class X509ExtensionsBuilder
{

    public SubjectKeyIdentifier buildSubjectKeyIdentifier( PublicKey publicKey )
    {
        try {
            ByteArrayInputStream pubKeyInputStream = new ByteArrayInputStream( publicKey.getEncoded() );
            SubjectPublicKeyInfo pubKeyInfo = new SubjectPublicKeyInfo( ( ASN1Sequence ) new ASN1InputStream( pubKeyInputStream ).readObject() );
            return new SubjectKeyIdentifier( pubKeyInfo );
        } catch ( IOException ex ) {
            throw new QiPkiFailure( "Unable to build SubjectKeyIdentifier", ex );
        }
    }

    public AuthorityKeyIdentifier buildAuthorityKeyIdentifier( PublicKey publicKey )
    {
        try {
            ByteArrayInputStream pubKeyInputStream = new ByteArrayInputStream( publicKey.getEncoded() );
            SubjectPublicKeyInfo pubKeyInfo = new SubjectPublicKeyInfo( ( ASN1Sequence ) new ASN1InputStream( pubKeyInputStream ).readObject() );
            return new AuthorityKeyIdentifier( pubKeyInfo );
        } catch ( IOException ex ) {
            throw new QiPkiFailure( "Unable to build SubjectKeyIdentifier", ex );
        }
    }

}
