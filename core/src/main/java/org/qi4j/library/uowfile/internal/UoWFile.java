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
package org.qi4j.library.uowfile.internal;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.qi4j.api.io.Inputs;
import org.qi4j.api.io.Outputs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UoWFile
{

    /* package */ static final Logger LOGGER = LoggerFactory.getLogger( "org.qi4j.library.uowfile" );
    private static final int FILE_BUFFER_SIZE = 4096;
    private static final AtomicLong COUNT = new AtomicLong( 0L );
    private final long originalIdentity;
    private final File original;
    private final File current;
    private final File backup;
    private boolean copied = false;

    UoWFile( File original )
    {
        this.originalIdentity = original.length() + original.lastModified();
        this.original = original;
        long count = COUNT.incrementAndGet();
        this.current = new File( original.getParentFile(), original.getName() + ".current." + count );
        this.backup = new File( original.getParentFile(), original.getName() + ".backup." + count );
    }

    public File asFile()
    {
        return current;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder().append( UoWFile.class.getSimpleName() );
        // UoWFile{parent/( original(oid->id) | current(id) | backup(id) )}
        sb.append( "{" ).append( original.getParentFile().getName() ).append( "/( " ).
                append( original.getName() ).append( "(" ).append( originalIdentity ).append( "->" ).append( fileTag( original ) ).append( ") | " ).
                append( current.getName() ).append( "(" ).append( fileTag( current ) ).append( ") | " ).
                append( backup.getName() ).append( "(" ).append( fileTag( backup ) ).
                append( ") )}" );
        return sb.toString();
    }

    void copyOriginalToCurrent()
    {
        try {
            if ( original.exists() ) {
                Inputs.byteBuffer( original, FILE_BUFFER_SIZE ).transferTo( Outputs.byteBuffer( current ) );
            }
        } catch ( IOException ex ) {
            throw new UoWFileException( ex );
        }
    }

    void apply()
            throws ConcurrentUoWFileStateModificationException
    {
        LOGGER.trace( "Will apply changes to {}", this );
        if ( fileTag( current ) != originalIdentity ) {
            if ( fileTag( original ) != originalIdentity ) {
                LOGGER.info( "Concurrent modification, original creation identity is {} and original apply identity is {}", originalIdentity, fileTag( original ) );
                throw new ConcurrentUoWFileStateModificationException( this );
            }
            if ( original.exists() ) {
                move( original, backup );
            }
            if ( current.exists() ) {
                move( current, original );
            }
            LOGGER.debug( "Applied changes to {}", original );
        }
    }

    void rollback()
    {
        if ( backup.exists() ) {
            if ( fileTag( original ) != originalIdentity ) {
                delete( original );
                move( backup, original );
            }
            LOGGER.debug( "Restored backup to {}", original );
        }
    }

    void cleanup()
    {
        if ( current.exists() ) {
            delete( current );
        }
        if ( backup.exists() ) {
            delete( backup );
        }
    }

    /**
     * @return OL if the file does not exist
     */
    private long fileTag( File file )
    {
        return file.length() + file.lastModified();
    }

    private void delete( File file )
    {
        if ( !file.delete() ) {
            throw new UoWFileException( new IOException( "Unable to delete file " + file ) );
        }
    }

    private void move( File source, File dest )
    {
        if ( !source.renameTo( dest ) ) {
            throw new UoWFileException( new IOException( "Unable to move file " + source + " to " + dest ) );
        }
    }

}
