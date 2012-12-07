/*
 * Copyright (c) 2010, Paul Merlin. All Rights Reserved.
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
package org.qipki.core.sql;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 * A simple DataSource wrapper (for overriding)
 */
public class DataSourceWrapperAdapter
    implements DataSource
{

    protected final DataSource ds;

    public DataSourceWrapperAdapter( DataSource dataSource )
    {
        this.ds = dataSource;
    }

    @Override
    public Connection getConnection()
        throws SQLException
    {
        return ds.getConnection();
    }

    @Override
    public Connection getConnection( String username, String password )
        throws SQLException
    {
        return ds.getConnection( username, password );
    }

    @Override
    public PrintWriter getLogWriter()
        throws SQLException
    {
        return ds.getLogWriter();
    }

    @Override
    public void setLogWriter( PrintWriter out )
        throws SQLException
    {
        ds.setLogWriter( out );
    }

    @Override
    public void setLoginTimeout( int seconds )
        throws SQLException
    {
        ds.setLoginTimeout( seconds );
    }

    @Override
    public int getLoginTimeout()
        throws SQLException
    {
        return ds.getLoginTimeout();
    }

    @Override
    public <T> T unwrap( Class<T> iface )
        throws SQLException
    {
        return ds.unwrap( iface );
    }

    @Override
    public boolean isWrapperFor( Class<?> iface )
        throws SQLException
    {
        return ds.isWrapperFor( iface );
    }

    public Logger getParentLogger()
        throws SQLFeatureNotSupportedException
    {
        try
        {
            Method method = ds.getClass().getMethod( "getParentLogger" );
            System.err.println( "WARN Using reflection in " + getClass().getName()
                                + " to support new DataSource methods added in Java7." );
            return (Logger) method.invoke( ds );
        }
        catch( Exception ex )
        {
            throw new SQLFeatureNotSupportedException( "Wrapped DataSource do not support new methods added in Java7", ex );
        }
    }

}
