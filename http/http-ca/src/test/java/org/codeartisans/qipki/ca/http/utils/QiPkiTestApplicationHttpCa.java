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
package org.codeartisans.qipki.ca.http.utils;

import org.codeartisans.qipki.ca.http.assembly.QiPkiHttpCaAssembler;
import org.codeartisans.qipki.core.AbstractQiPkiApplication;

import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;

public class QiPkiTestApplicationHttpCa
        extends AbstractQiPkiApplication
{

    public QiPkiTestApplicationHttpCa()
    {
        super( new QiPkiHttpCaAssembler( "jdbc:derby:target/qi4j-entities;create=true", "target/qi4j-index" )
        {

            @Override
            @SuppressWarnings( "unchecked" )
            protected void assembleDevTestModule( ModuleAssembly devTestModule )
                    throws AssemblyException
            {
                devTestModule.addServices( QiPkiCaFixtures.class ).instantiateOnStartup();
            }

        } );
    }

}
