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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import javax.sql.DataSource;

/**
 * An object that allows Qi4J connections to be wrapped so that commit() and rollback() are handled by the user transaction.
 *
 * FIXME : proof of concept / to be completed !
 */
public class DataSourceUserTxDelegate
        extends DataSourceWrapperAdapter
{

    public DataSourceUserTxDelegate( DataSource dataSource )
    {
        super( dataSource );
    }

    @Override
    public Connection getConnection()
            throws SQLException
    {
        return new ConnectionUserTxDelegate( ds.getConnection() )
        {
        };
    }

    @Override
    public Connection getConnection( String username, String password )
            throws SQLException
    {
        return new ConnectionUserTxDelegate( ds.getConnection( username, password ) );
    }

    private static class ConnectionUserTxDelegate
            extends ConnectionWrapperAdapter
    {

        public ConnectionUserTxDelegate( Connection connection )
        {
            super( connection );
        }

        @Override
        public void commit()
                throws SQLException
        {
            // the actual commit will be performed by the UserTransaction (probably container...)
        }

        @Override
        public void rollback()
                throws SQLException
        {
            // the actual rollback will be performed by the UserTransaction (probably container...)
        }

        @Override
        public void rollback( Savepoint savepoint )
                throws SQLException
        {
            // the actual rollback will be performed by the UserTransaction (probably container...)
        }

    }

}
