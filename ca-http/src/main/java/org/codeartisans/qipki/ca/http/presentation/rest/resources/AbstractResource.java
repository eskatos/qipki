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
package org.codeartisans.qipki.ca.http.presentation.rest.resources;

import org.codeartisans.java.toolbox.StringUtils;
import org.codeartisans.qipki.ca.application.contexts.RootContext;
import org.codeartisans.qipki.core.dci.InteractionContext;

import org.qi4j.api.object.ObjectBuilderFactory;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractResource
        extends ServerResource
{

    private static final Logger LOGGER = LoggerFactory.getLogger( AbstractResource.class );

    public static final String PARAM_IDENTITY = "identity";

    protected final ObjectBuilderFactory obf;

    public AbstractResource( ObjectBuilderFactory obf )
    {
        this.obf = obf;
    }

    protected final RootContext newRootContext()
    {
        return obf.newObjectBuilder( RootContext.class ).use( new InteractionContext() ).newInstance();
    }

    protected final <T> T ensureRequestAttribute( String key, Class<? extends T> type, Status ifAbsent )
    {
        Object obj = getRequest().getAttributes().get( key );
        if ( obj == null ) {
            LOGGER.trace( "{}: No request attribute named {}", ifAbsent, key );
            throw new ResourceException( ifAbsent );
        }
        try {
            return type.cast( obj );
        } catch ( ClassCastException ex ) {
            LOGGER.trace( "{}: Request attribute named {} is of the wrong type", new Object[]{ ifAbsent, key }, ex );
            throw new ResourceException( ifAbsent, ex );
        }
    }

    protected final String ensureFormFirstValue( String key, Status ifAbsent )
    {
        String value = getRequest().getEntityAsForm().getFirstValue( key );
        if ( StringUtils.isEmpty( value ) ) {
            LOGGER.trace( "{}: No form first value named {}", ifAbsent, key );
            throw new ResourceException( ifAbsent );
        }
        return value;
    }

    protected final String ensureQueryParamValue( String key, Status ifAbsent )
    {
        String value = getRequest().getResourceRef().getQueryAsForm().getFirstValue( key );
        if ( StringUtils.isEmpty( value ) ) {
            LOGGER.trace( "{}: No query parameter named {}", ifAbsent, key );
            throw new ResourceException( ifAbsent );
        }
        return value;
    }

}
