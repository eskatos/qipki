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
package org.qipki.ca.http.assembly;

import org.qipki.ca.http.presentation.http.HttpService;
import org.qipki.ca.http.presentation.http.RootServletService;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import static org.qi4j.library.http.Servlets.*;

public class HttpModuleAssembler
        implements Assembler
{

    /**
     * Servlets are added with layer visibility.
     * 
     * @param module                the Module to assemble
     * @throws AssemblyException    thrown if the assembler tries to do something illegal
     */
    @Override
    @SuppressWarnings( "unchecked" )
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        module.addServices( HttpService.class ).visibleIn( Visibility.module ).instantiateOnStartup();
        addServlets( serve( "/" ).with( RootServletService.class ) ).to( module );
    }

}
