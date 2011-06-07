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
import java.util.Arrays;

import joptsimple.OptionParser;
import joptsimple.OptionSpec;
import joptsimple.ValueConverter;

import org.qi4j.api.structure.Application.Mode;

/* package */ class QiPkiHttpCaOptionParser
        extends OptionParser
{

    /* package */ static interface Options
    {

        String HELP = "help";
        String VERBOSE = "verbose";
        String MODE = "mode";
        String CONFIGURATION_DIR = "configuration";
        String DATA_DIR = "data";
        String TEMPORARY_DIR = "temporary";
        String CACHE_DIR = "cache";
        String LOG_DIR = "log";
        String JMX_PORT = "jmx-port";
        String HOST = "host";
        String PORT = "port";
    }

    private final OptionSpec<Void> helpSpec;
    private final OptionSpec<Void> verboseSpec;
    private final OptionSpec<Mode> modeSpec;
    private final OptionSpec<File> configurationDirSpec;
    private final OptionSpec<File> dataDirSpec;
    private final OptionSpec<File> temporaryDirSpec;
    private final OptionSpec<File> cacheDirSpec;
    private final OptionSpec<File> logDirSpec;
    private final OptionSpec<Integer> jmxPortSpec;
    private final OptionSpec<String> hostSpec;
    private final OptionSpec<Integer> portSpec;


    /* package */ QiPkiHttpCaOptionParser()
    {
        super();

        ValueConverter<Integer> portConverter = new PortValueConverter();
        ValueConverter<File> fileConverter = new FileValueConverter();

        helpSpec = accepts( Options.HELP, "Show help" );
        verboseSpec = accepts( Options.VERBOSE, "Turn on verbose mode" );
        modeSpec = accepts( Options.MODE, "Application mode" ).
                withRequiredArg().
                ofType( Mode.class ).
                describedAs( Arrays.toString( Mode.values() ) ).
                defaultsTo( Mode.production );
        configurationDirSpec = accepts( Options.CONFIGURATION_DIR, "Base configuration directory" ).
                withRequiredArg().
                ofType( File.class ).
                withValuesConvertedBy( fileConverter );
        dataDirSpec = accepts( Options.DATA_DIR, "Base data directory" ).
                withRequiredArg().
                ofType( File.class ).
                withValuesConvertedBy( fileConverter ).
                describedAs( "Data basedir" );
        temporaryDirSpec = accepts( Options.TEMPORARY_DIR, "Base temporary directory" ).
                withRequiredArg().
                ofType( File.class ).
                withValuesConvertedBy( fileConverter );
        cacheDirSpec = accepts( Options.CACHE_DIR, "Base cache directory" ).
                withRequiredArg().
                ofType( File.class ).
                withValuesConvertedBy( fileConverter );
        logDirSpec = accepts( Options.LOG_DIR, "Base log directory" ).
                withRequiredArg().
                ofType( File.class ).
                withValuesConvertedBy( fileConverter );
        jmxPortSpec = accepts( Options.JMX_PORT, "Fix remote JMX port" ).
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

    /* package */ OptionSpec<Mode> getModeSpec()
    {
        return modeSpec;
    }

    /* package */ OptionSpec<File> getConfigurationDirSpec()
    {
        return configurationDirSpec;
    }

    /* package */ OptionSpec<File> getDataDirSpec()
    {
        return dataDirSpec;
    }

    /* package */ OptionSpec<File> getTemporaryDirSpec()
    {
        return temporaryDirSpec;
    }

    /* package */ OptionSpec<File> getCacheDirSpec()
    {
        return cacheDirSpec;
    }

    /* package */ OptionSpec<File> getLogDirSpec()
    {
        return logDirSpec;
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
