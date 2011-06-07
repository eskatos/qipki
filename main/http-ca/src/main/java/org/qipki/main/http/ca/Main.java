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
import java.util.Calendar;

import org.qi4j.bootstrap.ApplicationAssembler;
import org.qi4j.library.fileconfig.FileConfigurationOverride;

import org.qipki.ca.http.assembly.QiPkiHttpCaAssembler;
import org.qipki.core.AbstractQiPkiApplication;
import org.qipki.core.QiPkiApplication;
import org.qipki.main.core.QiPkiMain;
import org.qipki.main.core.QiPkiApplicationArguments;

/**
 *   An engineer, a chemist, and a standards designer are stranded on a desert
 *   island with absolutely nothing on it.  One of them finds a can of spam washed
 *   up by the waves.
 *
 *   The engineer says "Taking the strength of the seams into account, we can
 *   calculate that bashing it against a rock with a given force will open it up
 *   without destroying the contents".
 *
 *   The chemist says "Taking the type of metal the can is made of into account,
 *   we can calculate that further immersion in salt water will corrode it enough
 *   to allow it to be easily opened after a day".
 *
 *   The standards designer gives the other two a condescending look, gazes into
 *   the middle distance, and begins "Assuming we have an electric can opener...".
 *
 *                                                       X.509 Style Guide - October 2000
 *                                               Peter Gutmann, pgut001@cs.auckland.ac.nz
 *                               http://www.cs.auckland.ac.nz/~pgut001/pubs/x509guide.txt
 *
 *   TODO Find citation from http://www.cs.auckland.ac.nz/~pgut001/pubs/pkitutorial.pdf
 */
public class Main
        extends QiPkiMain
{

    public static void main( String[] mainArgs )
            throws IOException
    {
        final QiPkiApplication app = new Main( mainArgs ).bootstrap();
        app.run();
        Runtime.getRuntime().addShutdownHook( new Thread( new Runnable()
        {

            @Override
            public void run()
            {
                app.stop();
            }

        } ) );
    }

    public Main( String[] mainArgs )
    {
        super( mainArgs );
    }

    @Override
    protected QiPkiApplication buildApplication( QiPkiApplicationArguments args )
    {
        FileConfigurationOverride fileConfigOverride = args.buildFileConfigOverride();
        ApplicationAssembler appAssembler = new QiPkiHttpCaAssembler( QiPkiHttpCaArtifactInfo.NAME, args.getMode(),
                                                                      "jdbc:derby:" + new File( fileConfigOverride.data(), "ca-store" ).getAbsolutePath() + ";create=true",
                                                                      new File( fileConfigOverride.data(), "ca-index" ).getAbsolutePath(),
                                                                      args.getJmxPort() ).withFileConfigurationOverride( fileConfigOverride );
        return new AbstractQiPkiApplication( appAssembler )
        {

            @Override
            protected void afterActivate()
            {
                System.out.println( "After application activation" );
            }

            @Override
            protected void beforePassivate()
            {
                System.out.println( "Before application passivation" );
            }

        };
    }

    @Override
    protected void outputBanner( PrintWriter out )
    {
        out.println();
        out.println( QiPkiHttpCaArtifactInfo.NAME + ' ' + QiPkiHttpCaArtifactInfo.INCEPTION_YEAR + '-' + Calendar.getInstance().get( Calendar.YEAR ) );
        out.println( QiPkiHttpCaArtifactInfo.VERSION );
        out.println();
    }

}
