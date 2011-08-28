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
package org.qipki.ca.bootstrap;

import org.qipki.ca.domain.ca.CAFactory;
import org.qipki.ca.domain.ca.CARepository;
import org.qipki.ca.domain.ca.profileassignment.X509ProfileAssignmentEntity;
import org.qipki.ca.domain.ca.profileassignment.X509ProfileAssignmentFactory;
import org.qipki.ca.domain.ca.root.RootCAEntity;
import org.qipki.ca.domain.ca.sub.SubCAEntity;
import org.qipki.ca.domain.crl.CRLEntity;
import org.qipki.ca.domain.crl.CRLFactory;
import org.qipki.ca.domain.cryptostore.CryptoStoreEntity;
import org.qipki.ca.domain.cryptostore.CryptoStoreFactory;
import org.qipki.ca.domain.cryptostore.CryptoStoreRepository;
import org.qipki.ca.domain.escrowedkeypair.EscrowedKeyPairEntity;
import org.qipki.ca.domain.escrowedkeypair.EscrowedKeyPairFactory;
import org.qipki.ca.domain.escrowedkeypair.EscrowedKeyPairRepository;
import org.qipki.ca.domain.revocation.RevocationEntity;
import org.qipki.ca.domain.revocation.RevocationFactory;
import org.qipki.ca.domain.x509.X509Entity;
import org.qipki.ca.domain.x509.X509Factory;
import org.qipki.ca.domain.x509.X509Repository;
import org.qipki.ca.domain.x509profile.X509ProfileEntity;
import org.qipki.ca.domain.x509profile.X509ProfileFactory;
import org.qipki.ca.domain.x509profile.X509ProfileRepository;
import org.qipki.commons.crypto.services.CryptoValuesFactory;
import org.qipki.commons.crypto.values.ValidityIntervalValue;
import org.qipki.core.bootstrap.AutomaticReindexingAssembler;
import org.qipki.core.sideeffects.TracingSideEffect;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qipki.ca.domain.crl.CRLFileService;
import org.qipki.ca.domain.cryptostore.CryptoStoreFileService;

public class CaDomainModuleAssembler
        implements Assembler
{

    @Override
    @SuppressWarnings( "unchecked" )
    public void assemble( ModuleAssembly ma )
            throws AssemblyException
    {
        // Values
        ma.values( ValidityIntervalValue.class );

        // Entities
        ma.entities( CryptoStoreEntity.class,
                     RootCAEntity.class,
                     SubCAEntity.class,
                     CRLEntity.class,
                     X509ProfileAssignmentEntity.class,
                     X509ProfileEntity.class,
                     X509Entity.class,
                     RevocationEntity.class,
                     EscrowedKeyPairEntity.class ).
                visibleIn( Visibility.application );

        // Services
        ma.services( CryptoStoreRepository.class,
                     CryptoStoreFactory.class,
                     CARepository.class,
                     CAFactory.class,
                     CRLFactory.class,
                     X509ProfileAssignmentFactory.class,
                     X509ProfileRepository.class,
                     X509ProfileFactory.class,
                     X509Repository.class,
                     X509Factory.class,
                     RevocationFactory.class,
                     CryptoValuesFactory.class,
                     EscrowedKeyPairFactory.class,
                     EscrowedKeyPairRepository.class ).
                visibleIn( Visibility.application ).
                withSideEffects( TracingSideEffect.class );
        ma.services( CryptoStoreFileService.class,
                     CRLFileService.class ).
                visibleIn( Visibility.module );

        // Automatic reindex
        new AutomaticReindexingAssembler().assemble( ma );
    }

}
