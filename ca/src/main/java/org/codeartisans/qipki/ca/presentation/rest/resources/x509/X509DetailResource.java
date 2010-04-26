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
package org.codeartisans.qipki.ca.presentation.rest.resources.x509;

import org.codeartisans.qipki.ca.application.contexts.x509.X509Context;
import org.codeartisans.qipki.ca.domain.x509.X509;
import org.codeartisans.qipki.ca.presentation.rest.RestletValuesFactory;
import org.codeartisans.qipki.ca.presentation.rest.resources.AbstractEntityResource;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

public class X509DetailResource
        extends AbstractEntityResource
{

    @Service
    private RestletValuesFactory valuesFactory;

    public X509DetailResource( @Structure ObjectBuilderFactory obf )
    {
        super( obf );
    }

    @Override
    protected Representation representJson()
    {
        // Data
        String identity = ensureRequestAttribute( PARAM_IDENTITY, String.class, Status.CLIENT_ERROR_BAD_REQUEST );

        // Context
        X509Context x509Ctx = newRootContext().x509Context( identity );

        // Interaction
        X509 x509 = x509Ctx.x509();

        // Representation
        return new StringRepresentation( valuesFactory.x509Detail( getRootRef(), x509 ).toJSON(),
                                         MediaType.APPLICATION_JSON );

    }

}
