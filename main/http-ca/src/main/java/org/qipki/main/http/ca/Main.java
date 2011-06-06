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

import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.qipki.ca.http.QiPkiHttpCa;
import static org.qipki.main.http.ca.QiPkiHttpCaOptionParser.Options.*;

public class Main
{

    public static void main( String[] args )
            throws IOException
    {
        QiPkiHttpCaOptionParser parser = new QiPkiHttpCaOptionParser();
        try {

            OptionSet options = parser.parse( args );

            if ( options.has( HELP ) || !options.nonOptionArguments().isEmpty() ) {
                printHelp( parser );
                System.exit( 0 );
            }

            boolean verbose = options.has( VERBOSE );
            File dataDir = options.valueOf( parser.getDataDirSpec() );
            Integer jmxPort = options.valueOf( parser.getJMXPortSpec() );
            String host = options.valueOf( parser.getHostSpec() );
            Integer port = options.valueOf( parser.getPortSpec() );

            if ( verbose ) {
                System.out.println( QiPkiHttpCaArtifactInfo.NAME + " will start with the following options:" );
                System.out.println( "\tverbose: " + verbose );
                System.out.println( "\tdata-dir: " + dataDir.getAbsolutePath() );
                System.out.println( "\thost: " + host );
                System.out.println( "\tport: " + port );
            }

            final QiPkiHttpCa qiPkiHttpCa = new QiPkiHttpCa( dataDir, jmxPort );
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
            printHelp( parser );
            System.exit( 1 );

        }
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
        System.out.println( sw );
    }

}
