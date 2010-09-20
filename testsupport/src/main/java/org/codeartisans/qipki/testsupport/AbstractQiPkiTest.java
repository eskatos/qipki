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

import org.codeartisans.qipki.commons.assembly.CryptoValuesModuleAssembler;
import org.codeartisans.qipki.commons.assembly.RestValuesModuleAssembler;
import org.codeartisans.qipki.commons.crypto.services.CryptoValuesFactory;
import org.codeartisans.qipki.commons.crypto.services.X509ExtensionsValueFactory;
import org.codeartisans.qipki.commons.rest.values.params.ParamsFactory;
import org.codeartisans.qipki.core.QiPkiApplication;
import org.codeartisans.qipki.crypto.assembly.CryptoEngineModuleAssembler;
import org.codeartisans.qipki.crypto.asymetric.AsymetricGenerator;
import org.codeartisans.qipki.crypto.io.CryptIO;
import org.codeartisans.qipki.crypto.x509.X509Generator;

import org.junit.After;
import org.junit.Before;

import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.test.AbstractQi4jTest;

@SuppressWarnings( "ProtectedField" )
public abstract class AbstractQiPkiTest
        extends AbstractQi4jTest
{

    protected QiPkiApplication qipkiServer;
    protected CryptIO cryptio;
    protected X509Generator x509Generator;
    protected AsymetricGenerator asymGenerator;
    protected CryptoValuesFactory cryptoValuesFactory;
    protected ParamsFactory paramsFactory;
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
    public void qiPkiBefore()
    {
        qipkiServer = createQiPkiApplication();
        qipkiServer.run();
        x509Generator = serviceLocator.<X509Generator>findService( X509Generator.class ).get();
        asymGenerator = serviceLocator.<AsymetricGenerator>findService( AsymetricGenerator.class ).get();
        paramsFactory = serviceLocator.<ParamsFactory>findService( ParamsFactory.class ).get();
        cryptoValuesFactory = serviceLocator.<CryptoValuesFactory>findService( CryptoValuesFactory.class ).get();
        x509ExtValuesFactory = serviceLocator.<X509ExtensionsValueFactory>findService( X509ExtensionsValueFactory.class ).get();
    }

    protected abstract QiPkiApplication createQiPkiApplication();

    @After
    public void qiPkiAfter()
    {
        qipkiServer.stop();
    }

}
