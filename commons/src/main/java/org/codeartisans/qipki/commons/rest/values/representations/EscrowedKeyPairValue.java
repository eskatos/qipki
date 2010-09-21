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
package org.codeartisans.qipki.commons.rest.values.representations;

import java.util.List;

import org.codeartisans.qipki.commons.crypto.states.EscrowedKeyPairState;

import org.qi4j.api.common.UseDefaults;
import org.qi4j.api.property.Property;
import org.qi4j.api.value.ValueComposite;

public interface EscrowedKeyPairValue
        extends RestValue, EscrowedKeyPairState, ValueComposite
{

    Property<String> recoveryUri();

    @UseDefaults
    Property<List<String>> x509sUris();

}
