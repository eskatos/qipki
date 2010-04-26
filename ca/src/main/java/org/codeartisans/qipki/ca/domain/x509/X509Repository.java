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
package org.codeartisans.qipki.ca.domain.x509;

import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.query.Query;
import org.qi4j.api.query.QueryBuilder;
import org.qi4j.api.query.QueryBuilderFactory;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

@Mixins( X509Repository.Mixin.class )
public interface X509Repository
        extends ServiceComposite
{

    X509 findByIdentity( String identity );

    Query<X509> findAllPaginated( int firstResult, int maxResults );

    abstract class Mixin
            implements X509Repository
    {

        @Structure
        private UnitOfWorkFactory uowf;
        @Structure
        private QueryBuilderFactory qbf;

        @Override
        public X509 findByIdentity( String identity )
        {
            return uowf.currentUnitOfWork().get( X509.class, identity );
        }

        @Override
        public Query<X509> findAllPaginated( int firstResult, int maxResults )
        {
            QueryBuilder<X509> builder = qbf.newQueryBuilder( X509.class );
            Query<X509> query = builder.newQuery( uowf.currentUnitOfWork() ).
                    firstResult( firstResult ).
                    maxResults( maxResults );
            return query;
        }

    }

}
