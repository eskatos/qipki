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
package org.qipki.crypto.jce;

import java.security.InvalidKeyException;
import javax.crypto.SecretKey;

import org.qi4j.api.injection.scope.Service;

import org.qipki.crypto.CryptoFailure;
import org.qipki.crypto.algorithms.BlockCipherModeOfOperation;
import org.qipki.crypto.algorithms.BlockCipherPadding;
import org.qipki.crypto.algorithms.SymetricAlgorithm;
import org.qipki.crypto.cipher.BlockCipher;
import org.qipki.crypto.cipher.CipherFactory;
import org.qipki.crypto.symetric.SymetricGenerator;
import org.qipki.crypto.symetric.SymetricGeneratorParameters;

/**
 * Implementation of {@link JceDetector} that generate and try to use a 256bits AES key.
 */
public class JceDetectorImpl
        implements JceDetector
{

    private final SymetricGenerator symGen;
    private final CipherFactory cipherFactory;

    public JceDetectorImpl( @Service SymetricGenerator symGen, @Service CipherFactory cipherFactory )
    {
        this.symGen = symGen;
        this.cipherFactory = cipherFactory;
    }

    @Override
    public boolean areJceInstalled()
    {
        try {
            SecretKey key = symGen.generateSecretKey( new SymetricGeneratorParameters( SymetricAlgorithm.AES, 256 ) );
            BlockCipher aesSicPkcs7 = cipherFactory.newBlockCipher( SymetricAlgorithm.AES, BlockCipherModeOfOperation.SIC, BlockCipherPadding.PKCS7 );
            aesSicPkcs7.cipher( new byte[]{}, key );
            return true;
        } catch ( CryptoFailure ex ) {
            if ( ex.getCause() != null && InvalidKeyException.class.isAssignableFrom( ex.getCause().getClass() ) ) {
                return false;
            }
            return true;
        }
    }

    @Override
    public void ensureJceAreInstalled()
    {
        if ( !areJceInstalled() ) {
            throw new CryptoFailure( "JCE must be installed" );
        }
    }

}
