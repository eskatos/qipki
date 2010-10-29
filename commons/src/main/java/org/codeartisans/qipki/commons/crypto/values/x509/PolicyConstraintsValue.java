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
package org.codeartisans.qipki.commons.crypto.values.x509;

import java.util.Set;

import org.codeartisans.qipki.commons.fragments.HasCriticality;

import org.qi4j.api.common.Optional;
import org.qi4j.api.common.UseDefaults;
import org.qi4j.api.property.Property;
import org.qi4j.api.value.ValueComposite;

public interface PolicyConstraintsValue
        extends HasCriticality, ValueComposite
{

    @UseDefaults
    Property<Set<PolicyConstraintValue>> constraints();

    @SuppressWarnings( "PublicInnerClass" )
    public interface PolicyConstraintValue
            extends ValueComposite
    {

        @Optional
        Property<Integer> requireExplicitPolicy();

        @Optional
        Property<Integer> inhibitPolicyMapping();

    }

}
