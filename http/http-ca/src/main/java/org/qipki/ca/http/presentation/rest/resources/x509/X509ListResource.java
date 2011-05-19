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
package org.qipki.ca.http.presentation.rest.resources.x509;

import java.io.IOException;

import org.qipki.ca.application.contexts.x509.X509ListContext;
import org.qipki.ca.domain.x509.X509;
import org.qipki.ca.http.presentation.rest.RestletValuesFactory;
import org.qipki.ca.http.presentation.rest.resources.AbstractListResource;
import org.qipki.ca.http.presentation.rest.uribuilder.CaUriResolver;
import org.qipki.commons.rest.values.params.X509FactoryParamsValue;
import org.qipki.commons.rest.values.representations.RestListValue;
import org.qipki.commons.rest.values.representations.RestValue;
import org.qipki.commons.rest.values.representations.X509Value;

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
            Boolean escrowed = params.escrowedKeyPairUri().get() != null;

            // Context
            X509ListContext caListCtx = newRootContext().x509ListContext();

            // Interaction
            X509 x509;
            if ( escrowed ) {
                x509 = caListCtx.createX509( new CaUriResolver( getRootRef(), params.caUri().get() ).identity(),
                                             new CaUriResolver( getRootRef(), params.x509ProfileUri().get() ).identity(),
                                             new CaUriResolver( getRootRef(), params.escrowedKeyPairUri().get() ).identity(),
                                             params.distinguishedName().get() );
            } else {
                x509 = caListCtx.createX509( new CaUriResolver( getRootRef(), params.caUri().get() ).identity(),
                                             new CaUriResolver( getRootRef(), params.x509ProfileUri().get() ).identity(),
                                             params.pemPkcs10().get() );
            }

            // Redirect to created resource
            X509Value x509Value = restValuesFactory.x509( getRootRef(), x509 );
            return redirectToCreatedResource( x509Value.uri().get() );

        } catch ( IOException ex ) {
            LOGGER.warn( "500: {}", ex.getMessage(), ex );
            throw new ResourceException( Status.SERVER_ERROR_INTERNAL, "Unable to read posted value", ex );
        }
    }

}
