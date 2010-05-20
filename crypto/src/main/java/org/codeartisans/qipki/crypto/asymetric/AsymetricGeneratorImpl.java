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
package org.codeartisans.qipki.crypto.asymetric;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.codeartisans.qipki.crypto.QiCryptoFailure;

public class AsymetricGeneratorImpl
        implements AsymetricGenerator
{

    @Override
    public KeyPair generateKeyPair( AsymetricGeneratorParameters params )
    {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance( params.algorithm().algoString(), BouncyCastleProvider.PROVIDER_NAME );
            keyGen.initialize( params.keySize() );
            return keyGen.generateKeyPair();
        } catch ( GeneralSecurityException ex ) {
            throw new QiCryptoFailure( "Unable to generate " + params.algorithm().algoString() + " KeyPair", ex );
        }
    }

}