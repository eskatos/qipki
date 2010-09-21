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
package org.codeartisans.qipki.ca.http.presentation.rest.resources.escrowedkeypair;

import org.codeartisans.qipki.ca.application.contexts.escrowedkeypair.EscrowedKeyPairContext;
import org.codeartisans.qipki.ca.domain.escrowedkeypair.EscrowedKeyPair;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.AbstractResource;

import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.restlet.data.MediaType;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;

public class EscrowedKeyPairPemResource
        extends AbstractResource
{

    public EscrowedKeyPairPemResource( @Structure ObjectBuilderFactory obf )
    {
        super( obf );
    }

    @Override
    protected Representation get()
            throws ResourceException
    {
        // Data
        String identity = ensureRequestAttribute( PARAM_IDENTITY, String.class, Status.CLIENT_ERROR_BAD_REQUEST );

        // Context
        EscrowedKeyPairContext ekpCtx = newRootContext().escrowedKeyPairContext( identity );

        // Interaction
        EscrowedKeyPair ekp = ekpCtx.escrowedKeyPair();

        // Representation
        return new StringRepresentation( ekp.pem().get(), MediaType.TEXT_PLAIN );
    }

}
