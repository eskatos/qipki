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
package org.qipki.ca.http.presentation.rest.resources.ca;

import java.util.Collections;

import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.object.ObjectBuilderFactory;

import org.qipki.ca.application.contexts.ca.CAContext;
import org.qipki.ca.application.contexts.RootContext;
import org.qipki.ca.domain.crl.CRL;
import org.qipki.ca.http.presentation.rest.api.RestApiService;
import org.qipki.ca.http.presentation.rest.resources.AbstractDCIResource;

import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

public class CRLResource
        extends AbstractDCIResource
{

    public CRLResource( @Structure ObjectBuilderFactory obf, @Service RestApiService restApi )
    {
        super( obf, restApi );
        setAllowedMethods( Collections.singleton( Method.GET ) );
        setNegotiated( false );
        getVariants().add( new Variant( MediaType.TEXT_PLAIN ) );
    }

    @Override
    protected Representation get()
            throws ResourceException
    {
        // Data
        String caIdentity = ensureRequestAttribute( PARAM_IDENTITY, String.class, Status.CLIENT_ERROR_BAD_REQUEST );

        // Context
        RootContext rootCtx = newRootContext();
        CAContext caCtx = rootCtx.caContext( caIdentity );

        // Interaction
        CRL crl = caCtx.ca().crl().get();

        // Representation
        return new FileRepresentation( crl.attachedFile(), MediaType.TEXT_PLAIN );
    }

}
