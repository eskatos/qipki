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
package org.codeartisans.qipki.ca.http.presentation.rest.uribuilder;

import org.restlet.data.Reference;

public final class CaEscrowedKeyPairUriBuilder
        extends AbstractUriBuilder
{
    /* package */ CaEscrowedKeyPairUriBuilder( Reference baseRef, String identity, String special )
    {
        super( baseRef, identity, special );
    }

    public CaEscrowedKeyPairUriBuilder withIdentity( String identity )
    {
        return new CaEscrowedKeyPairUriBuilder( baseRef, identity, special );
    }

    public CaEscrowedKeyPairUriBuilder pem()
    {
        return new CaEscrowedKeyPairUriBuilder( baseRef, identity, "pem" );
    }

}
