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

import java.io.IOException;
import org.codeartisans.qipki.ca.application.contexts.x509.X509ListContext;
import org.codeartisans.qipki.ca.domain.x509.X509;
import org.codeartisans.qipki.ca.presentation.rest.RestletValuesFactory;
import org.codeartisans.qipki.ca.presentation.rest.resources.AbstractListResource;
import org.codeartisans.qipki.ca.presentation.rest.uribuilder.UriResolver;
import org.codeartisans.qipki.commons.rest.values.params.X509FactoryParamsValue;
import org.codeartisans.qipki.commons.rest.values.representations.RestListValue;
import org.codeartisans.qipki.commons.rest.values.representations.RestValue;
import org.codeartisans.qipki.commons.rest.values.representations.X509Value;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.qi4j.api.query.Query;
import org.qi4j.api.value.ValueBuilderFactory;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class X509ListResource
        extends AbstractListResource
{

    private static final Logger LOGGER = LoggerFactory.getLogger( X509ListResource.class );
    @Structure
    private ValueBuilderFactory vbf;
    @Service
    private RestletValuesFactory restValuesFactory;

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
        Iterable<RestValue> values = restValuesFactory.asValues( getRootRef(), x509List );
        return restValuesFactory.newListRepresentationValue( getReference(), start, values );
    }

    @Override
    protected Representation post( Representation entity )
            throws ResourceException
    {
        try {

            // Data
            X509FactoryParamsValue params = vbf.newValueFromJSON( X509FactoryParamsValue.class, entity.getText() );

            // Context
            X509ListContext caListCtx = newRootContext().x509ListContext();

            // Interaction
            X509 x509 = caListCtx.createX509( new UriResolver( getRootRef(), params.caUri().get() ).identity(),
                                              new UriResolver( getRootRef(), params.x509ProfileUri().get() ).identity(),
                                              params.pemPkcs10().get() );

            // Redirect to created resource
            X509Value x509Value = restValuesFactory.x509( getRootRef(), x509 );
            return redirectToCreatedResource( x509Value.uri().get() );

        } catch ( IOException ex ) {
            LOGGER.warn( "500: {}", ex.getMessage(), ex );
            throw new ResourceException( Status.SERVER_ERROR_INTERNAL, "Unable to read posted value", ex );
        }
    }

}
