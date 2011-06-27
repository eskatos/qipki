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

import org.codeartisans.java.toolbox.StringUtils;

import org.qi4j.api.object.ObjectBuilderFactory;

import org.qipki.ca.application.contexts.RootContext;
import org.qipki.core.dci.InteractionContext;
import org.restlet.data.Form;

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
        String value = new Form( getRequest().getEntity() ).getFirstValue( key );
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

    protected final String getQueryParamValue( String key, String defaultValue )
    {
        String value = getRequest().getResourceRef().getQueryAsForm().getFirstValue( key );
        if ( StringUtils.isEmpty( value ) ) {
            value = defaultValue;
        }
        return value;
    }

}
