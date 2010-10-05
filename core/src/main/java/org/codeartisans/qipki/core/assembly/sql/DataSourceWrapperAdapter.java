/*
 * Copyright (c) 2010 Paul Merlin <paul@nosphere.org>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.codeartisans.qipki.core.assembly.sql;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * A simple connection wrapper (for overriding)
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

}
