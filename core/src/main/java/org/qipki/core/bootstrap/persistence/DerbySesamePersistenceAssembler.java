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
package org.qipki.core.bootstrap.persistence;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.entitystore.sql.assembly.DerbySQLEntityStoreAssembler;
import org.qi4j.index.rdf.assembly.RdfNativeSesameStoreAssembler;
import org.qi4j.library.rdf.repository.NativeConfiguration;
import org.qi4j.library.sql.assembly.DataSourceAssembler;
import org.qi4j.library.sql.common.SQLConfiguration;
import org.qi4j.library.sql.datasource.DataSourceConfiguration;
import org.qi4j.library.sql.dbcp.DBCPDataSourceServiceAssembler;

/**
 * Apache Derby entities and Sesame indexing/query.
 *
 * Derby use either a JDBC connection string or a DataSourceService Sesame use files located by the FileConfiguration
 * API.
 */
public class DerbySesamePersistenceAssembler
        implements PersistenceAssembler
{

    private final String derbyConnectionString;
    private ModuleAssembly configModule;

    public DerbySesamePersistenceAssembler( String derbyConnectionString )
    {
        this.derbyConnectionString = derbyConnectionString;
    }

    @Override
    public void withConfigModule( ModuleAssembly config )
    {
        this.configModule = config;
    }

    @Override
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        ModuleAssembly config = configModule == null ? module : configModule;

        // SQL DataSourceService
        new DBCPDataSourceServiceAssembler().
                identifiedBy( "qipki-datasource-service" ).
                visibleIn( Visibility.application ).
                withConfig( config ).
                withConfigVisibility( Visibility.application ).
                assemble( module );

        // SQL DataSource
        new DataSourceAssembler().
                withDataSourceServiceIdentity( "qipki-datasource-service" ).
                identifiedBy( "qipki-datasource" ).
                visibleIn( Visibility.application ).
                withCircuitBreaker().
                assemble( module );
        DataSourceConfiguration dsConfiguration = config.forMixin( DataSourceConfiguration.class ).declareDefaults();
        dsConfiguration.enabled().set( Boolean.TRUE );
        dsConfiguration.driver().set( "org.apache.derby.jdbc.EmbeddedDriver" ); // TODO FIXME Support both client and embedded driver!
        dsConfiguration.url().set( derbyConnectionString );

        // SQL EntityStore
        new DerbySQLEntityStoreAssembler().
                visibleIn( Visibility.application ).
                withConfig( config ).
                withConfigVisibility( Visibility.application ).
                assemble( module );
        SQLConfiguration sqlConfiguration = config.forMixin( SQLConfiguration.class ).declareDefaults();
        sqlConfiguration.schemaName().set( "qipki" );

        // RDF Index & Query
        new RdfNativeSesameStoreAssembler( Visibility.application, Visibility.application ).assemble( module );
        config.entities( NativeConfiguration.class ).visibleIn( Visibility.application );
    }

}
