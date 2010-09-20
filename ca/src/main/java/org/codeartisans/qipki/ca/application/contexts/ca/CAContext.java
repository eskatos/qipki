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
package org.codeartisans.qipki.ca.application.contexts.ca;

import java.util.Iterator;
import java.util.Map;

import org.codeartisans.qipki.ca.domain.ca.CA;
import org.codeartisans.qipki.ca.domain.ca.profileassignment.X509ProfileAssignment;
import org.codeartisans.qipki.ca.domain.ca.profileassignment.X509ProfileAssignmentFactory;
import org.codeartisans.qipki.ca.domain.x509profile.X509ProfileRepository;
import org.codeartisans.qipki.commons.crypto.states.KeyEscrowPolicy;
import org.codeartisans.qipki.core.dci.Context;

import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.unitofwork.UnitOfWork;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

public class CAContext
        extends Context
{

    @Structure
    private UnitOfWorkFactory uowf;

    public CA ca()
    {
        return context.role( CA.class );
    }

    public CA updateCA( Map<String, KeyEscrowPolicy> profileAssignments )
    {
        UnitOfWork uow = uowf.currentUnitOfWork();
        CA ca = context.role( CA.class );
        // Remove existing assignments
        Iterator<X509ProfileAssignment> it = ca.allowedX509Profiles().iterator();
        while ( it.hasNext() ) {
            X509ProfileAssignment eachCurrentAssignment = it.next();
            it.remove();
            uow.remove( eachCurrentAssignment );
        }
        // Create and add submitted assignments
        X509ProfileAssignmentFactory profileAssignmentFactory = context.role( X509ProfileAssignmentFactory.class );
        X509ProfileRepository profileRepository = context.role( X509ProfileRepository.class );
        for ( Map.Entry<String, KeyEscrowPolicy> eachEntry : profileAssignments.entrySet() ) {
            ca.allowedX509Profiles().add( profileAssignmentFactory.create( eachEntry.getValue(), profileRepository.findByIdentity( eachEntry.getKey() ) ) );
        }
        return ca;
    }

}
