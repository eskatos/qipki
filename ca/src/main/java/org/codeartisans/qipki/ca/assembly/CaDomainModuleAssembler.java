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
