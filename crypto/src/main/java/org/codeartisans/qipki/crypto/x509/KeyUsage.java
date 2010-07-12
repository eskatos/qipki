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

import java.util.Iterator;
import java.util.Set;

public enum KeyUsage
{

    digitalSignature( org.bouncycastle.asn1.x509.KeyUsage.digitalSignature ),
    nonRepudiation( org.bouncycastle.asn1.x509.KeyUsage.nonRepudiation ),
    keyEncipherment( org.bouncycastle.asn1.x509.KeyUsage.keyEncipherment ),
    dataEncipherment( org.bouncycastle.asn1.x509.KeyUsage.dataEncipherment ),
    keyAgreement( org.bouncycastle.asn1.x509.KeyUsage.keyAgreement ),
    keyCertSign( org.bouncycastle.asn1.x509.KeyUsage.keyCertSign ),
    cRLSign( org.bouncycastle.asn1.x509.KeyUsage.cRLSign ),
    encipherOnly( org.bouncycastle.asn1.x509.KeyUsage.encipherOnly ),
    decipherOnly( org.bouncycastle.asn1.x509.KeyUsage.decipherOnly );
    private int usage;

    private KeyUsage( int usage )
    {
        this.usage = usage;
    }

    public int usage()
    {
        return usage;
    }

    public static int usage( Set<KeyUsage> keyUsages )
    {
        Iterator<KeyUsage> it = keyUsages.iterator();
        int usage = 0;
        while ( it.hasNext() ) {
            usage |= it.next().usage();
        }
        return usage;
    }

}
