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
package org.codeartisans.qipki.ca;

import java.io.File;
import javax.sql.DataSource;

import org.codeartisans.qipki.ca.application.contexts.RootContext;
import org.codeartisans.qipki.ca.assembly.CaAssemblyNames;
import org.codeartisans.qipki.ca.assembly.QiPkiPersistentEmbeddedCaAssembler;
import org.codeartisans.qipki.ca.assembly.QiPkiVolatileEmbeddedCaAssembler;
import org.codeartisans.qipki.core.AbstractQiPkiApplication;
import org.codeartisans.qipki.core.dci.InteractionContext;

import org.qi4j.api.structure.Module;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

public class QiPkiEmbeddedCa
        extends AbstractQiPkiApplication
{

    private Module dciModule;

    /**
     * Instanciate an embedded QiPki CA application using in-memory storage.
     */
    public QiPkiEmbeddedCa()
    {
        super( new QiPkiVolatileEmbeddedCaAssembler() );
    }

    /**
     * Instanciate an embedded QiPki CA application using Apache Derby storage and Sesame RDF index.
     *
     * @param storePath     Path of the Apache Derby database
     * @param indexPath     Path of the Sesame native RDF index
     */
    public QiPkiEmbeddedCa( File storePath, File indexPath )
    {
        super( new QiPkiPersistentEmbeddedCaAssembler( "jdbc:derby:" + storePath.getAbsolutePath() + ";create=true", indexPath.getAbsolutePath() ) );
    }

    /**
     * Instanciate an embedded QiPki CA application using Apache Derby from a DataSource and Sesame RDF index.
     * 
     * @param dataSource    DataSource to use
     * @param indexPath     Path of the Sesame native RDF index
     */
    public QiPkiEmbeddedCa( DataSource dataSource, File indexPath )
    {
        super( new QiPkiPersistentEmbeddedCaAssembler( dataSource, indexPath.getAbsolutePath() ) );
    }

    public UnitOfWorkFactory unitOfWorkFactory()
    {
        return ensureDCIModule().unitOfWorkFactory();
    }

    public RootContext newRootContext()
    {
        return ensureDCIModule().objectBuilderFactory().newObjectBuilder( RootContext.class ).use( new InteractionContext() ).newInstance();
    }

    private synchronized Module ensureDCIModule()
    {
        if ( dciModule == null ) {
            dciModule = application.findModule( CaAssemblyNames.LAYER_APPLICATION, CaAssemblyNames.MODULE_CA_DCI );
        }
        return dciModule;
    }

    @Override
    protected void afterPassivate()
    {
        dciModule = null;
    }

}
