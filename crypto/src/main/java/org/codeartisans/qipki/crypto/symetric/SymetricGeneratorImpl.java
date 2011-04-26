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
package org.codeartisans.qipki.crypto.symetric;

import java.security.GeneralSecurityException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.codeartisans.qipki.crypto.QiCryptoFailure;

public class SymetricGeneratorImpl
        implements SymetricGenerator
{

    @Override
    public SecretKey generateSecretKey( SymetricGeneratorParameters params )
    {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance( params.algorithm().jcaString(), BouncyCastleProvider.PROVIDER_NAME );
            keyGen.init( params.keySize() );
            return keyGen.generateKey();
        } catch ( GeneralSecurityException ex ) {
            throw new QiCryptoFailure( "Unable to generate " + params.algorithm().jcaString() + " " + params.keySize() + " SecretKey", ex );
        }
    }

}
