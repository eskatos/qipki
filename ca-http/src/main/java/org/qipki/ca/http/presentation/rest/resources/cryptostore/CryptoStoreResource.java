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

import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.structure.Module;

import org.qipki.ca.application.contexts.cryptostore.CryptoStoreContext;
import org.qipki.ca.domain.cryptostore.CryptoStore;
import org.qipki.ca.http.presentation.rest.RestletValuesFactory;
import org.qipki.ca.http.presentation.rest.api.RestApiService;
import org.qipki.ca.http.presentation.rest.resources.AbstractEntityResource;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

public class CryptoStoreResource
        extends AbstractEntityResource
{

    @Service
    private RestletValuesFactory restValuesFactory;

    public CryptoStoreResource( @Structure Module module, @Service RestApiService restApi )
    {
        super( module, restApi );
    }

    @Override
    protected Representation representJson()
    {
        // Data
        String identity = ensureRequestAttribute( PARAM_IDENTITY, String.class, Status.CLIENT_ERROR_BAD_REQUEST );

        // Context
        CryptoStoreContext csCtx = newRootContext().cryptoStoreContext( identity );

        // Interaction
        CryptoStore cs = csCtx.cryptoStore();

        // Representation
        return new StringRepresentation( restValuesFactory.cryptoStore( cs ).toString(), MediaType.APPLICATION_JSON );
    }

}
