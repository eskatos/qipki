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
package org.qipki.crypto.asymetric;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import org.qi4j.api.injection.scope.Service;
import org.qipki.crypto.CryptoContext;
import org.qipki.crypto.CryptoFailure;

public class AsymetricGeneratorImpl
    implements AsymetricGenerator
{

    private final CryptoContext cryptoContext;

    public AsymetricGeneratorImpl( @Service CryptoContext cryptoContext )
    {
        this.cryptoContext = cryptoContext;
    }

    @Override
    public KeyPair generateKeyPair( AsymetricGeneratorParameters params )
    {
        try
        {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance( params.algorithm().jcaString(), cryptoContext.providerName() );
            keyGen.initialize( params.keySize() );
            return keyGen.generateKeyPair();
        }
        catch( GeneralSecurityException ex )
        {
            throw new CryptoFailure( "Unable to generate " + params.algorithm().jcaString() + " " + params.keySize() + " KeyPair", ex );
        }
    }

}
