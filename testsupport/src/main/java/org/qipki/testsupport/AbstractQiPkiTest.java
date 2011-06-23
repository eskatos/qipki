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

import org.junit.Before;

import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.test.AbstractQi4jTest;

import org.qipki.commons.bootstrap.CryptoValuesModuleAssembler;
import org.qipki.commons.bootstrap.RestValuesModuleAssembler;
import org.qipki.commons.crypto.services.CryptoValuesFactory;
import org.qipki.commons.crypto.services.X509ExtensionsValueFactory;
import org.qipki.commons.rest.values.params.ParamsFactory;
import org.qipki.crypto.bootstrap.CryptoEngineModuleAssembler;
import org.qipki.crypto.asymetric.AsymetricGenerator;
import org.qipki.crypto.io.CryptIO;
import org.qipki.crypto.x509.X509Generator;

@SuppressWarnings( "ProtectedField" )
public abstract class AbstractQiPkiTest
        extends AbstractQi4jTest
{

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
        new CryptoValuesModuleAssembler().assemble( module );
        new RestValuesModuleAssembler().assemble( module );
    }

    @Before
    public void qiPkiTestBefore()
    {
        x509Generator = serviceLocator.<X509Generator>findService( X509Generator.class ).get();
        asymGenerator = serviceLocator.<AsymetricGenerator>findService( AsymetricGenerator.class ).get();
        paramsFactory = serviceLocator.<ParamsFactory>findService( ParamsFactory.class ).get();
        cryptoValuesFactory = serviceLocator.<CryptoValuesFactory>findService( CryptoValuesFactory.class ).get();
        x509ExtValuesFactory = serviceLocator.<X509ExtensionsValueFactory>findService( X509ExtensionsValueFactory.class ).get();
    }

}
