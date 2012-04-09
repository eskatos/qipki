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
package org.qipki.ca.tests.http;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;

import org.junit.AfterClass;
import org.junit.Before;

import org.qipki.ca.application.contexts.RootContext;
import org.qipki.commons.rest.values.CaApiURIsValue;
import org.qipki.core.QiPkiApplication;
import org.qipki.testsupport.AbstractQiPkiHttpTest;

@SuppressWarnings( "ProtectedField" )
public abstract class AbstractQiPkiHttpCaTest
        extends AbstractQiPkiHttpTest
{

    protected static QiPkiApplication<RootContext> qipkiApplication;

    @AfterClass
    public static void stopQiPkiApplication()
    {
        if ( qipkiApplication != null ) {
            qipkiApplication.stop();
        }
    }

    protected CaApiURIsValue caApi;

    @Before
    public void before()
            throws IOException
    {
        HttpGet get = new HttpGet( "/api" );
        addAcceptJsonHeader( get );
        String jsonApi = httpClient.execute( new HttpHost( LOCALHOST, DEFAULT_PORT ), get, strResponseHandler );
        caApi = module.newValueFromJSON( CaApiURIsValue.class, jsonApi );
    }

}
