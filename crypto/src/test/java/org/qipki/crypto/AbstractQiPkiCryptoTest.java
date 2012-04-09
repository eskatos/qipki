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
package org.qipki.crypto;

import org.junit.Before;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.entitystore.memory.MemoryEntityStoreService;
import org.qi4j.test.AbstractQi4jTest;
import org.qipki.crypto.bootstrap.CryptoEngineModuleAssembler;
import org.qipki.crypto.cipher.CipherFactory;
import org.qipki.crypto.digest.Digester;
import org.qipki.crypto.jce.JceDetector;
import org.qipki.crypto.mac.MAC;
import org.qipki.crypto.symetric.SymetricGenerator;

public abstract class AbstractQiPkiCryptoTest
        extends AbstractQi4jTest
{

    @Override
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        ModuleAssembly config = module.layer().module( "config" );
        config.services( MemoryEntityStoreService.class );
        new CryptoEngineModuleAssembler().withConfigModule( config ).assemble( module );
    }

    protected CryptoContext cryptoContext;
    protected JceDetector jceDetector;
    protected Digester digester;
    protected SymetricGenerator symGenerator;
    protected CipherFactory cipherFactory;
    protected MAC mac;

    @Before
    public void beforeAbstractQiPkiCryptoTest()
    {
        cryptoContext = module.<CryptoContext>findService( CryptoContext.class ).get();
        jceDetector = module.<JceDetector>findService( JceDetector.class ).get();
        digester = module.<Digester>findService( Digester.class ).get();
        symGenerator = module.<SymetricGenerator>findService( SymetricGenerator.class ).get();
        cipherFactory = module.<CipherFactory>findService( CipherFactory.class ).get();
        mac = module.<MAC>findService( MAC.class ).get();
    }

}
