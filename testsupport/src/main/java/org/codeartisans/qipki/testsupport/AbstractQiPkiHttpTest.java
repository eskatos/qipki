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
package org.codeartisans.qipki.testsupport;

import java.io.IOException;

import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import org.junit.Before;

import org.restlet.data.MediaType;

@SuppressWarnings( "ProtectedField" )
public abstract class AbstractQiPkiHttpTest
        extends AbstractQiPkiTest
{

    protected static final String LOCALHOST = "localhost";
    protected static final int DEFAULT_PORT = 8443;
    protected ResponseHandler<String> strResponseHandler;
    protected ResponseHandler<byte[]> bytesResponseHandler;
    protected DefaultHttpClient httpClient;

    @Before
    public void qiPkiHttpBefore()
            throws Exception
    {
        strResponseHandler = new BasicResponseHandler();
        bytesResponseHandler = new ResponseHandler<byte[]>()
        {

            @Override
            public byte[] handleResponse( HttpResponse response )
                    throws ClientProtocolException, IOException
            {
                return EntityUtils.toByteArray( response.getEntity() );
            }

        };
        httpClient = new DefaultHttpClient();
    }

    protected final void addAcceptJsonHeader( HttpMessage httpMessage )
    {
        httpMessage.addHeader( "Accept", MediaType.APPLICATION_JSON.toString() );
    }

}
