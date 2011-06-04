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

import joptsimple.OptionParser;

/* package */ class QiPkiHttpCaOptionParser
        extends OptionParser
{

    /* package */ static interface Options
    {

        String HELP = "help";
        String VERBOSE = "verbose";
        String DAEMON = "daemon";
        String HOST = "host";
        String PORT = "port";
    }

    /* package */ QiPkiHttpCaOptionParser()
    {
        super();
        accepts( Options.HELP, "Show help" );
        accepts( Options.VERBOSE, "Turn on verbose mode" );
        accepts( Options.DAEMON, "Daemonize" );
        accepts( Options.HOST, "HTTP service hostname or IP address" ).withRequiredArg().ofType( String.class ).describedAs( "host" ).defaultsTo( "127.0.0.1" );
        accepts( Options.PORT, "HTTP service port number" ).withRequiredArg().ofType( Integer.class ).withValuesConvertedBy( new PortValueConverter() ).describedAs( "port" ).defaultsTo( 8443 );
    }

}
