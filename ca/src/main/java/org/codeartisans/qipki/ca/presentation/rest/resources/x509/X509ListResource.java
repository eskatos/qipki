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
package org.codeartisans.qipki.ca.presentation.rest.resources.x509;

import org.codeartisans.qipki.ca.application.contexts.x509.X509ListContext;
import org.codeartisans.qipki.ca.domain.x509.X509;
import org.codeartisans.qipki.ca.presentation.rest.RestletValuesFactory;
import org.codeartisans.qipki.ca.presentation.rest.resources.AbstractListResource;
import org.codeartisans.qipki.commons.values.rest.RestListValue;
import org.codeartisans.qipki.commons.values.rest.RestValue;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.qi4j.api.query.Query;

public class X509ListResource
        extends AbstractListResource
{

    @Service
    private RestletValuesFactory valuesFactory;

    public X509ListResource( @Structure ObjectBuilderFactory obf )
    {
        super( obf );
    }

    @Override
    protected RestListValue list( int start )
    {
        // Context
        X509ListContext x509ListCtx = newRootContext().x509ListContext();

        // Interaction
        Query<X509> x509List = x509ListCtx.list( start );

        // Representation
        Iterable<RestValue> values = valuesFactory.asValues( getRootRef(), x509List );
        return valuesFactory.newListRepresentationValue( getReference(), start, values );
    }

}
