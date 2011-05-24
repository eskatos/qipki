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
package org.qipki.ca.assembly;

import org.qipki.core.assembly.AssemblyUtil;
import org.qipki.core.assembly.InMemoryStoreAndIndexModuleAssembler;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.ApplicationAssemblyFactory;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.LayerAssembly;

public class QiPkiVolatileEmbeddedCaAssembler
        extends QiPkiEmbeddedCaAssembler
{

    @Override
    public ApplicationAssembly assemble( ApplicationAssemblyFactory applicationFactory )
            throws AssemblyException
    {
        ApplicationAssembly appAssembly = super.assemble( applicationFactory );

        LayerAssembly infrastructure = appAssembly.layer( CaAssemblyNames.LAYER_INFRASTRUCTURE );
        {
            new InMemoryStoreAndIndexModuleAssembler( Visibility.application ).assemble(
                    infrastructure.module( CaAssemblyNames.MODULE_PERSISTENCE ) );
        }

        LayerAssembly domain = AssemblyUtil.getLayerAssembly( appAssembly, CaAssemblyNames.LAYER_DOMAIN );

        domain.uses( infrastructure );

        return appAssembly;
    }

}