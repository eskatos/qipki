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
package org.qipki.crypto.jce;

import static org.junit.Assert.*;
import org.junit.Test;

import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.entitystore.memory.MemoryEntityStoreService;
import org.qi4j.core.testsupport.AbstractQi4jTest;

import org.qipki.crypto.bootstrap.CryptoEngineModuleAssembler;

public class JceDetectorTest
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

    @Test
    public void testPresent()
    {
        JceDetector jceDetector = serviceLocator.<JceDetector>findService( JceDetector.class ).get();
        assertTrue( "JCE must be installed to run QiPki crypto unit tests", jceDetector.areJceInstalled() );
    }

    @Test
    public void testEnsure()
    {
        JceDetector jceDetector = serviceLocator.<JceDetector>findService( JceDetector.class ).get();
        jceDetector.ensureJceAreInstalled();
    }

}
