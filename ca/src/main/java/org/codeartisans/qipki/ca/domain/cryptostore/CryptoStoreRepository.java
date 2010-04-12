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
package org.codeartisans.qipki.ca.domain.cryptostore;

import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.query.Query;
import org.qi4j.api.query.QueryBuilder;
import org.qi4j.api.query.QueryBuilderFactory;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWork;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

@Mixins( CryptoStoreRepository.Mixin.class )
public interface CryptoStoreRepository
        extends ServiceComposite
{

    CryptoStoreEntity findByIdentity( String identity );

    Query<CryptoStoreEntity> findAllPaginated( int firstResult, int maxResults );

    abstract class Mixin
            implements CryptoStoreRepository
    {

        @Structure
        private UnitOfWorkFactory uowf;
        @Structure
        private QueryBuilderFactory qbf;

        @Override
        public CryptoStoreEntity findByIdentity( String identity )
        {
            return uowf.currentUnitOfWork().get( CryptoStoreEntity.class, identity );
        }

        @Override
        public Query<CryptoStoreEntity> findAllPaginated( int firstResult, int maxResults )
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            QueryBuilder<CryptoStoreEntity> builder = qbf.newQueryBuilder( CryptoStoreEntity.class );
            Query<CryptoStoreEntity> query = builder.newQuery( uow ).
                    firstResult( firstResult ).
                    maxResults( maxResults );
            return query;
        }

    }

}
