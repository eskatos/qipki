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
package org.qipki.main.core;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import joptsimple.OptionException;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import static org.qipki.main.core.DefaultFileConfigOptions.*;
import org.qipki.core.QiPkiApplication;
import org.qipki.core.dci.Context;
import static org.qipki.main.core.QiPkiOptions.*;

import org.slf4j.bridge.SLF4JBridgeHandler;

public abstract class QiPkiMain<RootContextType extends Context>
{

    private static final String BASEDIR = System.getProperty( "basedir" );
    private final String[] mainArgs;

    public QiPkiMain( String[] mainArgs )
    {
        this.mainArgs = mainArgs;
    }

    protected abstract QiPkiApplication<RootContextType> buildApplication( QiPkiApplicationArguments args );

    protected abstract void outputBanner( PrintWriter out );

    public final QiPkiApplication<RootContextType> bootstrap()
    {

        QiPkiOptionParser parser = new QiPkiOptionParser();

        try {

            OptionSet options = parser.parse( mainArgs );

            if ( options.has( HELP ) || !options.nonOptionArguments().isEmpty() ) {
                printHelp( parser );
                System.exit( 0 );
            }

            QiPkiApplicationArguments args = new QiPkiApplicationArguments();

            args.setVerbose( options.has( VERBOSE ) );
            args.setMode( options.valueOf( parser.getModeSpec() ) );
            args.setConfiguration( resolveDirectory( CONFIGURATION_DIR, options, parser.getConfigurationDirSpec(), DEFAULT_CONFIGURATION_DIR ) );
            args.setData( resolveDirectory( DATA_DIR, options, parser.getDataDirSpec(), DEFAULT_DATA_DIR ) );
            args.setTemporary( resolveDirectory( TEMPORARY_DIR, options, parser.getTemporaryDirSpec(), DEFAULT_TEMPORARY_DIR ) );
            args.setCache( resolveDirectory( CACHE_DIR, options, parser.getCacheDirSpec(), DEFAULT_CACHE_DIR ) );
            args.setLog( resolveDirectory( LOG_DIR, options, parser.getLogDirSpec(), DEFAULT_LOG_DIR ) );
            args.setJmxPort( options.valueOf( parser.getJMXPortSpec() ) );
            args.setHost( options.valueOf( parser.getHostSpec() ) );
            args.setPort( options.valueOf( parser.getPortSpec() ) );

            if ( args.isVerbose() ) {
                System.out.println( args );
            }


            bindJDKLoggersToSLF4J();

            return buildApplication( args );

        } catch ( OptionException ex ) {

            System.out.println( ex.getMessage() );
            printHelp( parser );
            System.exit( 1 );
            return null;
        }
    }

    private void printHelp( QiPkiOptionParser parser )
    {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter( sw );
            outputBanner( pw );
            parser.printHelpOn( pw );
            pw.println();
            pw.flush();
            System.out.println( sw.toString() );
        } catch ( IOException ex ) {
            throw new RuntimeException( "Unable to print help, something is really wrong", ex );
        }
    }

    private File resolveDirectory( String ilk, OptionSet options, OptionSpec<File> optionSpec, String baseDirChild )
    {
        File dir = options.valueOf( optionSpec );
        if ( dir == null ) {
            if ( BASEDIR == null ) {
                throw new IllegalStateException( "No " + ilk + " directory given and basedir system property is not set" );
            }
            dir = new File( BASEDIR, baseDirChild );
        }
        return dir;
    }

    private void bindJDKLoggersToSLF4J()
    {
        Logger rootLogger = LogManager.getLogManager().getLogger( "" );
        for ( Handler eachHandler : rootLogger.getHandlers() ) {
            rootLogger.removeHandler( eachHandler );
        }
        SLF4JBridgeHandler.install();
    }

}
