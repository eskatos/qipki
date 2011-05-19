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
package org.qipki.ca.domain.revocation;

import org.qipki.ca.domain.x509.X509;
import org.qipki.crypto.x509.RevocationReason;

import org.qi4j.api.entity.Identity;
import org.qi4j.api.entity.Queryable;
import org.qi4j.api.entity.association.Association;
import org.qi4j.api.property.Property;

public interface RevocationState
        extends Identity
{

    Association<X509> x509();

    @Queryable( false )
    Property<RevocationReason> reason();

}
