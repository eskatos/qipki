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
