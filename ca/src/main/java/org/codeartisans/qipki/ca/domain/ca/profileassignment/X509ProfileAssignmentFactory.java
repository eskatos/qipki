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
package org.codeartisans.qipki.ca.domain.ca.profileassignment;

import org.codeartisans.qipki.ca.domain.x509profile.X509Profile;
import org.codeartisans.qipki.commons.crypto.states.KeyEscrowPolicy;

import org.qi4j.api.entity.EntityBuilder;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

@Mixins( X509ProfileAssignmentFactory.Mixin.class )
@SuppressWarnings( "PublicInnerClass" )
public interface X509ProfileAssignmentFactory
        extends ServiceComposite
{

    X509ProfileAssignment create( KeyEscrowPolicy keyEscrowPolicy, X509Profile profile );

    abstract class Mixin
            implements X509ProfileAssignmentFactory
    {

        @Structure
        private UnitOfWorkFactory uowf;

        @Override
        public X509ProfileAssignment create( KeyEscrowPolicy keyEscrowPolicy, X509Profile profile )
        {
            EntityBuilder<X509ProfileAssignment> profileAssignmentBuilder = uowf.currentUnitOfWork().newEntityBuilder( X509ProfileAssignment.class );
            X509ProfileAssignment profileAssignment = profileAssignmentBuilder.instance();
            profileAssignment.keyEscrowPolicy().set( keyEscrowPolicy );
            profileAssignment.x509Profile().set( profile );
            return profileAssignmentBuilder.newInstance();
        }

    }

}
