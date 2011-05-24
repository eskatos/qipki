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
package org.qipki.testsupport;

import java.io.IOException;

import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import org.junit.Before;

import org.restlet.data.MediaType;

/**
 * Base class for QiPKI HTTP based unit tests.
 *
 * Provide a preconfigured, thread safe, HttpClient.
 */
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
        HttpParams params = new BasicHttpParams();
        SchemeRegistry registry = new SchemeRegistry();
        registry.register( new Scheme( "http", PlainSocketFactory.getSocketFactory(), DEFAULT_PORT ) );
        ClientConnectionManager cm = new ThreadSafeClientConnManager( params, registry );
        httpClient = new DefaultHttpClient( cm, params );
    }

    protected final void addAcceptJsonHeader( HttpMessage httpMessage )
    {
        httpMessage.addHeader( "Accept", MediaType.APPLICATION_JSON.toString() );
    }

}