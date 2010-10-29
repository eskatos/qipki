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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.codeartisans.qipki.ca.application.contexts.ca.CAContext;
import org.codeartisans.qipki.ca.domain.ca.CA;
import org.codeartisans.qipki.ca.http.presentation.rest.RestletValuesFactory;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.AbstractEntityResource;
import org.codeartisans.qipki.ca.http.presentation.rest.uribuilder.CaUriResolver;
import org.codeartisans.qipki.commons.crypto.states.KeyEscrowPolicy;
import org.codeartisans.qipki.commons.rest.values.representations.CAValue;
import org.codeartisans.qipki.commons.rest.values.representations.X509ProfileAssignmentValue;

import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.qi4j.api.value.ValueBuilderFactory;

import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CAResource
        extends AbstractEntityResource
{

    private static final Logger LOGGER = LoggerFactory.getLogger( CAResource.class );
    @Structure
    private ValueBuilderFactory vbf;
    @Service
    private RestletValuesFactory restValuesFactory;

    public CAResource( @Structure ObjectBuilderFactory obf )
    {
        super( obf );
        setAllowedMethods( new HashSet<Method>( Arrays.asList( new Method[]{ Method.GET, Method.POST } ) ) );
    }

    @Override
    protected Representation representJson()
    {

        // Data
        String identity = ensureRequestAttribute( PARAM_IDENTITY, String.class, Status.CLIENT_ERROR_BAD_REQUEST );

        // Context
        CAContext caCtx = newRootContext().caContext( identity );

        // Interaction
        CA ca = caCtx.ca();

        // Representation
        return new StringRepresentation( restValuesFactory.ca( getRootRef(), ca ).toJSON(), MediaType.APPLICATION_JSON );
    }

    @Override
    protected Representation post( Representation entity, Variant variant )
            throws ResourceException
    {
        try {
            // Data
            String identity = ensureRequestAttribute( PARAM_IDENTITY, String.class, Status.CLIENT_ERROR_BAD_REQUEST );
            CAValue caValue = vbf.newValueFromJSON( CAValue.class, entity.getText() );
            Map<String, KeyEscrowPolicy> profileAssignments = new HashMap<String, KeyEscrowPolicy>();
            for ( X509ProfileAssignmentValue eachAssignment : caValue.allowedX509Profiles().get() ) {
                profileAssignments.put( new CaUriResolver( getRootRef(), eachAssignment.x509ProfileUri().get() ).identity(),
                                        eachAssignment.keyEscrowPolicy().get() );
            }

            // Context
            CAContext caCtx = newRootContext().caContext( identity );

            // Interaction
            CA ca = caCtx.updateCA( profileAssignments );

            // Redirect to updated resource
            caValue = restValuesFactory.ca( getRootRef(), ca );
            return redirectToUpdatedResource( caValue.uri().get() );

        } catch ( IOException ex ) {
            LOGGER.warn( "500: {}", ex.getMessage(), ex );
            throw new ResourceException( Status.SERVER_ERROR_INTERNAL, "Unable to read posted value", ex );
        }
    }

}
