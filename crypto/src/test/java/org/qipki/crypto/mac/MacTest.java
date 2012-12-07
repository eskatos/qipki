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
import org.junit.Test;
import org.qipki.crypto.AbstractQiPkiCryptoTest;

import static org.junit.Assert.assertEquals;
import static org.qipki.crypto.algorithms.HMACAlgorithm.HmacSHA256;
import static org.qipki.crypto.symetric.SymetricSigningGeneratorParameters.HmacSHA256_256;

public class MacTest
    extends AbstractQiPkiCryptoTest
{

    public static final String[] SAMPLES = new String[]
    {
        "Hello World",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla tristique dui vel leo porta commodo. Nam "
        + "neque mauris, semper in rhoncus eget, fringilla in tellus. Nunc consequat felis eget turpis lacinia non "
        + "mattis nunc mollis. Fusce nec quam mi. Fusce viverra, magna eu convallis aliquet, enim justo imperdiet "
        + "eros, at ullamcorper eros orci in lorem. Fusce volutpat massa a turpis facilisis porta consequat lacus "
        + "commodo. Pellentesque vulputate fermentum velit. Integer elementum ornare tortor quis consectetur. Cras "
        + "vel orci sed nisl sollicitudin fringilla ac et libero. Nulla a eros est, nec volutpat mi. Curabitur "
        + "vehicula mollis vulputate. Donec ligula erat, facilisis ut semper ac, lacinia vitae purus. Vivamus "
        + "pharetra mauris eget tellus elementum elementum. Ut et justo purus, vitae elementum magna. Phasellus "
        + "tortor orci, feugiat id venenatis sit amet, tempor id nisl. Donec venenatis enim vitae diam pulvinar "
        + "lobortis."
    };

    // SNIPPET BEGIN crypto.mac.1
    @Test
    public void testMac()
    {
        // Alice & Bob
        SecretKey signingKey = symGenerator.generateSigningKey( HmacSHA256_256 );

        // Alice
        String message = "Oh Bob, MAC me!";
        String aliceMAC = mac.hexMac( message, new HMACParameters( HmacSHA256, signingKey ) );

        System.out.println( "Alice: " + message + " [ " + aliceMAC + " ]" );

        // Bob
        String bobMAC = mac.hexMac( message, new HMACParameters( HmacSHA256, signingKey ) );
        assertEquals( aliceMAC, bobMAC );

        System.out.println( "Bob: Alright Alice!" + " [ " + bobMAC + " ]" );
    }
    // SNIPPET END crypto.mac.1
}
