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
package org.qipki.ca.bootstrap;

import javax.sql.DataSource;

import org.qi4j.api.common.Visibility;
import org.qi4j.api.structure.Application.Mode;
import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.ApplicationAssemblyFactory;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.LayerAssembly;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.library.rdf.repository.NativeConfiguration;
import org.qi4j.library.sql.common.SQLConfiguration;
import org.qi4j.library.sql.ds.DBCPDataSourceConfiguration;
import org.qi4j.library.sql.ds.assembly.ImportableDataSourceService;

import org.qipki.core.bootstrap.AssemblyUtil;
import org.qipki.core.bootstrap.DerbyStoreAndSesameIndexModuleAssembler;

public class QiPkiPersistentEmbeddedCaAssembler
        extends QiPkiEmbeddedCaAssembler
{

    private final String connectionString;
    private final DataSource dataSource;

    public QiPkiPersistentEmbeddedCaAssembler( String appName, Mode appMode, String connectionString )
    {
        super( appName, appMode );
        this.connectionString = connectionString;
        this.dataSource = null;
    }

    public QiPkiPersistentEmbeddedCaAssembler( String appName, Mode appMode, DataSource dataSource )
    {
        super( appName, appMode );
        this.connectionString = null;
        this.dataSource = dataSource;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public ApplicationAssembly assemble( ApplicationAssemblyFactory applicationFactory )
            throws AssemblyException
    {
        ApplicationAssembly appAssembly = super.assemble( applicationFactory );


        LayerAssembly infrastructure = appAssembly.layer( CaAssemblyNames.LAYER_INFRASTRUCTURE );
        {
            ModuleAssembly persistenceMa = infrastructure.module( CaAssemblyNames.MODULE_PERSISTENCE );

            if ( dataSource != null ) {
                new DerbyStoreAndSesameIndexModuleAssembler( Visibility.application, new ImportableDataSourceService( dataSource ) ).assemble( persistenceMa );
            } else {
                new DerbyStoreAndSesameIndexModuleAssembler( Visibility.application ).assemble( persistenceMa );
            }

        }

        LayerAssembly config = AssemblyUtil.getLayerAssembly( appAssembly, CaAssemblyNames.LAYER_CONFIGURATION );
        {
            ModuleAssembly configMa = AssemblyUtil.getModuleAssembly( appAssembly, CaAssemblyNames.LAYER_CONFIGURATION, CaAssemblyNames.MODULE_CONFIGURATION );
            configMa.entities( NativeConfiguration.class ).visibleIn( Visibility.application );

            if ( dataSource != null ) {
                configMa.entities( SQLConfiguration.class ).visibleIn( Visibility.application );
            } else {
                configMa.entities( DBCPDataSourceConfiguration.class, SQLConfiguration.class ).visibleIn( Visibility.application );
                configMa.forMixin( DBCPDataSourceConfiguration.class ).declareDefaults().url().set( connectionString );
            }
        }

        LayerAssembly domain = AssemblyUtil.getLayerAssembly( appAssembly, CaAssemblyNames.LAYER_DOMAIN );

        domain.uses( infrastructure );
        infrastructure.uses( config );

        return appAssembly;
    }

}
