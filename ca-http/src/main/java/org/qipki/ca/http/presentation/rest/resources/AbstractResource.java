/*
 * Copyright (c) 2011, Paul Merlin. All Rights Reserved.
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
package org.qipki.ca.http.presentation.rest.resources;

import org.qi4j.api.injection.scope.Service;

import org.qipki.ca.http.presentation.rest.api.RestApiService;

import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class AbstractResource
        extends ServerResource
{

    private final RestApiService restApi;

    public AbstractResource( @Service RestApiService restApi )
    {
        this.restApi = restApi;
    }

    @Override
    protected final void doInit()
            throws ResourceException
    {
        restApi.setApiRootRef( getRootRef() ); // FIXME Find a way to not do it on each request
        super.doInit();
    }

}
