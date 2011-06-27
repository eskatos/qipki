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
package org.qipki.core.bootstrap;

import org.codeartisans.java.toolbox.ObjectHolder;

import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.AssemblyVisitorAdapter;
import org.qi4j.bootstrap.LayerAssembly;
import org.qi4j.bootstrap.ModuleAssembly;

public final class AssemblyUtil
{

    public static LayerAssembly getLayerAssembly( ApplicationAssembly app, final String layerName )
            throws AssemblyException
    {
        final ObjectHolder<LayerAssembly> holder = new ObjectHolder<LayerAssembly>();
        app.visit( new AssemblyVisitorAdapter<AssemblyException>()
        {

            @Override
            public void visitLayer( LayerAssembly assembly )
                    throws AssemblyException
            {
                if ( layerName.equals( assembly.name() ) ) {
                    holder.setHolded( assembly );
                }
            }

        } );
        return holder.getHolded();
    }

    public static ModuleAssembly getModuleAssembly( ApplicationAssembly app, final String layerName, final String moduleName )
            throws AssemblyException
    {
        final ObjectHolder<ModuleAssembly> holder = new ObjectHolder<ModuleAssembly>();
        app.visit( new AssemblyVisitorAdapter<AssemblyException>()
        {

            @Override
            public void visitModule( ModuleAssembly assembly )
                    throws AssemblyException
            {
                if ( layerName.equals( assembly.layer().name() ) && moduleName.equals( assembly.name() ) ) {
                    holder.setHolded( assembly );
                }

            }

        } );

        return holder.getHolded();
    }

    private AssemblyUtil()
    {
    }

}