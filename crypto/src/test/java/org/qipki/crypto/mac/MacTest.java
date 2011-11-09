/*
 * Copyright (c) 2011, Paul Merlin. All Rights Reserved.
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
package org.qipki.crypto.mac;

import javax.crypto.SecretKey;

import static org.junit.Assert.*;
import org.junit.Test;

import org.qipki.crypto.AbstractQiPkiCryptoTest;
import static org.qipki.crypto.algorithms.MACAlgorithm.*;
import static org.qipki.crypto.symetric.SymetricSigningGeneratorParameters.*;

public class MacTest
        extends AbstractQiPkiCryptoTest
{

    // SNIPPET BEGIN crypto.mac.1
    @Test
    public void testMac()
    {
        // Alice & Bob
        SecretKey signingKey = symGenerator.generateSigningKey( HmacSHA256_256 );

        // Alice
        String message = "Oh Bob, MAC me!";
        String aliceMAC = mac.hexMac( message, new MACParameters( HmacSHA256, signingKey ) );

        System.out.println( "Alice: " + message + " [ " + aliceMAC + " ]" );

        // Bob
        String bobMAC = mac.hexMac( message, new MACParameters( HmacSHA256, signingKey ) );
        assertEquals( aliceMAC, bobMAC );

        System.out.println( "Bob: Alright Alice!" + " [ " + bobMAC + " ]" );
    }
    // SNIPPET END crypto.mac.1

}
