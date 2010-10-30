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
package org.codeartisans.qipki.client.ca.services;

import java.io.IOException;

import org.apache.http.HttpMessage;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import org.codeartisans.qipki.client.ca.QiPkiClientFailure;
import org.codeartisans.qipki.commons.rest.values.CaApiURIsValue;

import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.value.ValueBuilderFactory;

// TODO Use ThreadLocal for all instances shared between methods for thread safety
@Mixins( RestClientService.Mixin.class )
public interface RestClientService
        extends ServiceComposite
{

    CaApiURIsValue fetchApiURIs();

    String getJSON( String uri );

    @SuppressWarnings( "PublicInnerClass" )
    abstract class Mixin
            implements RestClientService
    {
        // TODO Find a way to configure client services, api URI for now, for authentication later

        private static final String API_URI = "http://localhost:8443/api";
        @Structure
        private ValueBuilderFactory valueBuilderFactory;

        @Override
        public CaApiURIsValue fetchApiURIs()
        {
            String jsonApi = getJSON( API_URI );
            return valueBuilderFactory.newValueFromJSON( CaApiURIsValue.class, jsonApi );
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
