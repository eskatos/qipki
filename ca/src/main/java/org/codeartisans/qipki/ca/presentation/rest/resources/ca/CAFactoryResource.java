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
import org.codeartisans.qipki.ca.application.contexts.CAListContext;
import org.codeartisans.qipki.ca.domain.ca.root.RootCAEntity;
import org.codeartisans.qipki.ca.presentation.rest.RestValuesFactory;
import org.codeartisans.qipki.ca.presentation.rest.resources.AbstractFactoryResource;
import org.codeartisans.qipki.commons.values.params.CAFactoryParamsValue;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.qi4j.api.value.ValueBuilderFactory;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CAFactoryResource
        extends AbstractFactoryResource
{

    private static final Logger LOGGER = LoggerFactory.getLogger( CAFactoryResource.class );
    @Service
    private RestValuesFactory restValuesFactory;
    @Structure
    private ValueBuilderFactory vbf;

    public CAFactoryResource( @Structure ObjectBuilderFactory obf )
    {
        super( obf );
    }

    @Override
    protected Representation post( Representation entity )
            throws ResourceException
    {
        try {

            // Data
            CAFactoryParamsValue data = vbf.newValueFromJSON( CAFactoryParamsValue.class, entity.getText() );

            // Context
            CAListContext caListCtx = newRootContext().caListContext();

            // Interaction
            RootCAEntity ca = caListCtx.createCA( data );

            // Representation
            getResponse().setStatus( Status.SUCCESS_CREATED );
            return new StringRepresentation( restValuesFactory.ca( getReference(), ca ).uri().get(),
                                             MediaType.TEXT_URI_LIST );

        } catch ( IOException ex ) {
            LOGGER.trace( "500: {}", ex.getMessage(), ex );
            throw new ResourceException( Status.SERVER_ERROR_INTERNAL, "Unable to read posted entity", ex );
        }
    }

}
