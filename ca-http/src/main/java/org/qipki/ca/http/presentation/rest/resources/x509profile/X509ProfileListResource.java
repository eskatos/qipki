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
package org.qipki.ca.http.presentation.rest.resources.x509profile;

import java.io.IOException;

import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.query.Query;
import org.qi4j.api.structure.Module;

import org.qipki.ca.application.contexts.x509profile.X509ProfileListContext;
import org.qipki.ca.domain.x509profile.X509Profile;
import org.qipki.ca.http.presentation.rest.RestletValuesFactory;
import org.qipki.ca.http.presentation.rest.api.RestApiService;
import org.qipki.ca.http.presentation.rest.resources.AbstractListResource;
import org.qipki.commons.rest.values.params.X509ProfileFactoryParamsValue;
import org.qipki.commons.rest.values.representations.RestListValue;
import org.qipki.commons.rest.values.representations.RestValue;
import org.qipki.commons.rest.values.representations.X509ProfileValue;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class X509ProfileListResource
        extends AbstractListResource
{

    private static final Logger LOGGER = LoggerFactory.getLogger( X509ProfileListResource.class );
    @Service
    private RestletValuesFactory restValuesFactory;

    public X509ProfileListResource( @Structure Module module, @Service RestApiService restApi )
    {
        super( module, restApi );
    }

    @Override
    protected RestListValue list( int start )
    {
        // Context
        X509ProfileListContext x509ProfileListCtx = newRootContext().x509ProfileListContext();

        // Interaction
        Query<X509Profile> x509ProfileList = x509ProfileListCtx.list( start );

        // Representation
        Iterable<RestValue> values = restValuesFactory.asValues( x509ProfileList );
        return restValuesFactory.newListRepresentationValue( getReference(), start, values );
    }

    @Override
    protected Representation post( Representation entity )
            throws ResourceException
    {
        try {

            // Data
            X509ProfileFactoryParamsValue params = module.newValueFromJSON( X509ProfileFactoryParamsValue.class, entity.getText() );

            // Context
            X509ProfileListContext x509ProfileListCtx = newRootContext().x509ProfileListContext();

            // Interaction
            X509Profile x509Profile = x509ProfileListCtx.createX509Profile( params.name().get(),
                                                                            params.validityDays().get(),
                                                                            params.netscapeCertComment().get(),
                                                                            params.keyUsages().get(),
                                                                            params.extendedKeyUsages().get(),
                                                                            params.netscapeCertTypes().get(),
                                                                            params.basicConstraints().get(),
                                                                            params.nameConstraints().get() );

            // Redirect to created resource
            X509ProfileValue x509ProfileValue = restValuesFactory.x509Profile( x509Profile );
            return redirectToCreatedResource( x509ProfileValue.uri().get() );

        } catch ( IOException ex ) {
            LOGGER.warn( "500: {}", ex.getMessage(), ex );
            throw new ResourceException( Status.SERVER_ERROR_INTERNAL, "Unable to read posted value", ex );
        }
    }

}
