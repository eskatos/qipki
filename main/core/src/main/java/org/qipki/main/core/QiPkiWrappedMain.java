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

import org.qipki.core.QiPkiApplication;
import org.qipki.core.dci.Context;

import org.tanukisoftware.wrapper.WrapperListener;
import org.tanukisoftware.wrapper.WrapperManager;

public abstract class QiPkiWrappedMain<RootContextType extends Context>
{

    private final WrapperListener listener = new WrapperListener()
    {

        @Override
        public final Integer start( String[] args )
        {
            QiPkiApplication<RootContextType> application = buildQiPkiMain( args ).bootstrap();
            WrapperManager.signalStarting( 60 );
            application.run();
            WrapperManager.signalStarting( 1 );
            app = application;
            return null;
        }

        @Override
        public final int stop( int exitCode )
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

    };
    private final String[] args;
    private QiPkiApplication<RootContextType> app;

    public QiPkiWrappedMain( String[] args )
    {
        this.args = args;
    }

    protected abstract QiPkiMain<RootContextType> buildQiPkiMain( String[] args );

    public final void startWrapped()
    {
        WrapperManager.start( listener, args );
    }

}
