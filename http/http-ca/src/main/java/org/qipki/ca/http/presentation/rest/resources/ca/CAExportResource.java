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
package org.qipki.ca.http.presentation.rest.resources.ca;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import org.qipki.ca.application.contexts.ca.CAContext;
import org.qipki.ca.http.presentation.rest.resources.AbstractResource;
import org.qipki.crypto.storage.KeyStoreType;

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
