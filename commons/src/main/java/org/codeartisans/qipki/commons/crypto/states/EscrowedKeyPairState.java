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
package org.codeartisans.qipki.commons.crypto.states;

import org.codeartisans.qipki.crypto.algorithms.AsymetricAlgorithm;

import org.qi4j.api.property.Property;

public interface EscrowedKeyPairState
{

    Property<AsymetricAlgorithm> algorithm();

    Property<Integer> length();

}