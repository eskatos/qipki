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

import java.io.IOException;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;

import org.qi4j.api.object.ObjectBuilderFactory;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class AbstractEntityResource
        extends AbstractResource
{

    private static final Logger LOGGER = LoggerFactory.getLogger( AbstractEntityResource.class );

    protected AbstractEntityResource( ObjectBuilderFactory obf )
    {
        super( obf );
        getVariants().addAll( Arrays.asList(
                new Variant( MediaType.TEXT_HTML ),
                new Variant( MediaType.APPLICATION_JSON ) ) );
        setNegotiated( true );
    }

    @Override
    protected Representation get( Variant variant )
            throws ResourceException
    {
        if ( MediaType.APPLICATION_JSON.equals( variant.getMediaType() ) ) {
            LOGGER.trace( "Will represent {}", MediaType.APPLICATION_JSON );
            return representJson();
        } else if ( MediaType.TEXT_HTML.equals( variant.getMediaType() ) ) {
            LOGGER.trace( "Will represent {}", MediaType.TEXT_HTML );
            return representHtml();
        }
        throw new ResourceException( Status.CLIENT_ERROR_NOT_FOUND );
    }

    protected abstract Representation representJson();

    /**
     * TODO Provide a "json browser" rendering by default
     */
    protected Representation representHtml()
    {
        try {

            DomRepresentation representation = new DomRepresentation( MediaType.TEXT_HTML );

            Document d = representation.getDocument();

            Element html = d.createElement( "html" );
            Element body = d.createElement( "body" );
            Element title = d.createElement( "h1" );
            Element json = d.createElement( "pre" );
            
            title.setTextContent( getReference().toString() );
            json.setTextContent( new JSONObject( representJson().getText() ).toString( 2 ) );

            body.appendChild( title );
            body.appendChild( json );

            html.appendChild( body );

            d.appendChild( html );
            d.normalizeDocument();

            return representation;

        } catch ( JSONException ex ) {
            throw new ResourceException( ex );
        } catch ( IOException ex ) {
            throw new ResourceException( ex );
        }
    }

    /**
     * Shortcut to apply POST/302/GET redirect pattern.
     *
     * @param getURI URI of the updated resource
     * @return An EmptyRepresentation with proper HTTP headers to apply the redirection
     */
    protected final Representation redirectToUpdatedResource( String getURI )
    {
        getResponse().redirectSeeOther( getURI );
        return new EmptyRepresentation();
    }

}
