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
import java.security.cert.X509Certificate;
import java.util.Collections;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.codeartisans.qipki.ca.application.contexts.CAContext;
import org.codeartisans.qipki.ca.application.contexts.RootContext;
import org.codeartisans.qipki.ca.presentation.rest.resources.AbstractEntityResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.AbstractResource;
import org.codeartisans.qipki.core.crypto.CryptIO;
import org.codeartisans.qipki.core.crypto.CryptoToolFactory;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.qi4j.api.unitofwork.NoSuchEntityException;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
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
    @Service
    private CryptoToolFactory cryptoToolFactory;

    public PKCS10SignerResource( @Structure ObjectBuilderFactory obf )
    {
        super( obf );
        setAllowedMethods( Collections.singleton( Method.POST ) );
        setNegotiated( false );
        getVariants().add( new Variant( MediaType.TEXT_PLAIN ) );
    }

    @Override
    protected Representation post( Representation entity )
            throws ResourceException
    {
        try {

            // Data
            String caIdentity = ensureRequestAttribute( AbstractEntityResource.PARAM_IDENTITY, String.class, Status.CLIENT_ERROR_BAD_REQUEST );
            CryptIO cryptIO = cryptoToolFactory.newCryptIOInstance();
            PKCS10CertificationRequest pkcs10 = cryptIO.readPKCS10PEM( entity.getReader() );

            // Context
            RootContext rootCtx = newRootContext();
            CAContext caCtx = rootCtx.caContext( caIdentity );

            // Interaction
            X509Certificate cert = caCtx.sign( pkcs10 );

            // Representation
            return new StringRepresentation( cryptIO.asPEM( cert ), MediaType.TEXT_PLAIN );

        } catch ( NoSuchEntityException ex ) {
            LOGGER.trace( "404: {}", ex.getMessage(), ex );
            throw new ResourceException( Status.CLIENT_ERROR_NOT_FOUND );
        } catch ( IOException ex ) {
            LOGGER.warn( "500: {}", ex.getMessage(), ex );
            throw new ResourceException( Status.SERVER_ERROR_INTERNAL, "Unable to read posted entity", ex );
        }
    }

}
