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

import org.qi4j.library.fileconfig.FileConfigurationOverride;

import org.qipki.ca.http.QiPkiHttpCa;
import org.qipki.ca.http.assembly.QiPkiHttpCaAssembler;
import org.qipki.core.QiPkiApplication;
import org.qipki.main.core.QiPkiMain;
import org.qipki.main.core.QiPkiApplicationArguments;

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
        QiPkiHttpCaAssembler appAssembler = new QiPkiHttpCaAssembler( QiPkiHttpCaArtifactInfo.NAME, args.getMode(),
                                                                      "jdbc:derby:" + new File( fileConfigOverride.data(), "ca-store" ).getAbsolutePath() + ";create=true",
                                                                      args.getJmxPort() );
        appAssembler.withFileConfigurationOverride( fileConfigOverride );

        return new QiPkiHttpCa( appAssembler );
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
