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

import org.qi4j.api.structure.Application.Mode;
import org.qi4j.library.fileconfig.FileConfigurationOverride;

public class QiPkiApplicationArguments
{

    private boolean verbose;
    private Mode mode;
    private File configuration;
    private File data;
    private File temporary;
    private File cache;
    private File log;
    private Integer jmxPort;
    private String host;
    private Integer port;

    public FileConfigurationOverride buildFileConfigOverride()
    {
        return new FileConfigurationOverride().withConfiguration( configuration ).
                withData( data ).
                withTemporary( temporary ).
                withCache( cache ).
                withLog( log );
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder( "RunArgs{" );
        sb.append( "\n\t" ).append( "verbose: " ).append( verbose );
        sb.append( "\n\t" ).append( "mode: " ).append( mode );
        sb.append( "\n\t" ).append( "configuration: " ).append( configuration );
        sb.append( "\n\t" ).append( "data: " ).append( data );
        sb.append( "\n\t" ).append( "temporary: " ).append( temporary );
        sb.append( "\n\t" ).append( "cache: " ).append( cache );
        sb.append( "\n\t" ).append( "log: " ).append( log );
        sb.append( "\n\t" ).append( "jmx-port: " ).append( jmxPort );
        sb.append( "\n\t" ).append( "http-host: " ).append( host );
        sb.append( "\n\t" ).append( "http-port: " ).append( port );
        return sb.toString();
    }

    public void setCache( File cache )
    {
        this.cache = cache;
    }

    public void setConfiguration( File configuration )
    {
        this.configuration = configuration;
    }

    public void setData( File data )
    {
        this.data = data;
    }

    public void setHost( String host )
    {
        this.host = host;
    }

    public void setJmxPort( Integer jmxPort )
    {
        this.jmxPort = jmxPort;
    }

    public void setLog( File log )
    {
        this.log = log;
    }

    public void setMode( Mode mode )
    {
        this.mode = mode;
    }

    public void setPort( Integer port )
    {
        this.port = port;
    }

    public void setTemporary( File temporary )
    {
        this.temporary = temporary;
    }

    public void setVerbose( boolean verbose )
    {
        this.verbose = verbose;
    }

    public File getCache()
    {
        return cache;
    }

    public File getConfiguration()
    {
        return configuration;
    }

    public File getData()
    {
        return data;
    }

    public String getHost()
    {
        return host;
    }

    public Integer getJmxPort()
    {
        return jmxPort;
    }

    public File getLog()
    {
        return log;
    }

    public Mode getMode()
    {
        return mode;
    }

    public Integer getPort()
    {
        return port;
    }

    public File getTemporary()
    {
        return temporary;
    }

    public boolean isVerbose()
    {
        return verbose;
    }

}
