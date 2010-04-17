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
package org.codeartisans.qipki.ca.presentation.rest.resources.cryptostore;

import org.codeartisans.qipki.ca.application.contexts.CryptoStoreContext;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStore;
import org.codeartisans.qipki.ca.presentation.rest.RestValuesFactory;
import org.codeartisans.qipki.ca.presentation.rest.resources.AbstractEntityResource;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.qi4j.api.unitofwork.NoSuchEntityException;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CryptoStoreResource
        extends AbstractEntityResource
{

    private static final Logger LOGGER = LoggerFactory.getLogger( CryptoStoreResource.class );
    @Service
    private RestValuesFactory restValuesFactory;

    public CryptoStoreResource( @Structure ObjectBuilderFactory obf )
    {
        super( obf );
    }

    @Override
    protected Representation representJson()
    {
        String identity = null;
        try {

            // Data
            identity = ensureRequestAttribute( PARAM_IDENTITY, String.class, Status.CLIENT_ERROR_BAD_REQUEST );

            // Context
            CryptoStoreContext csCtx = newRootContext().ksContext( identity );

            // Interaction
            CryptoStore cs = csCtx.cryptoStore();

            // Representation
            return new StringRepresentation( restValuesFactory.cryptoStore( getReference(), cs ).toJSON(),
                                             MediaType.APPLICATION_JSON );

        } catch ( NoSuchEntityException ex ) {
            LOGGER.debug( "{}: No CryptoStore found for the requested identity ('{}')", new Object[]{ Status.CLIENT_ERROR_NOT_FOUND, identity }, ex );
            throw new ResourceException( Status.CLIENT_ERROR_NOT_FOUND, ex );
        }
    }

}
