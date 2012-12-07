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
package org.qipki.ca.application.contexts;

import org.qi4j.api.injection.scope.Service;
import org.qipki.ca.application.contexts.ca.CAContext;
import org.qipki.ca.application.contexts.ca.CAListContext;
import org.qipki.ca.application.contexts.cryptostore.CryptoStoreContext;
import org.qipki.ca.application.contexts.cryptostore.CryptoStoreListContext;
import org.qipki.ca.application.contexts.escrowedkeypair.EscrowedKeyPairContext;
import org.qipki.ca.application.contexts.escrowedkeypair.EscrowedKeyPairListContext;
import org.qipki.ca.application.contexts.x509.X509Context;
import org.qipki.ca.application.contexts.x509.X509ListContext;
import org.qipki.ca.application.contexts.x509profile.X509ProfileContext;
import org.qipki.ca.application.contexts.x509profile.X509ProfileListContext;
import org.qipki.ca.domain.ca.CA;
import org.qipki.ca.domain.ca.CAFactory;
import org.qipki.ca.domain.ca.CARepository;
import org.qipki.ca.domain.ca.profileassignment.X509ProfileAssignmentFactory;
import org.qipki.ca.domain.cryptostore.CryptoStore;
import org.qipki.ca.domain.cryptostore.CryptoStoreFactory;
import org.qipki.ca.domain.cryptostore.CryptoStoreRepository;
import org.qipki.ca.domain.escrowedkeypair.EscrowedKeyPair;
import org.qipki.ca.domain.escrowedkeypair.EscrowedKeyPairFactory;
import org.qipki.ca.domain.escrowedkeypair.EscrowedKeyPairRepository;
import org.qipki.ca.domain.x509.X509;
import org.qipki.ca.domain.x509.X509Factory;
import org.qipki.ca.domain.x509.X509Repository;
import org.qipki.ca.domain.x509profile.X509Profile;
import org.qipki.ca.domain.x509profile.X509ProfileFactory;
import org.qipki.ca.domain.x509profile.X509ProfileRepository;
import org.qipki.commons.crypto.services.CryptoValuesFactory;
import org.qipki.commons.crypto.services.X509ExtensionsValueFactory;
import org.qipki.core.dci.Context;
import org.qipki.crypto.io.CryptIO;

/**
 * QUID Use ServiceReference for lazy service instanciation ?
 */
public class RootContext
    extends Context
{

    @Service
    private CryptoStoreRepository cryptoStoreRepository;
    @Service
    private CryptoStoreFactory cryptoStoreFactory;
    @Service
    private CARepository caRepository;
    @Service
    private CAFactory caFactory;
    @Service
    private X509ExtensionsValueFactory x509ExtensionsValueFactory;
    @Service
    private X509ProfileFactory x509ProfileFactory;
    @Service
    private X509ProfileRepository x509ProfileRepository;
    @Service
    private X509ProfileAssignmentFactory x509ProfileAssignmentFactory;
    @Service
    private X509Factory x509Factory;
    @Service
    private X509Repository x509Repository;
    @Service
    private CryptIO cryptIO;
    @Service
    private CryptoValuesFactory cryptoValuesFactory;
    @Service
    private EscrowedKeyPairRepository escrowedKeyPairRepository;
    @Service
    private EscrowedKeyPairFactory escrowedKeyPairFactory;

    public CryptoStoreListContext cryptoStoreListContext()
    {
        context.playRoles( cryptoStoreRepository, CryptoStoreRepository.class );
        context.playRoles( cryptoStoreFactory, CryptoStoreFactory.class );
        return subContext( CryptoStoreListContext.class );
    }

    public CryptoStoreContext cryptoStoreContext( String identity )
    {
        CryptoStore ks = cryptoStoreRepository.findByIdentity( identity );
        context.playRoles( ks, CryptoStore.class );
        return subContext( CryptoStoreContext.class );
    }

    public CAListContext caListContext()
    {
        context.playRoles( caRepository, CARepository.class );
        context.playRoles( caFactory, CAFactory.class );
        context.playRoles( cryptoStoreRepository, CryptoStoreRepository.class );
        context.playRoles( cryptoValuesFactory, CryptoValuesFactory.class );
        return subContext( CAListContext.class );
    }

    public CAContext caContext( String identity )
    {
        CA ca = caRepository.findByIdentity( identity );
        context.playRoles( ca, CA.class );
        context.playRoles( x509Repository, X509Repository.class );
        context.playRoles( x509ProfileAssignmentFactory, X509ProfileAssignmentFactory.class );
        context.playRoles( x509ProfileRepository, X509ProfileRepository.class );
        context.playRoles( cryptIO, CryptIO.class );
        return subContext( CAContext.class );
    }

    public X509ProfileListContext x509ProfileListContext()
    {
        context.playRoles( x509ExtensionsValueFactory, X509ExtensionsValueFactory.class );
        context.playRoles( x509ProfileRepository, X509ProfileRepository.class );
        context.playRoles( x509ProfileFactory, X509ProfileFactory.class );
        return subContext( X509ProfileListContext.class );
    }

    public X509ProfileContext x509ProfileContext( String identity )
    {
        X509Profile x509Profile = x509ProfileRepository.findByIdentity( identity );
        context.playRoles( x509Profile, X509Profile.class );
        return subContext( X509ProfileContext.class );
    }

    public X509ListContext x509ListContext()
    {
        context.playRoles( x509Repository, X509Repository.class );
        context.playRoles( x509Factory, X509Factory.class );
        context.playRoles( x509ProfileRepository, X509ProfileRepository.class );
        context.playRoles( escrowedKeyPairRepository, EscrowedKeyPairRepository.class );
        context.playRoles( caRepository, CARepository.class );
        return subContext( X509ListContext.class );
    }

    public X509Context x509Context( String identity )
    {
        X509 x509 = x509Repository.findByIdentity( identity );
        context.playRoles( x509, X509.class );
        context.playRoles( escrowedKeyPairRepository, EscrowedKeyPairRepository.class );
        return subContext( X509Context.class );
    }

    public EscrowedKeyPairListContext escrowedKeyPairListContext()
    {
        context.playRoles( escrowedKeyPairRepository, EscrowedKeyPairRepository.class );
        context.playRoles( escrowedKeyPairFactory, EscrowedKeyPairFactory.class );
        return subContext( EscrowedKeyPairListContext.class );
    }

    public EscrowedKeyPairContext escrowedKeyPairContext( String identity )
    {
        EscrowedKeyPair ekp = escrowedKeyPairRepository.findByIdentity( identity );
        context.playRoles( ekp, EscrowedKeyPair.class );
        return subContext( EscrowedKeyPairContext.class );
    }

}
