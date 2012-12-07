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
package org.qipki.crypto.cipher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Security;

import javax.crypto.SecretKey;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.qipki.crypto.AbstractQiPkiCryptoTest;
import org.qipki.crypto.CryptoContext;
import org.qipki.crypto.DefaultCryptoContext;
import static org.qipki.crypto.cipher.SymetricCipherFactoryParameters.AES_CBC_PKCS5;
import static org.qipki.crypto.constants.IOConstants.UTF_8;
import static org.qipki.crypto.symetric.SymetricCipheringGeneratorParameters.AES_128;
import org.qipki.crypto.symetric.SymetricGenerator;
import org.qipki.crypto.symetric.SymetricGeneratorImpl;

public class CipherTest
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

    @Test
    public void testAES128WithQi4j()
        throws UnsupportedEncodingException
    {
        testAES128( symGenerator, cipherFactory );
    }

    @Test
    public void testAES128WithoutQi4j()
        throws Exception
    {
        Security.addProvider( new BouncyCastleProvider() );

        CryptoContext cryptoContext = new DefaultCryptoContext();
        testAES128( new SymetricGeneratorImpl( cryptoContext ),
                    new CipherFactoryImpl( cryptoContext ) );

        Security.removeProvider( BouncyCastleProvider.PROVIDER_NAME );
    }

    // SNIPPET BEGIN crypto.cipher.block
    private void testAES128( SymetricGenerator symGenerator, CipherFactory cipherFactory )
        throws UnsupportedEncodingException
    {
        // AES-128 key generation
        SecretKey key = symGenerator.generateCipheringKey( AES_128 );
        String plainText = "CipherMe";

        // Cipher creation
        SymetricCipher cipher = cipherFactory.newSymetricCipher( AES_CBC_PKCS5 );

        // Cipher and decipher
        byte[] ciphered = cipher.cipher( plainText.getBytes( UTF_8 ), key );
        byte[] deciphered = cipher.decipher( ciphered, key );

        // Test
        assertEquals( plainText, new String( deciphered, UTF_8 ) );
    }
    // SNIPPET END crypto.cipher.block

    @Test
    // SNIPPET BEGIN crypto.cipher.stream
    public void testOnStreams()
        throws UnsupportedEncodingException
    {
        // AES-128 key generation
        SecretKey key = symGenerator.generateCipheringKey( AES_128 );
        String plainText = "CipherMe in a stream";

        // Cipher creation
        SymetricCipher cipher = cipherFactory.newSymetricCipher( AES_CBC_PKCS5 );

        // Ciphering…
        InputStream inputStream = new ByteArrayInputStream( plainText.getBytes( UTF_8 ) );
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        cipher.cipher( inputStream, outputStream, key );
        byte[] ciphered = outputStream.toByteArray();

        // Deciphering…
        inputStream = new ByteArrayInputStream( ciphered );
        outputStream = new ByteArrayOutputStream();

        cipher.decipher( inputStream, outputStream, key );
        byte[] deciphered = outputStream.toByteArray();

        // Test
        assertEquals( plainText, new String( deciphered, UTF_8 ) );
    }
    // SNIPPET END crypto.cipher.stream

}
