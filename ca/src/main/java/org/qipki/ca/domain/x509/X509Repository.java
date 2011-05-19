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
package org.qipki.ca.domain.x509;

import org.codeartisans.java.toolbox.CollectionUtils;
import org.qipki.core.services.AbstractBoxedDomainRepository;
import org.qipki.core.services.BoxedDomainRepository;

import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.query.Query;
import org.qi4j.api.query.QueryBuilder;
import org.qi4j.api.query.QueryBuilderFactory;
import static org.qi4j.api.query.QueryExpressions.*;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

@Mixins( X509Repository.Mixin.class )
public interface X509Repository
        extends BoxedDomainRepository<X509>, ServiceComposite
{

    X509 findByHexSha256( String hexSha256 );

    @SuppressWarnings( "PublicInnerClass" )
    abstract class Mixin
            extends AbstractBoxedDomainRepository<X509>
            implements X509Repository
    {

        public Mixin( @Structure UnitOfWorkFactory uowf, @Structure QueryBuilderFactory qbf )
        {
            super( uowf, qbf );
        }

        @Override
        public X509 findByHexSha256( String hexSha256 )
        {
            QueryBuilder<X509> builder = qbf.newQueryBuilder( X509.class );
            builder = builder.where( eq( templateFor( X509.class ).sha256Fingerprint(), hexSha256 ) );
            Query<X509> query = builder.newQuery( uowf.currentUnitOfWork() );
            assert query.count() <= 1;
            return CollectionUtils.firstElementOrNull( query );
        }

    }

}
