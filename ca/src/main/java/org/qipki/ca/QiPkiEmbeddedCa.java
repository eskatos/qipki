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
package org.qipki.ca;

import java.io.File;
import javax.sql.DataSource;
import org.qi4j.api.structure.Application.Mode;

import org.qipki.ca.application.contexts.RootContext;
import org.qipki.ca.assembly.CaAssemblyNames;
import org.qipki.ca.assembly.QiPkiPersistentEmbeddedCaAssembler;
import org.qipki.ca.assembly.QiPkiVolatileEmbeddedCaAssembler;
import org.qipki.core.AbstractQiPkiApplication;
import org.qipki.core.dci.InteractionContext;

import org.qi4j.api.structure.Module;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

public class QiPkiEmbeddedCa
        extends AbstractQiPkiApplication
{

    private Module dciModule;

    /**
     * Instanciate an embedded QiPki CA application using in-memory storage.
     */
    public QiPkiEmbeddedCa( Mode appMode )
    {
        super( new QiPkiVolatileEmbeddedCaAssembler( CaAssemblyNames.APPLICATION_NAME, appMode ) );
    }

    /**
     * Instanciate an embedded QiPki CA application using Apache Derby storage and Sesame RDF index.
     *
     * @param storePath     Path of the Apache Derby database
     */
    public QiPkiEmbeddedCa( Mode appMode, File storePath )
    {
        super( new QiPkiPersistentEmbeddedCaAssembler( CaAssemblyNames.APPLICATION_NAME, appMode, "jdbc:derby:" + storePath.getAbsolutePath() + ";create=true" ) );
    }

    /**
     * Instanciate an embedded QiPki CA application using Apache Derby from a DataSource and Sesame RDF index.
     * 
     * @param dataSource    DataSource to use
     */
    public QiPkiEmbeddedCa( Mode appMode, DataSource dataSource )
    {
        super( new QiPkiPersistentEmbeddedCaAssembler( CaAssemblyNames.APPLICATION_NAME, appMode, dataSource ) );
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
