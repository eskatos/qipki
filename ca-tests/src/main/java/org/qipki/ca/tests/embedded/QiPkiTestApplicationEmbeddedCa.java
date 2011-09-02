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

import org.qi4j.api.structure.Application.Mode;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;

import org.qipki.ca.AbstractQiPkiCa;
import org.qipki.ca.bootstrap.QiPkiEmbeddedCaAssembler;
import org.qipki.ca.tests.QiPkiCaFixtures;
import org.qipki.core.bootstrap.persistence.DerbySesamePersistenceAssembler;
import org.qipki.testsupport.QiPkiTestSupport;

public class QiPkiTestApplicationEmbeddedCa
        extends AbstractQiPkiCa
{

    public QiPkiTestApplicationEmbeddedCa( String testCodeName )
    {
        super( new QiPkiEmbeddedCaAssembler( testCodeName, null, Mode.staging ).withPresentationTestsAssembler( new Assembler()
        {

            @Override
            public void assemble( ModuleAssembly module )
                    throws AssemblyException
            {
                module.services( QiPkiCaFixtures.class ).instantiateOnStartup();
            }

        } ).withFileConfigurationOverride( QiPkiTestSupport.fileConfigTestOverride( testCodeName ) ).
                withPersistenceAssembler( new DerbySesamePersistenceAssembler( "jdbc:derby:target/" + testCodeName + "-qi4j-entities;create=true" ) ) );
    }

}
