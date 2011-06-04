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

import java.util.Calendar;
import java.io.IOException;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import static org.qipki.main.http.ca.QiPkiHttpCaOptionParser.Options.*;

public class Main
{

    public static void main( String[] args )
            throws IOException
    {
        OptionParser parser = new QiPkiHttpCaOptionParser();
        try {

            OptionSet options = parser.parse( args );

            if ( options.has( HELP ) || !options.nonOptionArguments().isEmpty() ) {
                printHelp( parser );
                System.exit( 0 );
            }

        } catch ( OptionException ex ) {

            System.out.println( ex.getMessage() );
            printHelp( parser );
            System.exit( 1 );

        }
    }

    /* package */ static void printHelp( OptionParser parser )
            throws IOException
    {
        System.out.println();
        System.out.println( QiPkiHttpCaArtifactInfo.NAME + ' ' + QiPkiHttpCaArtifactInfo.INCEPTION_YEAR + '-' + Calendar.getInstance().get( Calendar.YEAR ) );
        System.out.println( QiPkiHttpCaArtifactInfo.VERSION );
        System.out.println();
        parser.printHelpOn( System.out );
        System.out.println();
    }

}
