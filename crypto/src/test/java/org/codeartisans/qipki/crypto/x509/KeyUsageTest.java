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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Test;

@SuppressWarnings( "SetReplaceableByEnumSet" )
public class KeyUsageTest
{

    @Test
    public void testEmpty()
    {
        assertEquals( 0, KeyUsage.usage( Collections.<KeyUsage>emptySet() ) );
    }

    @Test
    public void testSSlClient()
    {
        Set<KeyUsage> sslUsages = new HashSet<KeyUsage>();
        sslUsages.add( KeyUsage.keyEncipherment );
        sslUsages.add( KeyUsage.digitalSignature );
        assertEquals( org.bouncycastle.asn1.x509.KeyUsage.keyEncipherment | org.bouncycastle.asn1.x509.KeyUsage.digitalSignature, KeyUsage.usage( sslUsages ) );
    }

    @Test
    public void testFull()
    {
        int bcValue = org.bouncycastle.asn1.x509.KeyUsage.digitalSignature | org.bouncycastle.asn1.x509.KeyUsage.nonRepudiation | org.bouncycastle.asn1.x509.KeyUsage.keyEncipherment
                | org.bouncycastle.asn1.x509.KeyUsage.dataEncipherment | org.bouncycastle.asn1.x509.KeyUsage.keyAgreement | org.bouncycastle.asn1.x509.KeyUsage.keyCertSign
                | org.bouncycastle.asn1.x509.KeyUsage.cRLSign | org.bouncycastle.asn1.x509.KeyUsage.encipherOnly | org.bouncycastle.asn1.x509.KeyUsage.decipherOnly;
        Set<KeyUsage> allUsages = new HashSet<KeyUsage>();
        allUsages.addAll( Arrays.asList( KeyUsage.values() ) );
        assertEquals( bcValue, KeyUsage.usage( allUsages ) );
    }

}
