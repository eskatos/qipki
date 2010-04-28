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
package org.codeartisans.qipki.client.ca.services;

import java.io.IOException;
import org.apache.http.HttpMessage;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codeartisans.qipki.client.ca.QiPkiClientFailure;
import org.codeartisans.qipki.commons.values.rest.ApiURIsValue;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.value.ValueBuilderFactory;

// TODO Use ThreadLocal for all instances shared between methods for thread safety
@Mixins( RestClientService.Mixin.class )
public interface RestClientService
        extends ServiceComposite
{

    ApiURIsValue fetchApiURIs();

    String getJSON( String uri );

    abstract class Mixin
            implements RestClientService
    {
        // TODO Find a way to configure client services, api URI for now, for authentication later

        private static final String API_URI = "http://localhost:8443/api";
        @Structure
        private ValueBuilderFactory valueBuilderFactory;

        @Override
        public ApiURIsValue fetchApiURIs()
        {
            String jsonApi = getJSON( API_URI );
            return valueBuilderFactory.newValueFromJSON( ApiURIsValue.class, jsonApi );
        }

        @Override
        public String getJSON( String uri )
        {
            try {
                HttpGet get = new HttpGet( uri );
                addAcceptJsonHeader( get );
                return httpClient().execute( get, responseHandler() );
            } catch ( IOException ex ) {
                throw new QiPkiClientFailure( "Unable to execute HTTP GET " + uri, ex );
            }
        }

        private HttpClient httpClient()
        {
            return new DefaultHttpClient();
        }

        private ResponseHandler<String> responseHandler()
        {
            return new BasicResponseHandler();
        }

        private void addAcceptJsonHeader( HttpMessage httpMessage )
        {
            httpMessage.addHeader( "Accept", "application/json" );
        }

    }

}
