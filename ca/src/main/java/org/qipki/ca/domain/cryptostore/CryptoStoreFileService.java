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
package org.qipki.ca.domain.cryptostore;

import java.io.File;

import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.library.fileconfig.FileConfiguration;

import org.qipki.core.QiPkiFailure;

@Mixins( CryptoStoreFileService.Mixin.class )
public interface CryptoStoreFileService
        extends ServiceComposite
{

    File getKeyStoreFile( CryptoStore cryptoStore );

    abstract class Mixin
            implements CryptoStoreFileService
    {

        private static final String DATA_DIRNAME = "keystores";
        private static final String FILE_SUFFIX = "keystore";
        @Service
        private FileConfiguration fileConfig;

        @Override
        public File getKeyStoreFile( CryptoStore cryptoStore )
        {
            String keyStoreFileName = new StringBuilder( cryptoStore.identity().get() ).append( "-" ).
                    append( FILE_SUFFIX ).append( "." ).
                    append( cryptoStore.storeType().get().fileExtension() ).toString();
            return new File( ensureDataDir(), keyStoreFileName );
        }

        private File ensureDataDir()
        {
            File dataDir = new File( fileConfig.dataDirectory(), DATA_DIRNAME );
            if ( dataDir.exists() ) {
                if ( !dataDir.isDirectory() ) {
                    throw new IllegalStateException( DATA_DIRNAME + " already exists and is not a directory" );
                }
            } else {
                if ( !dataDir.mkdirs() ) {
                    throw new QiPkiFailure( DATA_DIRNAME + " directory do not exists and was unable to create it" );
                }
            }
            return dataDir;
        }

    }

}
