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
package org.qipki.ca.http.presentation.rest.resources;

import java.util.Arrays;
import java.util.HashSet;

import org.qipki.commons.rest.values.representations.RestListValue;

import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.object.ObjectBuilderFactory;

import org.restlet.data.Method;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

public abstract class AbstractListResource
        extends AbstractEntityResource
{

    protected AbstractListResource( @Structure ObjectBuilderFactory obf )
    {
        super( obf );

        setAllowedMethods( new HashSet<Method>( Arrays.asList( new Method[]{ Method.GET, Method.POST } ) ) );
    }

    @Override
    protected final Representation representJson()
    {
        String startParam = getQueryParamValue( "start", "0" );
        int start = Integer.valueOf( startParam );
        return new StringRepresentation( list( start ).toJSON() );
    }

    protected abstract RestListValue list( int start );

    @Override
    protected Representation post( Representation entity, Variant variant )
            throws ResourceException
    {
        // Ignoring Variant on post
        return post( entity );
    }

    @Override
    protected abstract Representation post( Representation entity )
            throws ResourceException;

    /**
     * Shortcut to apply POST/302/GET redirect pattern.
     *
     * @param getURI URI of the created resource
     * @return An EmptyRepresentation with proper HTTP headers to apply the redirection
     */
    protected final Representation redirectToCreatedResource( String getURI )
    {
        getResponse().redirectSeeOther( getURI );
        return new EmptyRepresentation();
    }

}
