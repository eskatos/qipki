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
package org.codeartisans.qipki.ca.assembly;

import javax.sql.DataSource;

import org.codeartisans.qipki.core.assembly.AssemblyUtil;
import org.codeartisans.qipki.core.assembly.index.AutomaticReindexingAssembler;
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
import org.qi4j.library.sql.ds.DBCPDataSourceConfiguration;
import org.qi4j.library.sql.ds.assembly.ImportableDataSourceService;

public class QiPkiPersistentEmbeddedCaAssembler
        extends QiPkiEmbeddedCaAssembler
{

    private final String connectionString;
    private final DataSource dataSource;
    private final String finderDataDirectory;

    public QiPkiPersistentEmbeddedCaAssembler( String connectionString, String finderDataDirectory )
    {
        this.connectionString = connectionString;
        this.dataSource = null;
        this.finderDataDirectory = finderDataDirectory;
    }

    public QiPkiPersistentEmbeddedCaAssembler( DataSource dataSource, String finderDataDirectory )
    {
        this.connectionString = null;
        this.finderDataDirectory = finderDataDirectory;
        this.dataSource = dataSource;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public ApplicationAssembly assemble( ApplicationAssemblyFactory applicationFactory )
            throws AssemblyException
    {
        ApplicationAssembly appAssembly = super.assemble( applicationFactory );

        LayerAssembly infrastructure = appAssembly.layerAssembly( CaAssemblyNames.LAYER_INFRASTRUCTURE );
        {
            ModuleAssembly config = infrastructure.moduleAssembly( CaAssemblyNames.MODULE_CONFIGURATION );
            config.addServices( MemoryEntityStoreService.class ).visibleIn( Visibility.module );
            config.forMixin( NativeConfiguration.class ).declareDefaults().dataDirectory().set( finderDataDirectory );

            ModuleAssembly persistenceMa = infrastructure.moduleAssembly( CaAssemblyNames.MODULE_PERSISTENCE );

            if ( dataSource != null ) {

                new DerbyStoreAndSesameIndexModuleAssembler( Visibility.application, new ImportableDataSourceService( dataSource ) ).assemble( persistenceMa );

                config.addEntities( SQLConfiguration.class, NativeConfiguration.class ).visibleIn( Visibility.layer );

            } else {

                new DerbyStoreAndSesameIndexModuleAssembler( Visibility.application ).assemble( persistenceMa );

                config.addEntities( DBCPDataSourceConfiguration.class, SQLConfiguration.class, NativeConfiguration.class ).visibleIn( Visibility.layer );
                config.forMixin( DBCPDataSourceConfiguration.class ).declareDefaults().url().set( connectionString );

            }

            new AutomaticReindexingAssembler().assemble( persistenceMa );
        }

        LayerAssembly domain = AssemblyUtil.getLayerAssembly( appAssembly, CaAssemblyNames.LAYER_DOMAIN );
        domain.uses( infrastructure );

        return appAssembly;
    }

}
