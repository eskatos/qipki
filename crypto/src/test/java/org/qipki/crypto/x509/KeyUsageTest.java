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
