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
package org.codeartisans.qipki.ca.presentation.rest.resources.ca;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.codeartisans.qipki.ca.application.contexts.ca.CAContext;
import org.codeartisans.qipki.ca.domain.ca.CA;
import org.codeartisans.qipki.ca.presentation.rest.RestletValuesFactory;
import org.codeartisans.qipki.ca.presentation.rest.resources.AbstractEntityResource;
import org.codeartisans.qipki.ca.presentation.rest.uribuilder.UriResolver;
import org.codeartisans.qipki.commons.states.KeyEscrowPolicy;
import org.codeartisans.qipki.commons.values.rest.CAValue;
import org.codeartisans.qipki.commons.values.rest.X509ProfileAssignmentValue;
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
                profileAssignments.put( new UriResolver( getRootRef(), eachAssignment.x509ProfileUri().get() ).identity(),
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
