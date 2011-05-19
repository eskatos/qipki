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
package org.qipki.ca.domain.cryptostore;

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

import org.qipki.crypto.io.CryptIO;

import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.This;

public class CryptoStoreMixin
        implements CryptoStoreBehavior
{

    @Service
    private CryptIO cryptIO;
    @This
    private CryptoStoreEntity state;

    @Override
    public KeyStore loadKeyStore()
    {
        return cryptIO.base64DecodeKeyStore( state.payload().get(),
                                             state.storeType().get(),
                                             state.password().get() );
    }

    @Override
    public String storeCertificate( X509Certificate certificate )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public void storeCertificate( String slotId, X509Certificate certificate )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public String storeKeyPair( KeyPair keyPair )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public void storeKeyPair( String slotId, KeyPair keyPair )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

}
