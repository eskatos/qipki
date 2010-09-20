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
package org.codeartisans.qipki.ca.http;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;

import org.codeartisans.qipki.ca.http.utils.QiPkiTestApplicationHttpCa;
import org.codeartisans.qipki.commons.rest.values.CaApiURIsValue;
import org.codeartisans.qipki.core.QiPkiApplication;
import org.codeartisans.qipki.crypto.io.CryptIO;
import org.codeartisans.qipki.testsupport.AbstractQiPkiHttpTest;

import org.junit.Before;

@SuppressWarnings( "ProtectedField" )
public abstract class AbstractQiPkiHttpCaTest
        extends AbstractQiPkiHttpTest
{

    protected CaApiURIsValue caApi;

    @Override
    protected QiPkiApplication createQiPkiApplication()
    {
        return new QiPkiTestApplicationHttpCa();
    }

    @Before
    public void before()
            throws IOException
    {
        cryptio = serviceLocator.<CryptIO>findService( CryptIO.class ).get();
        HttpGet get = new HttpGet( "/api" );
        addAcceptJsonHeader( get );
        String jsonApi = httpClient.execute( new HttpHost( LOCALHOST, DEFAULT_PORT ), get, strResponseHandler );
        caApi = valueBuilderFactory.newValueFromJSON( CaApiURIsValue.class, jsonApi );
    }

}
