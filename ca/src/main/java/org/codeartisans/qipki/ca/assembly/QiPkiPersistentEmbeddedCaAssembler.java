/*
 * Copyright (c) 2010 Paul Merlin <paul@nosphere.org>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.codeartisans.qipki.ca.assembly;

import org.codeartisans.qipki.core.assembly.AssemblyUtil;
import org.codeartisans.qipki.core.assembly.DerbyStoreAndSesameIndexModuleAssembler;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.ApplicationAssemblyFactory;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.LayerAssembly;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.entitystore.memory.MemoryEntityStoreService;
import org.qi4j.library.rdf.repository.NativeConfiguration;
import org.qi4j.library.sql.common.SQLConfiguration;

public class QiPkiPersistentEmbeddedCaAssembler
        extends QiPkiEmbeddedCaAssembler
{

    private final String connectionString;
    private final String finderDataDirectory;

    public QiPkiPersistentEmbeddedCaAssembler( String connectionString, String finderDataDirectory )
    {
        this.connectionString = connectionString;
        this.finderDataDirectory = finderDataDirectory;
    }

    @Override
    public ApplicationAssembly assemble( ApplicationAssemblyFactory applicationFactory )
            throws AssemblyException
    {
        ApplicationAssembly appAssembly = super.assemble( applicationFactory );

        LayerAssembly infrastructure = appAssembly.layerAssembly( AssemblyNames.LAYER_INFRASTRUCTURE );
        {
            new DerbyStoreAndSesameIndexModuleAssembler( Visibility.application ).assemble(
                    infrastructure.moduleAssembly( AssemblyNames.MODULE_PERSISTENCE ) );
            ModuleAssembly config = infrastructure.moduleAssembly( AssemblyNames.MODULE_CONFIGURATION );
            config.addServices( MemoryEntityStoreService.class ).visibleIn( Visibility.module );
            config.addEntities( SQLConfiguration.class, NativeConfiguration.class ).visibleIn( Visibility.layer );
            config.forMixin( SQLConfiguration.class ).declareDefaults().connectionString().set( connectionString );
            config.forMixin( NativeConfiguration.class ).declareDefaults().dataDirectory().set( finderDataDirectory );
        }

        LayerAssembly domain = AssemblyUtil.getLayerAssembly( appAssembly, AssemblyNames.LAYER_DOMAIN );

        domain.uses( infrastructure );

        return appAssembly;
    }

}
