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

import org.qipki.core.QiPkiApplication;

import org.tanukisoftware.wrapper.WrapperListener;
import org.tanukisoftware.wrapper.WrapperManager;

public class WrappedMain
{

    public static void main( final String[] mainArgs )
    {
        WrapperManager.start( new WrapperListener()
        {

            private QiPkiApplication app;

            @Override
            public Integer start( String[] strings )
            {
                app = new Main( mainArgs ).bootstrap();
                WrapperManager.signalStarting( 30 );
                app.run();
                WrapperManager.signalStarting( 1 );
                return null;
            }

            @Override
            public int stop( int exitCode )
            {
                if ( app != null ) {
                    app.stop();
                    app = null;
                }
                return exitCode;
            }

            @Override
            public void controlEvent( int i )
            {
            }

        }, mainArgs );
    }

}
