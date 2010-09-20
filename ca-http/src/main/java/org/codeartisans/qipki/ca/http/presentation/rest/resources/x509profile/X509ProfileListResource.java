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
package org.codeartisans.qipki.ca.http.presentation.rest.resources.x509profile;

import java.io.IOException;

import org.codeartisans.qipki.ca.application.contexts.x509profile.X509ProfileListContext;
import org.codeartisans.qipki.ca.domain.x509profile.X509Profile;
import org.codeartisans.qipki.ca.http.presentation.rest.RestletValuesFactory;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.AbstractListResource;
import org.codeartisans.qipki.commons.rest.values.params.X509ProfileFactoryParamsValue;
import org.codeartisans.qipki.commons.rest.values.representations.RestListValue;
import org.codeartisans.qipki.commons.rest.values.representations.RestValue;
import org.codeartisans.qipki.commons.rest.values.representations.X509ProfileValue;

import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.qi4j.api.query.Query;
import org.qi4j.api.value.ValueBuilderFactory;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class X509ProfileListResource
        extends AbstractListResource
{

    private static final Logger LOGGER = LoggerFactory.getLogger( X509ProfileListResource.class );
    @Structure
    private ValueBuilderFactory vbf;
    @Service
    private RestletValuesFactory restValuesFactory;

    public X509ProfileListResource( @Structure ObjectBuilderFactory obf )
    {
        super( obf );
    }

    @Override
    protected RestListValue list( int start )
    {
        // Context
        X509ProfileListContext x509ProfileListCtx = newRootContext().x509ProfileListContext();

        // Interaction
        Query<X509Profile> x509ProfileList = x509ProfileListCtx.list( start );

        // Representation
        Iterable<RestValue> values = restValuesFactory.asValues( getRootRef(), x509ProfileList );
        return restValuesFactory.newListRepresentationValue( getReference(), start, values );
    }

    @Override
    protected Representation post( Representation entity )
            throws ResourceException
    {
        try {

            // Data
            X509ProfileFactoryParamsValue params = vbf.newValueFromJSON( X509ProfileFactoryParamsValue.class, entity.getText() );

            // Context
            X509ProfileListContext x509ProfileListCtx = newRootContext().x509ProfileListContext();

            // Interaction
            X509Profile x509Profile = x509ProfileListCtx.createX509Profile( params.name().get(),
                                                                            params.netscapeCertComment().get(),
                                                                            params.keyUsages().get(),
                                                                            params.extendedKeyUsages().get(),
                                                                            params.netscapeCertTypes().get(),
                                                                            params.basicConstraints().get(),
                                                                            params.nameConstraints().get() );

            // Redirect to created resource
            X509ProfileValue x509ProfileValue = restValuesFactory.x509Profile( getRootRef(), x509Profile );
            return redirectToCreatedResource( x509ProfileValue.uri().get() );

        } catch ( IOException ex ) {
            LOGGER.warn( "500: {}", ex.getMessage(), ex );
            throw new ResourceException( Status.SERVER_ERROR_INTERNAL, "Unable to read posted value", ex );
        }
    }

}
