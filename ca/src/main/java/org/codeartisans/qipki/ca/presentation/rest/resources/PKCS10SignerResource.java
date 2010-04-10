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

import java.io.IOException;
import java.security.cert.X509Certificate;
import org.codeartisans.qipki.core.crypto.CryptIO;
import org.codeartisans.qipki.core.dci.InteractionContext;
import org.codeartisans.qipki.ca.application.contexts.CAContext;
import org.codeartisans.qipki.ca.application.contexts.RootContext;
import org.qi4j.api.composite.TransientBuilderFactory;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.qi4j.api.unitofwork.NoSuchEntityException;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PKCS10SignerResource
        extends AbstractResource
{

    private static final Logger LOGGER = LoggerFactory.getLogger( PKCS10SignerResource.class );
    private final CryptIO cryptIO;
    private final RootContext rootCtx;

    public PKCS10SignerResource( @Structure TransientBuilderFactory tbf,
                                 @Structure ObjectBuilderFactory obf )
    {
        super();
        getVariants().add( new Variant( MediaType.TEXT_PLAIN ) );
        this.cryptIO = tbf.newTransient( CryptIO.class );
        this.rootCtx = obf.newObjectBuilder( RootContext.class ).use( new InteractionContext() ).newInstance();
    }

    @Override
    protected Representation post( Representation entity, Variant variant )
            throws ResourceException
    {
        try {

            String identity = ensureRequestAttribute( CAResource.PARAM_IDENTITY, String.class, Status.CLIENT_ERROR_BAD_REQUEST );
            CAContext caCtx = rootCtx.caContext( identity );
            X509Certificate cert = caCtx.sign( cryptIO.readPKCS10PEM( entity.getReader() ) );
            return new StringRepresentation( cryptIO.asPEM( cert ), MediaType.TEXT_PLAIN );

        } catch ( NoSuchEntityException ex ) {
            LOGGER.trace( "404: {}", ex.getMessage() );
            throw new ResourceException( Status.CLIENT_ERROR_NOT_FOUND );
        } catch ( IOException ex ) {
            LOGGER.trace( "500: {}", ex.getMessage(), ex );
            throw new ResourceException( Status.SERVER_ERROR_INTERNAL, "Unable to read posted entity", ex );
        }
    }

    @Override
    protected Representation representJson()
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

}
