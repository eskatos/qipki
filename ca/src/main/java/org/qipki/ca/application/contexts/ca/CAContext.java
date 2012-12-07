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
package org.qipki.ca.application.contexts.ca;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.util.Iterator;
import java.util.Map;
import org.qi4j.api.unitofwork.UnitOfWork;
import org.qi4j.api.unitofwork.concern.UnitOfWorkRetry;
import org.qipki.ca.domain.ca.CA;
import org.qipki.ca.domain.ca.profileassignment.X509ProfileAssignment;
import org.qipki.ca.domain.ca.profileassignment.X509ProfileAssignmentFactory;
import org.qipki.ca.domain.x509profile.X509ProfileRepository;
import org.qipki.commons.crypto.states.KeyEscrowPolicy;
import org.qipki.core.dci.Context;
import org.qipki.crypto.CryptoFailure;
import org.qipki.crypto.io.CryptIO;
import org.qipki.crypto.storage.KeyStoreType;

public class CAContext
    extends Context
{

    public CA ca()
    {
        return context.role( CA.class );
    }

    @UnitOfWorkRetry
    public CA updateCA( Map<String, KeyEscrowPolicy> profileAssignments )
    {
        UnitOfWork uow = module.currentUnitOfWork();
        CA ca = context.role( CA.class );
        // Remove existing assignments
        Iterator<X509ProfileAssignment> it = ca.allowedX509Profiles().iterator();
        while( it.hasNext() )
        {
            X509ProfileAssignment eachCurrentAssignment = it.next();
            it.remove();
            uow.remove( eachCurrentAssignment );
        }
        // Create and add submitted assignments
        X509ProfileAssignmentFactory profileAssignmentFactory = context.role( X509ProfileAssignmentFactory.class );
        X509ProfileRepository profileRepository = context.role( X509ProfileRepository.class );
        for( Map.Entry<String, KeyEscrowPolicy> eachEntry : profileAssignments.entrySet() )
        {
            ca.allowedX509Profiles().add( profileAssignmentFactory.create(
                eachEntry.getValue(),
                profileRepository.findByIdentity( eachEntry.getKey() ) ) );
        }
        return ca;
    }

    public KeyStore exportCaKeyPair( char[] password, KeyStoreType keyStoreType )
    {
        if( keyStoreType == KeyStoreType.PKCS11 )
        {
            throw new UnsupportedOperationException( "Export in PKCS#11 format is not supported, cannot continue." );
        }
        try
        {
            CA ca = context.role( CA.class );
            CryptIO cryptio = context.role( CryptIO.class );
            KeyStore keystore = cryptio.createEmptyKeyStore( keyStoreType );
            keystore.setEntry( ca.name().get(),
                               new KeyStore.PrivateKeyEntry( ca.privateKey(), new Certificate[]
                {
                    ca.certificate()
                } ),
                               new KeyStore.PasswordProtection( password ) );
            return keystore;
        }
        catch( KeyStoreException ex )
        {
            throw new CryptoFailure( "Unable to store CA KeyPair for KeyStore export", ex );
        }
    }

}
