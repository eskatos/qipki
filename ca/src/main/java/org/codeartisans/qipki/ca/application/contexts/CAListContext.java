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
package org.codeartisans.qipki.ca.application.contexts;

import org.codeartisans.qipki.ca.domain.ca.root.RootCAEntity;
import org.codeartisans.qipki.ca.domain.ca.CAFactory;
import org.codeartisans.qipki.ca.domain.ca.CARepository;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStoreEntity;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStoreRepository;
import org.codeartisans.qipki.commons.values.params.CAFactoryParamsValue;
import org.codeartisans.qipki.core.dci.Context;
import org.qi4j.api.query.Query;
import org.qi4j.api.unitofwork.NoSuchEntityException;

public class CAListContext
        extends Context
{

    public Query<RootCAEntity> list( int start )
    {
        return context.role( CARepository.class ).findAllPaginated( start, 25 );
    }

    public RootCAEntity createCA( CAFactoryParamsValue params )
    {
        CryptoStoreEntity keyStore = context.role( CryptoStoreRepository.class ).findByIdentity( params.keyStoreIdentity().get() );
        RootCAEntity parentCA = fetchParentCA( params.parentCaIdentity().get() );
        if ( parentCA == null ) {
            return context.role( CAFactory.class ).createRootCA( params.name().get(), params.distinguishedName().get(), params.keySpec().get(), keyStore );
        }
        return context.role( CAFactory.class ).createSubCA( parentCA, params.name().get(), params.distinguishedName().get(), params.keySpec().get(), keyStore );
    }

    private RootCAEntity fetchParentCA( String identity )
    {
        try {
            return context.role( CARepository.class ).findByIdentity( identity );
        } catch ( NoSuchEntityException ex ) {
            return null;
        }
    }

}
