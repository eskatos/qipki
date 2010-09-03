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
package org.codeartisans.qipki.ca.application.contexts.ca;

import org.codeartisans.qipki.ca.domain.ca.CA;
import org.codeartisans.qipki.ca.domain.ca.CAFactory;
import org.codeartisans.qipki.ca.domain.ca.CARepository;
import org.codeartisans.qipki.ca.domain.ca.root.RootCA;
import org.codeartisans.qipki.ca.domain.ca.sub.SubCA;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStore;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStoreRepository;
import org.codeartisans.qipki.ca.presentation.rest.resources.WrongParametersBuilder;
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

    public KeyPairSpecValue createKeyPairSpecValue( AsymetricAlgorithm algorithm, Integer length )
    {
        return context.role( CryptoValuesFactory.class ).createKeySpec( algorithm, length );
    }

    public RootCA createRootCA( String cryptoStoreIdentity, String name, @X500Name String distinguishedName, KeyPairSpecValue keySpec )
    {
        CryptoStore cryptoStore = context.role( CryptoStoreRepository.class ).findByIdentity( cryptoStoreIdentity );
        return context.role( CAFactory.class ).createRootCA( name, distinguishedName, keySpec, cryptoStore );
    }

    public SubCA createSubCA( String cryptoStoreIdentity, String name, @X500Name String distinguishedName, KeyPairSpecValue keySpec, String parentCaIdentity )
    {
        CryptoStore cryptoStore = context.role( CryptoStoreRepository.class ).findByIdentity( cryptoStoreIdentity );
        CA parentCA = fetchParentCA( parentCaIdentity );
        return context.role( CAFactory.class ).createSubCA( parentCA, name, distinguishedName, keySpec, cryptoStore );
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
