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
package org.codeartisans.qipki.ca.presentation.rest.resources.ca;

import java.io.IOException;
import java.util.Collections;
import org.codeartisans.java.toolbox.io.ReaderUtil;
import org.codeartisans.qipki.ca.application.contexts.RootContext;
import org.codeartisans.qipki.ca.application.contexts.ca.CAContext;
import org.codeartisans.qipki.ca.presentation.rest.resources.AbstractResource;
import org.codeartisans.qipki.commons.values.params.RevocationParamsValue;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.qi4j.api.unitofwork.NoSuchEntityException;
import org.qi4j.api.value.ValueBuilderFactory;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class X509RevokerResource
        extends AbstractResource
{

    private static final Logger LOGGER = LoggerFactory.getLogger( X509RevokerResource.class );
    @Structure
    private ValueBuilderFactory vbf;

    public X509RevokerResource( @Structure ObjectBuilderFactory obf )
    {
        super( obf );
        setAllowedMethods( Collections.singleton( Method.POST ) );
        setNegotiated( false );
    }

    @Override
    protected Representation post( Representation entity )
            throws ResourceException
    {
        try {

            // Data
            String caIdentity = ensureRequestAttribute( PARAM_IDENTITY, String.class, Status.CLIENT_ERROR_BAD_REQUEST );
            RevocationParamsValue revocationParams = vbf.newValueFromJSON( RevocationParamsValue.class,
                                                                           ReaderUtil.readStringFully( entity.getReader() ) );

            // Context
            RootContext rootCtx = newRootContext();
            CAContext caCtx = rootCtx.caContext( caIdentity );

            // Interaction
            caCtx.revoke( revocationParams );

            // Representation
            return new EmptyRepresentation(); // QUID ???

        } catch ( NoSuchEntityException ex ) {
            LOGGER.trace( "404: {}", ex.getMessage(), ex );
            throw new ResourceException( Status.CLIENT_ERROR_NOT_FOUND );
        } catch ( IOException ex ) {
            LOGGER.warn( "500: {}", ex.getMessage(), ex );
            throw new ResourceException( Status.SERVER_ERROR_INTERNAL, "Unable to read posted entity", ex );
        }
    }

}
