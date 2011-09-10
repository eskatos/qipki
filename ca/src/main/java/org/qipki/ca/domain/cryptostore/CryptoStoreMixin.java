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

import java.io.File;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.This;

import org.qipki.core.file.UoWFileFactory;
import org.qipki.crypto.CryptoFailure;
import org.qipki.crypto.io.CryptIO;

public class CryptoStoreMixin
        implements CryptoStoreBehavior
{

    @Service
    private CryptIO cryptIO;
    @Service
    private CryptoStoreFileService fileService;
    @Service
    private UoWFileFactory uowFileFactory;
    @This
    private CryptoStoreEntity me;

    @Override
    public X509Certificate getX509Certificate( String slotId )
    {
        try {
            KeyStore ks = loadReadOnlyKeyStore();
            return ( X509Certificate ) ks.getCertificate( slotId );
        } catch ( KeyStoreException ex ) {
            throw new CryptoFailure( "Unable to load X509 certificate from " + me.name().get() + "/" + slotId, ex );
        }
    }

    @Override
    public PrivateKey getPrivateKey( String slotId )
    {
        try {
            KeyStore ks = loadReadOnlyKeyStore();
            return ( PrivateKey ) ks.getKey( slotId, me.password().get() );
        } catch ( GeneralSecurityException ex ) {
            throw new CryptoFailure( "Unable to load private key from " + me.name().get() + "/" + slotId, ex );
        }
    }

    @Override
    public void storeCertifiedKeyPair( String slotId, PrivateKey privateKey, Certificate... certChain )
    {
        try {
            KeyStore ks = loadKeyStore();
            ks.setEntry( slotId,
                         new KeyStore.PrivateKeyEntry( privateKey, certChain ),
                         new KeyStore.PasswordProtection( me.password().get() ) );
            cryptIO.writeKeyStore( ks, me.password().get(), fileService.getKeyStoreFile( me ) );
        } catch ( KeyStoreException ex ) {
            throw new CryptoFailure( "Unable to store certified keypair in " + me.name().get() + "/" + slotId, ex );
        }
    }

    private KeyStore loadKeyStore()
    {
        File attachedKeyStoreFile = fileService.getKeyStoreFile( me );
        return cryptIO.readKeyStore( attachedKeyStoreFile, me.storeType().get(), me.password().get() );
    }

    private KeyStore loadReadOnlyKeyStore()
    {
        File managedKeyStoreFile = uowFileFactory.createCurrentUoWFile( fileService.getKeyStoreFile( me ) ).asFile();
        return cryptIO.readKeyStore( managedKeyStoreFile, me.storeType().get(), me.password().get() );
    }

}
