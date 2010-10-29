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
package org.codeartisans.qipki.ca.application.contexts.ca;

import org.codeartisans.qipki.ca.application.WrongParametersBuilder;
import org.codeartisans.qipki.ca.domain.ca.CA;
import org.codeartisans.qipki.ca.domain.ca.CAFactory;
import org.codeartisans.qipki.ca.domain.ca.CARepository;
import org.codeartisans.qipki.ca.domain.ca.root.RootCA;
import org.codeartisans.qipki.ca.domain.ca.sub.SubCA;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStore;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStoreRepository;
import org.codeartisans.qipki.commons.crypto.services.CryptoValuesFactory;
import org.codeartisans.qipki.commons.crypto.values.KeyPairSpecValue;
import org.codeartisans.qipki.core.dci.Context;
import org.codeartisans.qipki.crypto.algorithms.AsymetricAlgorithm;
import org.codeartisans.qipki.crypto.constraints.X500Name;

import org.qi4j.api.query.Query;
import org.qi4j.api.unitofwork.NoSuchEntityException;

public class CAListContext
        extends Context
{

    public Query<CA> list( int start )
    {
        return context.role( CARepository.class ).findAllPaginated( start, 25 );
    }

    public Query<CA> findByName( String name, int start )
    {
        return context.role( CARepository.class ).findByNamePaginated( name, start, 25 );
    }

    public KeyPairSpecValue createKeyPairSpecValue( AsymetricAlgorithm algorithm, Integer length )
    {
        return context.role( CryptoValuesFactory.class ).createKeySpec( algorithm, length );
    }

    public RootCA createRootCA( String cryptoStoreIdentity, String name, int validityDays, @X500Name String distinguishedName, KeyPairSpecValue keySpec )
    {
        CryptoStore cryptoStore = context.role( CryptoStoreRepository.class ).findByIdentity( cryptoStoreIdentity );
        return context.role( CAFactory.class ).createRootCA( name, validityDays, distinguishedName, keySpec, cryptoStore );
    }

    public SubCA createSubCA( String cryptoStoreIdentity, String name, int validityDays, @X500Name String distinguishedName, KeyPairSpecValue keySpec, String parentCaIdentity )
    {
        CryptoStore cryptoStore = context.role( CryptoStoreRepository.class ).findByIdentity( cryptoStoreIdentity );
        CA parentCA = fetchParentCA( parentCaIdentity );
        return context.role( CAFactory.class ).createSubCA( parentCA, name, validityDays, distinguishedName, keySpec, cryptoStore );
    }

    private CA fetchParentCA( String identity )
    {
        try {
            return context.role( CARepository.class ).findByIdentity( identity );
        } catch ( NoSuchEntityException ex ) {
            throw new WrongParametersBuilder().missings( "Parent CA Identity" ).build( ex );
        }
    }

}
