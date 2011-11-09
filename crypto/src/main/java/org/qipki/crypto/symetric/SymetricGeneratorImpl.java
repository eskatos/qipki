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
package org.qipki.crypto.symetric;

import java.security.GeneralSecurityException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.qi4j.api.injection.scope.Service;

import org.qipki.crypto.CryptoContext;
import org.qipki.crypto.CryptoFailure;

public class SymetricGeneratorImpl
        implements SymetricGenerator
{

    private final CryptoContext cryptoContext;

    public SymetricGeneratorImpl( @Service CryptoContext cryptoContext )
    {
        this.cryptoContext = cryptoContext;
    }

    @Override
    public SecretKey generateCipheringKey( SymetricCipheringGeneratorParameters params )
    {
        return generateSecretKey( params.algorithm().jcaString(), params.keySize() );
    }

    @Override
    public SecretKey generateSigningKey( SymetricSigningGeneratorParameters params )
    {
        return generateSecretKey( params.algorithm().jcaString(), params.keySize() );
    }

    private SecretKey generateSecretKey( String algoJcaString, int keySize )
    {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance( algoJcaString, cryptoContext.providerName() );
            keyGen.init( keySize );
            return keyGen.generateKey();
        } catch ( GeneralSecurityException ex ) {
            throw new CryptoFailure( "Unable to generate " + algoJcaString + " " + keySize + " SecretKey", ex );
        }
    }

}
