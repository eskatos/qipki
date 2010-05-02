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

public enum NetscapeCertType
{

    sslClient( org.bouncycastle.asn1.misc.NetscapeCertType.sslClient ),
    sslServer( org.bouncycastle.asn1.misc.NetscapeCertType.sslServer ),
    smime( org.bouncycastle.asn1.misc.NetscapeCertType.smime ),
    objectSigning( org.bouncycastle.asn1.misc.NetscapeCertType.objectSigning ),
    reserved( org.bouncycastle.asn1.misc.NetscapeCertType.reserved ),
    sslCA( org.bouncycastle.asn1.misc.NetscapeCertType.sslCA ),
    smimeCA( org.bouncycastle.asn1.misc.NetscapeCertType.smimeCA ),
    objectSigningCA( org.bouncycastle.asn1.misc.NetscapeCertType.objectSigningCA );
    private int intValue;

    private NetscapeCertType( int intValue )
    {
        this.intValue = intValue;
    }

    public int getIntValue()
    {
        return intValue;
    }

}
