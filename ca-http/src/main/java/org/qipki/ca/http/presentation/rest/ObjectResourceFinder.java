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
package org.qipki.ca.http.presentation.rest;

import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.object.NoSuchObjectException;

import org.qi4j.api.structure.Module;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.Finder;
import org.restlet.resource.ServerResource;

public class ObjectResourceFinder
        extends Finder
{

    @Structure
    private Module module;

    @Override
    public ServerResource create( Class<? extends ServerResource> targetClass, Request request, Response response )
    {
        try {
            return module.newObject( targetClass );
        } catch ( NoSuchObjectException ex ) {
            return module.newTransient( targetClass );
        }
    }

}
