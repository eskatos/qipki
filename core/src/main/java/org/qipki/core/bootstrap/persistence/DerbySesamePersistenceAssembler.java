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
import org.qi4j.library.sql.common.SQLConfiguration;
import org.qi4j.library.sql.ds.DBCPDataSourceConfiguration;
import org.qi4j.library.sql.ds.DataSourceService;
import org.qi4j.library.sql.ds.assembly.DataSourceAssembler;

/**
 * Apache Derby entities and Sesame indexing/query.
 * 
 * Derby use either a JDBC connection string or a DataSourceService
 * Sesame use files located by the FileConfiguration API.
 */
public class DerbySesamePersistenceAssembler
        implements PersistenceAssembler
{

    private final String derbyConnectionString;
    private final DataSourceService derbyDataSourceService;

    public DerbySesamePersistenceAssembler( String derbyConnectionString )
    {
        this.derbyConnectionString = derbyConnectionString;
        this.derbyDataSourceService = null;
    }

    public DerbySesamePersistenceAssembler( DataSourceService derbyDataSourceService )
    {
        this.derbyConnectionString = null;
        this.derbyDataSourceService = derbyDataSourceService;
    }

    @Override
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        // Derby
        if ( derbyDataSourceService != null ) {
            new DerbySQLEntityStoreAssembler( Visibility.application, new DataSourceAssembler( derbyDataSourceService ) ).assemble( module );
        } else {
            new DerbySQLEntityStoreAssembler( Visibility.application ).assemble( module );
        }
        // Sesame
        new RdfNativeSesameStoreAssembler( null, Visibility.application, Visibility.application ).assemble( module );
    }

    @Override
    public void assembleConfigModule( ModuleAssembly config )
            throws AssemblyException
    {
        // Derby
        if ( derbyDataSourceService != null ) {
            config.entities( SQLConfiguration.class ).visibleIn( Visibility.application );
        } else {
            config.entities( DBCPDataSourceConfiguration.class, SQLConfiguration.class ).visibleIn( Visibility.application );
            config.forMixin( DBCPDataSourceConfiguration.class ).declareDefaults().url().set( derbyConnectionString );
        }
        // Sesame
        config.entities( NativeConfiguration.class ).visibleIn( Visibility.application );
    }

}
