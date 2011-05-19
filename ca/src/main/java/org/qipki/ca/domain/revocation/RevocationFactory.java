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
package org.qipki.ca.domain.revocation;

import org.qipki.ca.domain.x509.X509;
import org.qipki.crypto.x509.RevocationReason;

import org.qi4j.api.entity.EntityBuilder;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

@Mixins( RevocationFactory.Mixin.class )
public interface RevocationFactory
        extends ServiceComposite
{

    Revocation create( X509 x509, RevocationReason reason );

    abstract class Mixin
            implements RevocationFactory
    {

        @Structure
        private UnitOfWorkFactory uowf;

        @Override
        public Revocation create( X509 x509, RevocationReason reason )
        {
            EntityBuilder<Revocation> builder = uowf.currentUnitOfWork().newEntityBuilder( Revocation.class );
            Revocation revocation = builder.instance();
            revocation.x509().set( x509 );
            revocation.reason().set( reason );
            return builder.newInstance();
        }

    }

}
