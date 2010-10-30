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
package org.codeartisans.qipki.ca.http.presentation.rest.resources.escrowedkeypair;

import java.io.IOException;

import org.codeartisans.qipki.ca.application.contexts.escrowedkeypair.EscrowedKeyPairListContext;
import org.codeartisans.qipki.ca.domain.escrowedkeypair.EscrowedKeyPair;
import org.codeartisans.qipki.ca.http.presentation.rest.RestletValuesFactory;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.AbstractListResource;
import org.codeartisans.qipki.commons.rest.values.params.EscrowedKeyPairFactoryParamsValue;
import org.codeartisans.qipki.commons.rest.values.representations.EscrowedKeyPairValue;
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

public class EscrowedKeyPairListResource
        extends AbstractListResource
{

    private static final Logger LOGGER = LoggerFactory.getLogger( EscrowedKeyPairListResource.class );
    @Structure
    private ValueBuilderFactory vbf;
    @Service
    private RestletValuesFactory restValuesFactory;

    public EscrowedKeyPairListResource( @Structure ObjectBuilderFactory obf )
    {
        super( obf );
    }

    @Override
    protected RestListValue list( int start )
    {
        // Context
        EscrowedKeyPairListContext escrowListContext = newRootContext().escrowedKeyPairListContext();

        // Interaction
        Query<EscrowedKeyPair> escrowList = escrowListContext.list( start );

        // Representation
        Iterable<RestValue> values = restValuesFactory.asValues( getRootRef(), escrowList );
        return restValuesFactory.newListRepresentationValue( getReference(), start, values );
    }

    @Override
    protected Representation post( Representation entity )
            throws ResourceException
    {
        try {

            // Data
            EscrowedKeyPairFactoryParamsValue params = vbf.newValueFromJSON( EscrowedKeyPairFactoryParamsValue.class, entity.getText() );

            // Context
            EscrowedKeyPairListContext escrowListContext = newRootContext().escrowedKeyPairListContext();

            // Interaction
            EscrowedKeyPair kp = escrowListContext.createEscrowedKeyPair( params.algorithm().get(), params.length().get() );

            // Redirect to created resource
            EscrowedKeyPairValue kpValue = restValuesFactory.escrowedKeyPair( getRootRef(), kp );
            return redirectToCreatedResource( kpValue.uri().get() );

        } catch ( IOException ex ) {
            LOGGER.trace( "500: {}", ex.getMessage(), ex );
            throw new ResourceException( Status.SERVER_ERROR_INTERNAL, "Unable to read posted Value", ex );
        }
    }

}
