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

import java.io.File;
import java.util.Arrays;

import joptsimple.OptionParser;
import joptsimple.OptionSpec;
import joptsimple.ValueConverter;

import org.qi4j.api.structure.Application.Mode;

import static org.qipki.main.core.DefaultFileConfigOptions.*;
import static org.qipki.main.core.QiPkiOptions.*;

public class QiPkiOptionParser
        extends OptionParser
{

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

    public QiPkiOptionParser()
    {
        super();

        ValueConverter<Integer> portConverter = new PortValueConverter();
        ValueConverter<File> fileConverter = new FileValueConverter();

        helpSpec = accepts( HELP, "Show help" );
        verboseSpec = accepts( VERBOSE, "Turn on verbose mode" );
        modeSpec = accepts( MODE, "Application mode" ).
                withRequiredArg().
                ofType( Mode.class ).
                describedAs( Arrays.toString( Mode.values() ) ).
                defaultsTo( Mode.production );
        configurationDirSpec = accepts( CONFIGURATION_DIR, "Base configuration directory" ).
                withRequiredArg().
                ofType( File.class ).
                withValuesConvertedBy( fileConverter ).
                describedAs( "Defaults to $INSTALL_DIR/" + DEFAULT_CONFIGURATION_DIR );
        dataDirSpec = accepts( DATA_DIR, "Base data directory" ).
                withRequiredArg().
                ofType( File.class ).
                withValuesConvertedBy( fileConverter ).
                describedAs( "Defaults to $INSTALL_DIR/" + DEFAULT_DATA_DIR );
        temporaryDirSpec = accepts( TEMPORARY_DIR, "Base temporary directory" ).
                withRequiredArg().
                ofType( File.class ).
                withValuesConvertedBy( fileConverter ).
                describedAs( "Defaults to $INSTALL_DIR/" + DEFAULT_TEMPORARY_DIR );
        cacheDirSpec = accepts( CACHE_DIR, "Base cache directory" ).
                withRequiredArg().
                ofType( File.class ).
                withValuesConvertedBy( fileConverter ).
                describedAs( "Defaults to $INSTALL_DIR/" + DEFAULT_CACHE_DIR );
        logDirSpec = accepts( LOG_DIR, "Base log directory" ).
                withRequiredArg().
                ofType( File.class ).
                withValuesConvertedBy( fileConverter ).
                describedAs( "Defaults to $INSTALL_DIR/" + DEFAULT_LOG_DIR );
        jmxPortSpec = accepts( JMX_PORT, "Fix remote JMX port" ).
                withRequiredArg().
                ofType( Integer.class ).
                withValuesConvertedBy( portConverter ).
                describedAs( "JMX port" );
        hostSpec = accepts( HOST, "HTTP service hostname or IP address" ).
                withRequiredArg().
                ofType( String.class ).
                describedAs( "host" ).
                defaultsTo( "127.0.0.1" );
        portSpec = accepts( PORT, "HTTP service port number" ).
                withRequiredArg().
                ofType( Integer.class ).
                withValuesConvertedBy( portConverter ).
                describedAs( "port" ).
                defaultsTo( 8443 );
    }

    public OptionSpec<Void> getHelpSpec()
    {
        return helpSpec;
    }

    public OptionSpec<Void> getVerboseSpec()
    {
        return verboseSpec;
    }

    public OptionSpec<Mode> getModeSpec()
    {
        return modeSpec;
    }

    public OptionSpec<File> getConfigurationDirSpec()
    {
        return configurationDirSpec;
    }

    public OptionSpec<File> getDataDirSpec()
    {
        return dataDirSpec;
    }

    public OptionSpec<File> getTemporaryDirSpec()
    {
        return temporaryDirSpec;
    }

    public OptionSpec<File> getCacheDirSpec()
    {
        return cacheDirSpec;
    }

    public OptionSpec<File> getLogDirSpec()
    {
        return logDirSpec;
    }

    public OptionSpec<Integer> getJMXPortSpec()
    {
        return jmxPortSpec;
    }

    public OptionSpec<String> getHostSpec()
    {
        return hostSpec;
    }

    public OptionSpec<Integer> getPortSpec()
    {
        return portSpec;
    }

}
