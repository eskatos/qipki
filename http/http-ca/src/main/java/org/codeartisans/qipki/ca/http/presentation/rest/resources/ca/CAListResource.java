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
