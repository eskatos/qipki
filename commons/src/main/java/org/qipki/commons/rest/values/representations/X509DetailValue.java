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
package org.qipki.commons.rest.values.representations;

import org.qi4j.api.common.Optional;
import org.qi4j.api.property.Property;
import org.qipki.commons.crypto.values.x509.ConstraintsExtensionsValue;
import org.qipki.commons.crypto.values.x509.KeysExtensionsValue;
import org.qipki.commons.crypto.values.x509.NamesExtensionsValue;
import org.qipki.commons.crypto.values.x509.PoliciesExtensionsValue;

public interface X509DetailValue
    extends X509Value
{

    Property<Integer> certificateVersion();

    Property<String> signatureAlgorithm();

    Property<String> publicKeyAlgorithm();

    Property<Integer> publicKeySize();

    @Optional
    Property<String> netscapeCertComment();

    @Optional
    Property<KeysExtensionsValue> keysExtensions();

    @Optional
    Property<PoliciesExtensionsValue> policiesExtensions();

    @Optional
    Property<NamesExtensionsValue> namesExtensions();

    @Optional
    Property<ConstraintsExtensionsValue> constraintsExtensions();

}
