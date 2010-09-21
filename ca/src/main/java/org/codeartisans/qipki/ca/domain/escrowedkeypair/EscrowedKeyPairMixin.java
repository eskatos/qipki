/*
 * Created on 21 sept. 2010
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
package org.codeartisans.qipki.ca.domain.escrowedkeypair;

import java.io.StringReader;
import java.security.KeyPair;

import org.codeartisans.qipki.crypto.io.CryptIO;

import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.This;

public abstract class EscrowedKeyPairMixin
        implements EscrowedKeyPair
{

    @This
    private EscrowedKeyPair state;
    @Service
    private CryptIO cryptio;

    @Override
    public KeyPair keyPair()
    {
        return cryptio.readKeyPairPEM( new StringReader( state.pem().get() ) );
    }

}
