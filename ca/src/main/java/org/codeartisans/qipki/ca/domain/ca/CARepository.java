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
package org.codeartisans.qipki.ca.domain.ca;

import org.codeartisans.qipki.ca.domain.ca.root.RootCAEntity;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.query.Query;
import org.qi4j.api.query.QueryBuilder;
import org.qi4j.api.query.QueryBuilderFactory;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWork;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

@Mixins( CARepository.Mixin.class )
public interface CARepository
        extends ServiceComposite
{

    RootCAEntity findByIdentity( String identity );

    Query<RootCAEntity> findAllPaginated( int firstResult, int maxResults );

    abstract class Mixin
            implements CARepository
    {

        @Structure
        private UnitOfWorkFactory uowf;
        @Structure
        private QueryBuilderFactory qbf;

        @Override
        public RootCAEntity findByIdentity( String identity )
        {
            return uowf.currentUnitOfWork().get( RootCAEntity.class, identity );
        }

        @Override
        public Query<RootCAEntity> findAllPaginated( int firstResult, int maxResults )
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            QueryBuilder<RootCAEntity> builder = qbf.newQueryBuilder( RootCAEntity.class );
            Query<RootCAEntity> query = builder.newQuery( uow ).
                    firstResult( firstResult ).
                    maxResults( maxResults );
            return query;
        }

    }

}
