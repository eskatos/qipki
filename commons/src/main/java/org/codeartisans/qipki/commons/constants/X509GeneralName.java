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
package org.codeartisans.qipki.commons.constants;

public enum X509GeneralName
{

    otherName( org.bouncycastle.asn1.x509.GeneralName.otherName ),
    rfc822Name( org.bouncycastle.asn1.x509.GeneralName.rfc822Name ),
    dNSName( org.bouncycastle.asn1.x509.GeneralName.dNSName ),
    x400Address( org.bouncycastle.asn1.x509.GeneralName.x400Address ),
    directoryName( org.bouncycastle.asn1.x509.GeneralName.directoryName ),
    ediPartyName( org.bouncycastle.asn1.x509.GeneralName.ediPartyName ),
    uniformResourceIdentifier( org.bouncycastle.asn1.x509.GeneralName.uniformResourceIdentifier ),
    iPAddress( org.bouncycastle.asn1.x509.GeneralName.iPAddress ),
    registeredID( org.bouncycastle.asn1.x509.GeneralName.registeredID ),
    unknownGeneralName( -1 );
    private int tag;

    private X509GeneralName( int tag )
    {
        this.tag = tag;
    }

    public int tag()
    {
        return tag;
    }

}
