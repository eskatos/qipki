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
package org.codeartisans.qipki.ca.presentation.rest.resources;

import java.util.Arrays;
import java.util.HashSet;
import org.codeartisans.qipki.commons.rest.values.representations.RestListValue;
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
        int start = 0;
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
