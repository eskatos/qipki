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
package org.codeartisans.qipki.ca.http.presentation.rest.resources.ca;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import org.codeartisans.qipki.ca.application.contexts.ca.CAContext;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.AbstractResource;
import org.codeartisans.qipki.crypto.storage.KeyStoreType;

import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.object.ObjectBuilderFactory;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.OutputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

/**
 * This ressource allow to export a CA KeyPair in a PKCS#12 container protected using a provided password.
 */
public class CAExportResource
        extends AbstractResource
{

    private static final String PARAM_PASSWORD = "password";

    private static final String PARAM_KEYSTORE_TYPE = "kstype";

    public CAExportResource( @Structure ObjectBuilderFactory obf )
    {
        super( obf );
    }

    @Override
    protected Representation get()
            throws ResourceException
    {

        // Data
        final String identity = ensureRequestAttribute( PARAM_IDENTITY, String.class, Status.CLIENT_ERROR_BAD_REQUEST );
        final char[] password = ensureQueryParamValue( PARAM_PASSWORD, Status.CLIENT_ERROR_BAD_REQUEST ).toCharArray();
        final KeyStoreType ksType = KeyStoreType.valueOfTypeString( getQueryParamValue( PARAM_KEYSTORE_TYPE, KeyStoreType.PKCS12.typeString() ) );

        // Context
        CAContext caCtx = newRootContext().caContext( identity );

        // Interaction
        final KeyStore keyStore = caCtx.exportCaKeyPair( password, ksType );

        // Representation
        return new OutputRepresentation( MediaType.APPLICATION_OCTET_STREAM )
        {

            @Override
            public void write( OutputStream outputStream )
                    throws IOException
            {
                try {
                    keyStore.store( outputStream, password );
                } catch ( GeneralSecurityException ex ) {
                    throw new IOException( "Unable to store exported CA KeyStore", ex );
                }
            }

        };
    }

}
