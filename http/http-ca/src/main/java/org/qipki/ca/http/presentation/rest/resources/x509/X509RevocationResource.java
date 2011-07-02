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

import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.qi4j.api.value.ValueBuilderFactory;

import org.qipki.ca.application.contexts.RootContext;
import org.qipki.ca.application.contexts.x509.X509Context;
import org.qipki.ca.domain.revocation.Revocation;
import org.qipki.ca.http.presentation.rest.RestletValuesFactory;
import org.qipki.ca.http.presentation.rest.api.RestApiService;
import org.qipki.ca.http.presentation.rest.resources.AbstractDCIResource;
import org.qipki.commons.rest.values.params.X509RevocationParamsValue;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class X509RevocationResource
        extends AbstractDCIResource
{

    private static final Logger LOGGER = LoggerFactory.getLogger( X509RevocationResource.class );
    @Structure
    private ValueBuilderFactory vbf;
    @Service
    private RestletValuesFactory valuesFactory;

    public X509RevocationResource( @Structure ObjectBuilderFactory obf, @Service RestApiService restApi )
    {
        super( obf, restApi );
        setNegotiated( false );
    }

    @Override
    protected Representation post( Representation entity )
            throws ResourceException
    {
        try {

            // Data
            String x509Identity = ensureRequestAttribute( PARAM_IDENTITY, String.class, Status.CLIENT_ERROR_BAD_REQUEST );
            X509RevocationParamsValue params = vbf.newValueFromJSON( X509RevocationParamsValue.class, entity.getText() );

            // Context
            RootContext rootCtx = newRootContext();
            X509Context x509Ctx = rootCtx.x509Context( x509Identity );

            // Interaction
            Revocation revocation = x509Ctx.revoke( params.reason().get() );

            // Representation
            return new StringRepresentation( valuesFactory.revocation( revocation ).toJSON(), MediaType.APPLICATION_JSON );

        } catch ( IOException ex ) {
            LOGGER.warn( "500: {}", ex.getMessage(), ex );
            throw new ResourceException( Status.SERVER_ERROR_INTERNAL, "Unable to read posted value", ex );
        }
    }

}
