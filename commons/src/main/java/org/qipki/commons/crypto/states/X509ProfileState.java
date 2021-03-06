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
package org.qipki.commons.crypto.states;

import org.qi4j.api.common.Optional;
import org.qi4j.api.entity.Queryable;
import org.qi4j.api.property.Property;
import org.qipki.commons.crypto.values.x509.BasicConstraintsValue;
import org.qipki.commons.crypto.values.x509.ExtendedKeyUsagesValue;
import org.qipki.commons.crypto.values.x509.KeyUsagesValue;
import org.qipki.commons.crypto.values.x509.NameConstraintsValue;
import org.qipki.commons.crypto.values.x509.NetscapeCertTypesValue;
import org.qipki.commons.fragments.HasName;

public interface X509ProfileState
    extends HasName
{

    @Queryable( false )
    Property<Integer> validityDays();

    @Optional
    @Queryable( false )
    Property<String> netscapeCertComment();

    @Optional
    @Queryable( false )
    Property<KeyUsagesValue> keyUsages();

    @Optional
    @Queryable( false )
    Property<ExtendedKeyUsagesValue> extendedKeyUsages();

    @Optional
    @Queryable( false )
    Property<NetscapeCertTypesValue> netscapeCertTypes();

    @Optional
    @Queryable( false )
    Property<BasicConstraintsValue> basicConstraints();

    @Optional
    @Queryable( false )
    Property<NameConstraintsValue> nameConstraints();

}
