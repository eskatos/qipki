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
package org.qipki.ca.application.contexts.escrowedkeypair;

import org.codeartisans.java.toolbox.Collections;
import org.qi4j.api.unitofwork.concern.UnitOfWorkRetry;
import org.qipki.ca.domain.escrowedkeypair.EscrowedKeyPair;
import org.qipki.core.dci.Context;

public class EscrowedKeyPairContext
    extends Context
{

    public EscrowedKeyPair escrowedKeyPair()
    {
        return context.role( EscrowedKeyPair.class );
    }

    @UnitOfWorkRetry
    public void delete()
    {
        EscrowedKeyPair ekp = context.role( EscrowedKeyPair.class );
        if( Collections.firstElementOrNull( ekp.x509s() ) != null )
        {
            // TODO Throw list of X509 identities and list text in exception so the UI could present it to the user
            throw new IllegalStateException( "EscrowedKeyPair cannot be deleted as it has associated X509s" );
        }
        module.currentUnitOfWork().remove( ekp );
    }

}
