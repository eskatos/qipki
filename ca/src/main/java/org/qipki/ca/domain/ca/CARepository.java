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
package org.qipki.ca.domain.ca;

import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.query.Query;
import org.qi4j.api.query.QueryBuilder;
import org.qi4j.api.query.QueryBuilderFactory;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;
import org.qipki.core.services.AbstractBoxedDomainRepository;
import org.qipki.core.services.BoxedDomainRepository;

import static org.qi4j.api.query.QueryExpressions.*;

@Mixins( CARepository.Mixin.class )
@SuppressWarnings( "PublicInnerClass" )
public interface CARepository
    extends BoxedDomainRepository<CA>, ServiceComposite
{

    Query<CA> findByNamePaginated( String name, int firstResult, int maxResults );

    abstract class Mixin
        extends AbstractBoxedDomainRepository<CA>
        implements CARepository
    {

        public Mixin( @Structure UnitOfWorkFactory uowf, @Structure QueryBuilderFactory qbf )
        {
            super( uowf, qbf );
        }

        @Override
        public Query<CA> findByNamePaginated( String name, int firstResult, int maxResults )
        {
            QueryBuilder<CA> builder = qbf.newQueryBuilder( getBoxedClass() );
            builder = builder.where( eq( templateFor( CA.class ).name(), name ) );
            Query<CA> query = uowf.currentUnitOfWork().newQuery( builder ).
                firstResult( firstResult ).
                maxResults( maxResults );
            return query;
        }

    }

}
