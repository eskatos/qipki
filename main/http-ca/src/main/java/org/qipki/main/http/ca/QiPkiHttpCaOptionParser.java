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
import joptsimple.OptionSpec;

/* package */ class QiPkiHttpCaOptionParser
        extends OptionParser
{

    /* package */ static interface Options
    {

        String HELP = "help";
        String VERBOSE = "verbose";
        String DAEMON = "daemon";
        String JMX_PORT = "jmx-port";
        String HOST = "host";
        String PORT = "port";
    }

    private final OptionSpec<Void> helpSpec;
    private final OptionSpec<Void> verboseSpec;
    private final OptionSpec<Void> daemonSpec;
    private final OptionSpec<Integer> jmxPortSpec;
    private final OptionSpec<String> hostSpec;
    private final OptionSpec<Integer> portSpec;


    /* package */ QiPkiHttpCaOptionParser()
    {
        super();

        PortValueConverter portConverter = new PortValueConverter();

        helpSpec = accepts( Options.HELP, "Show help" );
        verboseSpec = accepts( Options.VERBOSE, "Turn on verbose mode" );
        daemonSpec = accepts( Options.DAEMON, "Daemonize" );
        jmxPortSpec = accepts( Options.JMX_PORT, "Enable remote JMX on given port" ).
                withRequiredArg().
                ofType( Integer.class ).
                withValuesConvertedBy( portConverter ).
                describedAs( "JMX port" );
        hostSpec = accepts( Options.HOST, "HTTP service hostname or IP address" ).
                withRequiredArg().
                ofType( String.class ).
                describedAs( "host" ).
                defaultsTo( "127.0.0.1" );
        portSpec = accepts( Options.PORT, "HTTP service port number" ).
                withRequiredArg().
                ofType( Integer.class ).
                withValuesConvertedBy( portConverter ).
                describedAs( "port" ).
                defaultsTo( 8443 );
    }

    /* package */ OptionSpec<Void> getHelpSpec()
    {
        return helpSpec;
    }

    /* package */ OptionSpec<Void> getVerboseSpec()
    {
        return verboseSpec;
    }

    /* package */ OptionSpec<Void> getDaemonSpec()
    {
        return daemonSpec;
    }

    /* package */ OptionSpec<Integer> getJMXPortSpec()
    {
        return jmxPortSpec;
    }

    /* package */ OptionSpec<String> getHostSpec()
    {
        return hostSpec;
    }

    /* package */ OptionSpec<Integer> getPortSpec()
    {
        return portSpec;
    }

}
