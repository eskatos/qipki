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
package org.qipki.main.http.ca;

import org.qi4j.api.structure.Application.Mode;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import org.qi4j.library.fileconfig.FileConfigurationOverride;

import org.qipki.ca.http.QiPkiHttpCa;
import static org.qipki.main.http.ca.QiPkiHttpCaOptionParser.Options.*;

public class Main
{

    private static final QiPkiHttpCaOptionParser PARSER = new QiPkiHttpCaOptionParser();
    private static final String BASEDIR = System.getProperty( "basedir" );

    public static void main( String[] args )
            throws IOException
    {
        try {

            OptionSet options = PARSER.parse( args );

            if ( options.has( HELP ) || !options.nonOptionArguments().isEmpty() ) {
                printHelp( PARSER );
                System.exit( 0 );
            }

            boolean verbose = options.has( VERBOSE );
            File configuration = resolveDirectory( CONFIGURATION_DIR, options, PARSER.getConfigurationDirSpec(), "etc" );
            File data = resolveDirectory( DATA_DIR, options, PARSER.getDataDirSpec(), "var/data" );
            File temporary = resolveDirectory( TEMPORARY_DIR, options, PARSER.getTemporaryDirSpec(), "tmp" );
            File cache = resolveDirectory( CACHE_DIR, options, PARSER.getCacheDirSpec(), "var/cache" );
            File log = resolveDirectory( LOG_DIR, options, PARSER.getLogDirSpec(), "var/log" );
            Integer jmxPort = options.valueOf( PARSER.getJMXPortSpec() );
            String host = options.valueOf( PARSER.getHostSpec() );
            Integer port = options.valueOf( PARSER.getPortSpec() );

            if ( verbose ) {
                System.out.println( QiPkiHttpCaArtifactInfo.NAME + " will start with the following options:" );
                System.out.println( "\tverbose: " + verbose );
                System.out.println( "\tconfiguration: " + configuration );
                System.out.println( "\tdata: " + data );
                System.out.println( "\ttemporary: " + temporary );
                System.out.println( "\tcache: " + cache );
                System.out.println( "\tlog: " + log );
                System.out.println( "\thost: " + host );
                System.out.println( "\tport: " + port );
            }

            final QiPkiHttpCa qiPkiHttpCa = new QiPkiHttpCa(
                    Mode.production,
                    new FileConfigurationOverride().withConfiguration( configuration ).
                    withData( data ).
                    withTemporary( temporary ).
                    withCache( cache ).
                    withLog( log ),
                    jmxPort );
            qiPkiHttpCa.run();

            Runtime.getRuntime().addShutdownHook( new Thread( new Runnable()
            {

                @Override
                public void run()
                {
                    qiPkiHttpCa.stop();
                }

            } ) );

        } catch ( OptionException ex ) {

            System.out.println( ex.getMessage() );
            printHelp( PARSER );
            System.exit( 1 );

        }
    }

    private static File resolveDirectory( String ilk, OptionSet options, OptionSpec<File> optionSpec, String baseDirChild )
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


    /* package */ static void printHelp( OptionParser parser )
            throws IOException
    {
        PrintWriter sw = new PrintWriter( new StringWriter() );
        sw.println();
        sw.println( QiPkiHttpCaArtifactInfo.NAME + ' ' + QiPkiHttpCaArtifactInfo.INCEPTION_YEAR + '-' + Calendar.getInstance().get( Calendar.YEAR ) );
        sw.println( QiPkiHttpCaArtifactInfo.VERSION );
        sw.println();
        parser.printHelpOn( sw );
        sw.println();
        System.out.println( sw.toString() );
    }

}
