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
package org.codeartisans.qipki.ca.http.presentation.rest.resources.ca;

import java.io.IOException;

import org.codeartisans.java.toolbox.StringUtils;
import org.codeartisans.qipki.ca.application.contexts.ca.CAListContext;
import org.codeartisans.qipki.ca.domain.ca.CA;
import org.codeartisans.qipki.ca.http.presentation.rest.RestletValuesFactory;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.AbstractListResource;
import org.codeartisans.qipki.ca.http.presentation.rest.uribuilder.CaUriResolver;
import org.codeartisans.qipki.commons.rest.values.params.CAFactoryParamsValue;
import org.codeartisans.qipki.commons.rest.values.representations.CAValue;
import org.codeartisans.qipki.commons.rest.values.representations.RestListValue;
import org.codeartisans.qipki.commons.rest.values.representations.RestValue;

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

public class CAListResource
        extends AbstractListResource
{

    private static final Logger LOGGER = LoggerFactory.getLogger( CAListResource.class );

    @Structure
    private ValueBuilderFactory vbf;

    @Service
    private RestletValuesFactory restValuesFactory;

    public CAListResource( @Structure ObjectBuilderFactory obf )
    {
        super( obf );
    }

    @Override
    protected RestListValue list( int start )
    {
        // Context
        CAListContext caListCtx = newRootContext().caListContext();

        // Interaction
        Query<CA> caList = caListCtx.list( start );

        // Representation
        Iterable<RestValue> values = restValuesFactory.asValues( getRootRef(), caList );
        return restValuesFactory.newListRepresentationValue( getReference(), start, values );
    }

    @Override
    protected Representation post( Representation entity )
            throws ResourceException
    {
        try {

            // Data
            CAFactoryParamsValue params = vbf.newValueFromJSON( CAFactoryParamsValue.class, entity.getText() );

            // Context
            CAListContext caListCtx = newRootContext().caListContext();

            // Interaction
            CA ca;
            CaUriResolver cryptoStoreResolver = new CaUriResolver( getRootRef(), params.cryptoStoreUri().get() );
            if ( StringUtils.isEmpty( params.parentCaUri().get() ) ) {
                ca = caListCtx.createRootCA( cryptoStoreResolver.identity(),
                                             params.name().get(), params.validityDays().get(),
                                             params.distinguishedName().get(), params.keySpec().get() );
            } else {
                CaUriResolver parentCaResolver = new CaUriResolver( getRootRef(), params.parentCaUri().get() );
                ca = caListCtx.createSubCA( cryptoStoreResolver.identity(),
                                            params.name().get(), params.validityDays().get(),
                                            params.distinguishedName().get(), params.keySpec().get(), parentCaResolver.identity() );
            }

            // Redirect to created resource
            CAValue caValue = restValuesFactory.ca( getRootRef(), ca );
            return redirectToCreatedResource( caValue.uri().get() );

        } catch ( IOException ex ) {
            LOGGER.trace( "500: {}", ex.getMessage(), ex );
            throw new ResourceException( Status.SERVER_ERROR_INTERNAL, "Unable to read posted entity", ex );
        }
    }

}
