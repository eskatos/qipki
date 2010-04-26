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
package org.codeartisans.qipki.ca.presentation.rest.resources.cryptostore;

import org.codeartisans.qipki.ca.application.contexts.cryptostore.CryptoStoreListContext;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStore;
import org.codeartisans.qipki.ca.presentation.rest.RestletValuesFactory;
import org.codeartisans.qipki.ca.presentation.rest.resources.AbstractListResource;
import org.codeartisans.qipki.commons.values.rest.RestListValue;
import org.codeartisans.qipki.commons.values.rest.RestValue;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.qi4j.api.query.Query;

public class CryptoStoreListResource
        extends AbstractListResource
{

    @Service
    private RestletValuesFactory restValuesFactory;

    public CryptoStoreListResource( @Structure ObjectBuilderFactory obf )
    {
        super( obf );
    }

    @Override
    protected RestListValue list( int start )
    {
        // Context
        CryptoStoreListContext csListCtx = newRootContext().cryptoStoreListContext();

        // Interaction
        Query<CryptoStore> csList = csListCtx.list( start );

        // Representation
        Iterable<RestValue> values = restValuesFactory.asValues( getRootRef(), csList );
        return restValuesFactory.newListRepresentationValue( getReference(), start, values );
    }

}
