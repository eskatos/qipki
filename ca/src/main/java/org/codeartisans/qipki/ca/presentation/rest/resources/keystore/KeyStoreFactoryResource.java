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
package org.codeartisans.qipki.ca.presentation.rest.resources.keystore;

import java.io.IOException;
import org.codeartisans.qipki.ca.application.contexts.KeyStoreListContext;
import org.codeartisans.qipki.ca.application.contexts.RootContext;
import org.codeartisans.qipki.ca.domain.keystore.KeyStoreEntity;
import org.codeartisans.qipki.ca.presentation.rest.RestValuesFactory;
import org.codeartisans.qipki.ca.presentation.rest.resources.AbstractFactoryResource;
import org.codeartisans.qipki.commons.values.params.KeyStoreFactoryParamsValue;
import org.codeartisans.qipki.commons.values.rest.KeyStoreValue;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.qi4j.api.value.ValueBuilderFactory;
import org.restlet.data.Status;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyStoreFactoryResource
        extends AbstractFactoryResource
{

    private static final Logger LOGGER = LoggerFactory.getLogger( KeyStoreFactoryResource.class );
    @Structure
    private ValueBuilderFactory vbf;
    @Service
    private RestValuesFactory restValuesFactory;

    public KeyStoreFactoryResource( @Structure ObjectBuilderFactory obf )
    {
        super( obf );
    }

    @Override
    protected Representation post( Representation entity )
            throws ResourceException
    {
        try {

            // Data
            KeyStoreFactoryParamsValue data = vbf.newValueFromJSON( KeyStoreFactoryParamsValue.class, entity.getText() );

            // Context
            KeyStoreListContext ksListCtx = newRootContext().ksListContext();

            // Interaction
            KeyStoreEntity ks = ksListCtx.createKeyStore( data );

            // Representation
            KeyStoreValue ksValue = restValuesFactory.keyStore( getRootRef().addSegment( "keystore" ), ks );
            getResponse().redirectSeeOther( ksValue.uri().get() );
            return new EmptyRepresentation();

        } catch ( IOException ex ) {
            LOGGER.trace( "500: {}", ex.getMessage(), ex );
            throw new ResourceException( Status.SERVER_ERROR_INTERNAL, "Unable to read posted entity", ex );
        }
    }

}
