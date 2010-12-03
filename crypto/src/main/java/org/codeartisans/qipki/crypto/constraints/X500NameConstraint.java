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
package org.codeartisans.qipki.crypto.constraints;

import org.bouncycastle.asn1.x509.X509Name;

import org.codeartisans.qipki.crypto.x509.DistinguishedName;

import org.qi4j.api.constraint.Constraint;
import org.qi4j.library.constraints.annotation.Contains;

public class X500NameConstraint
        implements Constraint<Contains, String>
{

    private static final long serialVersionUID = 1L;

    @Override
    @SuppressWarnings( "ResultOfObjectAllocationIgnored" )
    public boolean isValid( Contains annotation, String value )
    {
        try {
            new X509Name( value );
            new DistinguishedName( value );
            return true;
        } catch ( IllegalArgumentException ignored ) {
            return false;
        }
    }

}
