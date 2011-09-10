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
import java.util.HashMap;

import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWork;
import org.qi4j.api.unitofwork.UnitOfWorkCallback;
import org.qi4j.api.unitofwork.UnitOfWorkCallback.UnitOfWorkStatus;
import org.qi4j.api.unitofwork.UnitOfWorkCompletionException;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

@Mixins( UoWFileFactory.Mixin.class )
public interface UoWFileFactory
        extends ServiceComposite
{

    UoWFile createCurrentUoWFile( File file );

    UoWFile createUoWFile( UnitOfWork uow, File file );

    abstract class Mixin
            implements UoWFileFactory
    {

        private static class UoWFilesMetaInfo
                extends HashMap<String, UoWFile>
        {
        }

        @Structure
        private UnitOfWorkFactory uowf;

        @Override
        public UoWFile createCurrentUoWFile( File file )
        {
            return createUoWFile( uowf.currentUnitOfWork(), file );
        }

        @Override
        public UoWFile createUoWFile( UnitOfWork uow, File file )
        {
            UoWFilesMetaInfo uowMeta = ensureUoWMeta( uow );
            String absolutePath = file.getAbsolutePath();
            UoWFile uowFile = uowMeta.get( absolutePath );
            if ( uowFile == null ) {
                uowFile = new UoWFile( file );
                uowFile.copyOriginalToCurrent();
                uowMeta.put( absolutePath, uowFile );
            }
            return uowFile;
        }

        /**
         * Ensure UoW meta info tracking UoWFiles is present and UoW callback is registered.
         */
        private synchronized UoWFilesMetaInfo ensureUoWMeta( final UnitOfWork uow )
        {
            UoWFilesMetaInfo uowMeta = uow.metaInfo().get( UoWFilesMetaInfo.class );
            if ( uowMeta == null ) {

                uowMeta = new UoWFilesMetaInfo();
                uow.metaInfo().set( uowMeta );

                uow.addUnitOfWorkCallback( new UnitOfWorkCallback()
                {

                    @Override
                    public void beforeCompletion()
                            throws UnitOfWorkCompletionException
                    {
                        UoWFilesMetaInfo uowMeta = uow.metaInfo().get( UoWFilesMetaInfo.class );
                        if ( uowMeta != null && !uowMeta.isEmpty() ) {
                            for ( UoWFile eachUoWFile : uowMeta.values() ) {
                                eachUoWFile.apply();
                            }
                        }
                    }

                    @Override
                    public void afterCompletion( UnitOfWorkStatus status )
                    {
                        UoWFilesMetaInfo uowMeta = uow.metaInfo().get( UoWFilesMetaInfo.class );
                        if ( uowMeta != null && !uowMeta.isEmpty() ) {
                            switch ( status ) {
                                case COMPLETED:
                                    for ( UoWFile eachUoWFile : uowMeta.values() ) {
                                        eachUoWFile.cleanup();
                                    }
                                    break;
                                case DISCARDED:
                                    for ( UoWFile eachUoWFile : uowMeta.values() ) {
                                        eachUoWFile.restoreOriginal();
                                    }
                                    break;
                            }
                            uow.metaInfo().get( UoWFilesMetaInfo.class ).clear();
                        }
                    }

                } );
            }
            return uowMeta;
        }

    }

}
