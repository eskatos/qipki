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
