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
package org.qipki.ca.domain.escrowedkeypair;

import org.codeartisans.java.toolbox.CollectionUtils;
import org.qipki.ca.domain.x509.X509Repository;
import org.qipki.core.services.AbstractBoxedDomainRepository;
import org.qipki.core.services.BoxedDomainRepository;

import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.query.Query;
import org.qi4j.api.query.QueryBuilder;
import org.qi4j.api.query.QueryBuilderFactory;
import static org.qi4j.api.query.QueryExpressions.*;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

@Mixins( EscrowedKeyPairRepository.Mixin.class )
public interface EscrowedKeyPairRepository
        extends BoxedDomainRepository<EscrowedKeyPair>, ServiceComposite
{

    EscrowedKeyPair findByX509Identity( String x509Identity );

    @SuppressWarnings( "PublicInnerClass" )
    abstract class Mixin
            extends AbstractBoxedDomainRepository<EscrowedKeyPair>
            implements EscrowedKeyPairRepository
    {

        @Service
        private X509Repository x509Repository;

        public Mixin( @Structure UnitOfWorkFactory uowf, @Structure QueryBuilderFactory qbf )
        {
            super( uowf, qbf );
        }

        @Override
        public EscrowedKeyPair findByX509Identity( String x509Identity )
        {
            QueryBuilder<EscrowedKeyPair> builder = qbf.newQueryBuilder( EscrowedKeyPair.class );
            EscrowedKeyPair ekpTemplate = templateFor( EscrowedKeyPair.class );
            builder = builder.where( contains( ekpTemplate.x509s(), x509Repository.findByIdentity( x509Identity ) ) );
            Query<EscrowedKeyPair> query = builder.newQuery( uowf.currentUnitOfWork() );
            assert query.count() <= 1;
            return CollectionUtils.firstElementOrNull( query );
        }

    }

}
