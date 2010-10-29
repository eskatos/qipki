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
package org.codeartisans.qipki.core.assembly;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.entitystore.sql.assembly.DerbySQLEntityStoreAssembler;
import org.qi4j.index.rdf.assembly.RdfNativeSesameStoreAssembler;
import org.qi4j.library.sql.ds.DataSourceService;
import org.qi4j.library.sql.ds.assembly.DataSourceAssembler;

public class DerbyStoreAndSesameIndexModuleAssembler
        implements Assembler
{

    private final Visibility visibility;
    private final DataSourceService dataSourceService;

    public DerbyStoreAndSesameIndexModuleAssembler()
    {
        this( Visibility.application );
    }

    public DerbyStoreAndSesameIndexModuleAssembler( Visibility visibility )
    {
        this( visibility, null );
    }

    public DerbyStoreAndSesameIndexModuleAssembler( Visibility visibility, DataSourceService dataSourceService )
    {
        this.visibility = visibility;
        this.dataSourceService = dataSourceService;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        if ( dataSourceService != null ) {
            new DerbySQLEntityStoreAssembler( visibility, new DataSourceAssembler( dataSourceService ) ).assemble( module );
        } else {
            new DerbySQLEntityStoreAssembler( visibility ).assemble( module );
        }
        new RdfNativeSesameStoreAssembler( null, visibility, visibility ).assemble( module );
    }

}
