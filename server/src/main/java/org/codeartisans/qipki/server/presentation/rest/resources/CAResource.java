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

import org.codeartisans.qipki.server.domain.ca.CA;
import org.codeartisans.qipki.server.domain.ca.CARepository;
import org.codeartisans.qipki.server.presentation.rest.values.RestValuesFactory;
import org.qi4j.api.injection.scope.Service;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CAResource
        extends AbstractResource
{

    private static final Logger LOGGER = LoggerFactory.getLogger( CAResource.class );
    public static final String PARAM_IDENTITY = "identity";
    private final RestValuesFactory valuesFactory;
    private final CARepository caRepos;

    public CAResource( @Service RestValuesFactory valuesFactory,
                       @Service CARepository caRepos )
    {
        this.valuesFactory = valuesFactory;
        this.caRepos = caRepos;
    }

    @Override
    protected Representation representJson()
    {
        String requestedIdentity = ensureRequestedIdentity();
        CA ca = ensureCA( requestedIdentity );
        return new StringRepresentation( valuesFactory.ca( getReference(), ca ).toJSON(), MediaType.APPLICATION_JSON );
    }

    private String ensureRequestedIdentity()
    {
        String identity = ( String ) getRequest().getAttributes().get( PARAM_IDENTITY );
        if ( identity == null ) {
            LOGGER.debug( "No requested identity, sending {}", Status.CLIENT_ERROR_BAD_REQUEST.toString() );
            throw new ResourceException( Status.CLIENT_ERROR_BAD_REQUEST );
        }
        return identity;
    }

    private CA ensureCA( String requestedIdentity )
    {
        CA ca = caRepos.findByIdentity( requestedIdentity );
        if ( ca == null ) {
            LOGGER.debug( "No CA found for the requested identity ('{}'), sending {}", requestedIdentity, Status.CLIENT_ERROR_NOT_FOUND );
            throw new ResourceException( Status.CLIENT_ERROR_NOT_FOUND );
        }
        return ca;
    }

}
