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
package org.qipki.commons.rest.values.params;

import java.util.List;
import org.qi4j.api.common.Optional;
import org.qi4j.api.common.UseDefaults;
import org.qi4j.api.property.Property;
import org.qi4j.api.value.ValueComposite;
import org.qipki.commons.crypto.values.KeyPairSpecValue;
import org.qipki.commons.fragments.HasName;
import org.qipki.crypto.constraints.X500Name;

public interface CAFactoryParamsValue
    extends HasName, ValueComposite
{

    Property<String> cryptoStoreUri();

    Property<Integer> validityDays();

    @UseDefaults
    Property<List<String>> crlDistPoints();

    @X500Name
    Property<String> distinguishedName();

    Property<KeyPairSpecValue> keySpec();

    @Optional
    Property<String> parentCaUri();

}
