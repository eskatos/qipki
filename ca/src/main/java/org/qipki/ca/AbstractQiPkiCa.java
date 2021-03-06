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
package org.qipki.ca;

import org.qi4j.api.structure.Application;
import org.qi4j.api.structure.Module;
import org.qi4j.bootstrap.ApplicationAssembler;
import org.qipki.ca.application.contexts.RootContext;
import org.qipki.ca.bootstrap.CaAssemblyNames;
import org.qipki.core.AbstractQiPkiApplication;
import org.qipki.core.bootstrap.ModuleFinder;

public class AbstractQiPkiCa
    extends AbstractQiPkiApplication<RootContext>
{

    private static ModuleFinder dciFinder = new ModuleFinder()
    {

        @Override
        public Module findModule( Application application )
        {
            return application.findModule( CaAssemblyNames.LAYER_APPLICATION, CaAssemblyNames.MODULE_CA_DCI );
        }

    };

    protected AbstractQiPkiCa( ApplicationAssembler appAssembler )
    {
        super( appAssembler );
    }

    @Override
    protected final ModuleFinder dciModuleFinder()
    {
        return dciFinder;
    }

    @Override
    protected final Class<RootContext> rootContextType()
    {
        return RootContext.class;
    }

}
