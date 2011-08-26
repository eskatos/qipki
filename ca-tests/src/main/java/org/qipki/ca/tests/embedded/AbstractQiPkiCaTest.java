/*
 * Copyright (c) 2011, Paul Merlin. All Rights Reserved.
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
package org.qipki.ca.tests.embedded;

import org.junit.AfterClass;
import org.junit.Before;

import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;

import org.qipki.ca.application.contexts.RootContext;
import org.qipki.commons.bootstrap.CryptoValuesModuleAssembler;
import org.qipki.core.QiPkiApplication;
import org.qipki.crypto.asymetric.AsymetricGenerator;
import org.qipki.crypto.bootstrap.CryptoEngineModuleAssembler;
import org.qipki.crypto.io.CryptIO;
import org.qipki.crypto.x509.X509Generator;
import org.qipki.testsupport.AbstractQiPkiTest;

public abstract class AbstractQiPkiCaTest
        extends AbstractQiPkiTest
{

    protected static QiPkiApplication<RootContext> qipkiApplication;

    @AfterClass
    public static void stopQiPkiApplication()
    {
        if ( qipkiApplication != null ) {
            qipkiApplication.stop();
        }
    }

    protected CryptIO cryptio;
    protected X509Generator x509Generator;
    protected AsymetricGenerator asymGenerator;

    @Override
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        new CryptoEngineModuleAssembler().assemble( module );
        new CryptoValuesModuleAssembler().assemble( module );
    }

    @Before
    public void beforeEachAbstractQiPkiCaTest()
    {
        cryptio = serviceLocator.<CryptIO>findService( CryptIO.class ).get();
        x509Generator = serviceLocator.<X509Generator>findService( X509Generator.class ).get();
        asymGenerator = serviceLocator.<AsymetricGenerator>findService( AsymetricGenerator.class ).get();

    }

}
