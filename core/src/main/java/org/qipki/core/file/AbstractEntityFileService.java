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
package org.qipki.core.file;

import java.io.File;

import org.qi4j.library.fileconfig.FileConfiguration;

import org.qipki.core.QiPkiFailure;

public abstract class AbstractEntityFileService
{

    protected final FileConfiguration fileConfig;

    public AbstractEntityFileService( FileConfiguration fileConfig )
    {
        this.fileConfig = fileConfig;
    }

    protected final File ensureDataDir( String dataDirName )
    {
        File dataDir = new File( fileConfig.dataDirectory(), dataDirName );
        if ( dataDir.exists() ) {
            if ( !dataDir.isDirectory() ) {
                throw new QiPkiFailure( dataDirName + " already exists and is not a directory" );
            }
        } else {
            if ( !dataDir.mkdirs() ) {
                throw new QiPkiFailure( dataDirName + " directory do not exists and was unable to create it" );
            }
        }
        return dataDir;
    }

}
