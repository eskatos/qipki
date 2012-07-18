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
package org.qipki.ca.http.presentation.rest.resources.cryptostore;

import java.io.IOException;

import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.query.Query;
import org.qi4j.api.structure.Module;

import org.qipki.ca.application.contexts.cryptostore.CryptoStoreListContext;
import org.qipki.ca.domain.cryptostore.CryptoStore;
import org.qipki.ca.http.presentation.rest.RestletValuesFactory;
import org.qipki.ca.http.presentation.rest.api.RestApiService;
import org.qipki.ca.http.presentation.rest.resources.AbstractListResource;
import org.qipki.commons.rest.values.params.CryptoStoreFactoryParamsValue;
import org.qipki.commons.rest.values.representations.CryptoStoreValue;
import org.qipki.commons.rest.values.representations.RestListValue;
import org.qipki.commons.rest.values.representations.RestValue;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CryptoStoreListResource
        extends AbstractListResource
{

    private static final Logger LOGGER = LoggerFactory.getLogger( CryptoStoreListResource.class );
    @Service
    private RestletValuesFactory restValuesFactory;

    public CryptoStoreListResource( @Structure Module module, @Service RestApiService restApi )
    {
        super( module, restApi );
    }

    @Override
    protected RestListValue list( int start )
    {
        // Context
        CryptoStoreListContext csListCtx = newRootContext().cryptoStoreListContext();

        // Interaction
        Query<CryptoStore> csList = csListCtx.list( start );

        // Representation
        Iterable<RestValue> values = restValuesFactory.asValues( csList );
        return restValuesFactory.newListRepresentationValue( getReference(), start, values );
    }

    @Override
    protected Representation post( Representation entity )
            throws ResourceException
    {
        try {

            // Data
            CryptoStoreFactoryParamsValue params = module.newValueFromJSON( CryptoStoreFactoryParamsValue.class, entity.getText() );

            // Context
            CryptoStoreListContext csListCtx = newRootContext().cryptoStoreListContext();

            // Interaction
            CryptoStore cs = csListCtx.createCryptoStore( params.name().get(), params.storeType().get(), params.password().get() );

            // Redirect to created resource
            CryptoStoreValue csValue = restValuesFactory.cryptoStore( cs );
            return redirectToCreatedResource( csValue.uri().get() );

        } catch ( IOException ex ) {
            LOGGER.trace( "500: {}", ex.getMessage(), ex );
            throw new ResourceException( Status.SERVER_ERROR_INTERNAL, "Unable to read posted Value", ex );
        }
    }

}
