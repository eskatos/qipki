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
package org.codeartisans.qipki.server.presentation.rest.resources;

import java.io.IOException;
import java.util.Arrays;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class AbstractResource
        extends ServerResource
{

    private static final Logger LOGGER = LoggerFactory.getLogger( AbstractResource.class );

    protected AbstractResource()
    {
        super();
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
            title.setTextContent( getReference().toString() );
            Element json = d.createElement( "pre" );
            json.setTextContent( representJson().getText() );
            body.appendChild( title );
            body.appendChild( json );
            html.appendChild( body );
            d.appendChild( html );

            d.normalizeDocument();

            return representation;

        } catch ( IOException ex ) {
            throw new ResourceException( ex );
        }
    }

    protected final <T> T ensureRequestAttribute( String key, Class<? extends T> type, Status ifAbsent )
    {

        Object obj = getRequest().getAttributes().get( key );
        if ( obj == null ) {
            LOGGER.debug( "{}: Required request attribute {} is null", ifAbsent.getCode(), key );
            throw new ResourceException( ifAbsent );
        }
        LOGGER.trace( "Request attribute {} is not null", key );
        return type.cast( obj );

    }

}
