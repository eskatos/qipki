/*
 * Created on 20 sept. 2010
 *
 * Licenced under the Netheos Licence, Version 1.0 (the "Licence"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at :
 *
 * http://www.netheos.net/licences/LICENCE-1.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright (c) Netheos
 */
package org.codeartisans.qipki.ca.application.contexts.escrowedkeypair;

import org.codeartisans.qipki.ca.domain.escrowedkeypair.EscrowedKeyPair;
import org.codeartisans.qipki.ca.domain.escrowedkeypair.EscrowedKeyPairFactory;
import org.codeartisans.qipki.ca.domain.escrowedkeypair.EscrowedKeyPairRepository;
import org.codeartisans.qipki.core.dci.Context;
import org.codeartisans.qipki.crypto.algorithms.AsymetricAlgorithm;

import org.qi4j.api.query.Query;

public class EscrowedKeyPairListContext
        extends Context
{

    public Query<EscrowedKeyPair> list( int start )
    {
        return context.role( EscrowedKeyPairRepository.class ).findAllPaginated( start, 25 );
    }

    public EscrowedKeyPair createEscrowedKeyPair( AsymetricAlgorithm algorithm, Integer length )
    {
        return context.role( EscrowedKeyPairFactory.class ).create( algorithm, length );
    }

}
