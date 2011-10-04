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
package org.qipki.client.ca.spi;

import java.io.IOException;
import org.apache.http.HttpMessage;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codeartisans.java.toolbox.Strings;
import org.qi4j.api.common.Optional;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.injection.scope.This;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.value.ValueBuilderFactory;
import org.qipki.client.ca.QiPkiCaHttpClientConfiguration;
import org.qipki.client.ca.QiPkiClientFailure;
import org.qipki.commons.rest.values.CaApiURIsValue;

// TODO Cache costly objects and use ThreadLocal for all instances shared between methods for thread safety
@Mixins( RestClientService.Mixin.class )
public interface RestClientService
        extends ServiceComposite
{

    // TODO Move this in CA client specific code
    CaApiURIsValue fetchApiURIs();

    String getJSON( String uri );

    String postJSON( String uri, String json );

    @SuppressWarnings( "PublicInnerClass" )
    abstract class Mixin
            implements RestClientService
    {

        @This
        @Optional
        private QiPkiCaHttpClientConfiguration configuration;
        @Structure
        private ValueBuilderFactory valueBuilderFactory;
        // This one is thread safe
        private ResponseHandler<String> responseHandler = new BasicResponseHandler();

        @Override
        public CaApiURIsValue fetchApiURIs()
        {
            String apiUri = "http://localhost:8443/api";
            if ( configuration != null && !Strings.isEmpty( configuration.apiUri().get() ) ) {
                apiUri = configuration.apiUri().get();
            }
            String jsonApi = getJSON( apiUri );
            return valueBuilderFactory.newValueFromJSON( CaApiURIsValue.class, jsonApi );
        }

        @Override
        public String getJSON( String uri )
        {
            try {
                HttpGet get = new HttpGet( uri );
                addAcceptJsonHeader( get );
                return httpClient().execute( get, responseHandler );
            } catch ( IOException ex ) {
                throw new QiPkiClientFailure( "Unable to execute HTTP GET " + uri, ex );
            }
        }

        @Override
        public String postJSON( String uri, String json )
        {
            try {
                HttpPost post = new HttpPost( uri );
                addAcceptJsonHeader( post );
                post.setEntity( new StringEntity( json ) );
                return httpClient().execute( post, responseHandler );
            } catch ( IOException ex ) {
                throw new QiPkiClientFailure( "Unable to execute HTTP POST " + uri, ex );
            }
        }

        private HttpClient httpClient()
        {
            return new DefaultHttpClient();
        }

        private void addAcceptJsonHeader( HttpMessage httpMessage )
        {
            httpMessage.addHeader( "Accept", "application/json" );
        }

    }

}
