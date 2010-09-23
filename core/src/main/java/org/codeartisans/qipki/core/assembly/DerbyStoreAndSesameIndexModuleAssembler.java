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
package org.codeartisans.qipki.core.assembly;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.entitystore.sql.assembly.DerbySQLEntityStoreAssembler;
import org.qi4j.entitystore.sql.internal.datasource.DataSourceService;
import org.qi4j.index.rdf.assembly.RdfNativeSesameStoreAssembler;

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
            new DerbySQLEntityStoreAssembler( visibility, dataSourceService ).assemble( module );
        } else {
            new DerbySQLEntityStoreAssembler( visibility ).assemble( module );
        }
        new RdfNativeSesameStoreAssembler( null, visibility, visibility ).assemble( module );
    }

}
