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

import info.aduna.io.FileUtil;

import java.io.File;

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
import org.junit.BeforeClass;

import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.test.AbstractQi4jTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings( "ProtectedField" )
public abstract class AbstractQiPkiTest
        extends AbstractQi4jTest
{

    private static final Logger LOGGER = LoggerFactory.getLogger( AbstractQiPkiTest.class );
    protected static final File ENTITIES_DIRECTORY = new File( "target/qi4j-entities" );
    protected static final File INDEX_DIRECTORY = new File( "target/qi4j-index" );
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

    @BeforeClass
    public static void qipPkiTestBeforeClass()
    {
        LOGGER.info( "BEFORE_CLASS WILL DELETE INDEX REPOSITORY AND ENTITIES STORE DATA" );
        FileUtil.deltree( ENTITIES_DIRECTORY );
        FileUtil.deltree( INDEX_DIRECTORY );
        if ( ENTITIES_DIRECTORY.exists() ) {
            throw new IllegalStateException( ENTITIES_DIRECTORY + " still exists cannot continue" );
        }
        if ( INDEX_DIRECTORY.exists() ) {
            throw new IllegalStateException( INDEX_DIRECTORY + " still exists cannot continue" );
        }
    }

    @Before
    public void qiPkiTestBefore()
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
    public void qiPkiTestAfter()
    {
        qipkiServer.stop();
    }

}
