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
package org.codeartisans.qipki.ca.assembly;

import org.codeartisans.qipki.ca.domain.ca.CAFactory;
import org.codeartisans.qipki.ca.domain.ca.CARepository;
import org.codeartisans.qipki.ca.domain.ca.profileassignment.X509ProfileAssignmentEntity;
import org.codeartisans.qipki.ca.domain.ca.profileassignment.X509ProfileAssignmentFactory;
import org.codeartisans.qipki.ca.domain.ca.root.RootCAEntity;
import org.codeartisans.qipki.ca.domain.ca.sub.SubCAEntity;
import org.codeartisans.qipki.ca.domain.crl.CRLEntity;
import org.codeartisans.qipki.ca.domain.crl.CRLFactory;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStoreEntity;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStoreFactory;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStoreRepository;
import org.codeartisans.qipki.ca.domain.escrowedkeypair.EscrowedKeyPairEntity;
import org.codeartisans.qipki.ca.domain.escrowedkeypair.EscrowedKeyPairFactory;
import org.codeartisans.qipki.ca.domain.escrowedkeypair.EscrowedKeyPairRepository;
import org.codeartisans.qipki.ca.domain.revocation.RevocationEntity;
import org.codeartisans.qipki.ca.domain.revocation.RevocationFactory;
import org.codeartisans.qipki.ca.domain.x509.X509Entity;
import org.codeartisans.qipki.ca.domain.x509.X509Factory;
import org.codeartisans.qipki.ca.domain.x509.X509Repository;
import org.codeartisans.qipki.ca.domain.x509profile.X509ProfileEntity;
import org.codeartisans.qipki.ca.domain.x509profile.X509ProfileFactory;
import org.codeartisans.qipki.ca.domain.x509profile.X509ProfileRepository;
import org.codeartisans.qipki.commons.crypto.services.CryptoValuesFactory;
import org.codeartisans.qipki.commons.crypto.values.ValidityIntervalValue;
import org.codeartisans.qipki.core.sideeffects.TracingSideEffect;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;

public class CaDomainModuleAssembler
        implements Assembler
{

    @Override
    @SuppressWarnings( "unchecked" )
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        // Values
        module.addValues( ValidityIntervalValue.class );

        // Entities
        module.addEntities( CryptoStoreEntity.class,
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
        module.addServices( CryptoStoreRepository.class,
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
    }

}
