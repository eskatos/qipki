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
package org.codeartisans.qipki.ca;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.apache.http.HttpMessage;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codeartisans.qipki.ca.utils.QiPkiTestApplicationCa;
import org.codeartisans.qipki.commons.assembly.CryptoValuesModuleAssembler;
import org.codeartisans.qipki.commons.assembly.RestValuesModuleAssembler;
import org.codeartisans.qipki.commons.values.crypto.CryptoValuesFactory;
import org.codeartisans.qipki.commons.values.crypto.X509ExtensionsValueFactory;
import org.codeartisans.qipki.commons.values.params.ParamsFactory;
import org.codeartisans.qipki.commons.values.rest.ApiURIsValue;
import org.codeartisans.qipki.core.QiPkiApplication;
import org.codeartisans.qipki.crypto.assembly.CryptoEngineModuleAssembler;
import org.codeartisans.qipki.crypto.asymetric.AsymetricGenerator;
import org.codeartisans.qipki.crypto.x509.X509Generator;
import org.codeartisans.qipki.crypto.io.CryptIO;
import org.junit.After;
import org.junit.Before;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.test.AbstractQi4jTest;
import org.restlet.data.MediaType;

public abstract class AbstractQiPkiTest
        extends AbstractQi4jTest
{

    private static final int DEFAULT_PORT = 8443;
    protected QiPkiApplication qipkiServer;
    protected ResponseHandler<String> strResponseHandler;
    protected DefaultHttpClient httpClient;
    protected CryptIO cryptio;
    protected X509Generator x509Generator;
    protected AsymetricGenerator asymGenerator;
    protected CryptoValuesFactory cryptoValuesFactory;
    protected ParamsFactory paramsFactory;
    protected ApiURIsValue qiPkiApi;
    protected X509ExtensionsValueFactory x509ExtValuesFactory;

    @Override
    @SuppressWarnings( "unchecked" )
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        new CryptoEngineModuleAssembler().assemble( module );
        new RestValuesModuleAssembler().assemble( module );
        new CryptoValuesModuleAssembler().assemble( module );
    }

    @Before
    public void before()
            throws IOException
    {
        qipkiServer = new QiPkiTestApplicationCa();
        qipkiServer.run();
        strResponseHandler = new BasicResponseHandler();
        httpClient = new DefaultHttpClient();
        cryptio = serviceLocator.<CryptIO>findService( CryptIO.class ).get();
        x509Generator = serviceLocator.<X509Generator>findService( X509Generator.class ).get();
        asymGenerator = serviceLocator.<AsymetricGenerator>findService( AsymetricGenerator.class ).get();
        paramsFactory = serviceLocator.<ParamsFactory>findService( ParamsFactory.class ).get();
        cryptoValuesFactory = serviceLocator.<CryptoValuesFactory>findService( CryptoValuesFactory.class ).get();
        x509ExtValuesFactory = serviceLocator.<X509ExtensionsValueFactory>findService( X509ExtensionsValueFactory.class ).get();
        HttpGet get = new HttpGet( "/api" );
        addAcceptJsonHeader( get );
        String jsonApi = httpClient.execute( new HttpHost( "localhost", DEFAULT_PORT ), get, strResponseHandler );
        qiPkiApi = valueBuilderFactory.newValueFromJSON( ApiURIsValue.class, jsonApi );
    }

    @After
    public void after()
    {
        qipkiServer.stop();
    }

    protected final void addAcceptJsonHeader( HttpMessage httpMessage )
    {
        httpMessage.addHeader( "Accept", MediaType.APPLICATION_JSON.toString() );
    }

}
