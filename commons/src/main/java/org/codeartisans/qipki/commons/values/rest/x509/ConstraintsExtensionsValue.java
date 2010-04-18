/*
 * Copyright (c) 2010 Paul Merlin <paul@nosphere.org>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.codeartisans.qipki.commons.values.rest.x509;

import java.util.Set;
import org.codeartisans.qipki.commons.fragments.HasCriticality;
import org.qi4j.api.common.Optional;
import org.qi4j.api.common.UseDefaults;
import org.qi4j.api.property.Property;
import org.qi4j.api.value.ValueComposite;
import org.qi4j.library.constraints.annotation.GreaterThan;

public interface ConstraintsExtensionsValue
        extends ValueComposite
{

    @Optional
    Property<BasicConstraintsValue> basicConstraints();

    @Optional
    Property<NameConstraintsValue> nameConstraints();

    @Optional
    Property<PolicyConstraintsValue> policyConstraints();

    public interface BasicConstraintsValue
            extends HasCriticality, ValueComposite
    {

        @UseDefaults
        Property<Boolean> subjectIsCA();

        @Optional
        @GreaterThan( 0L )
        Property<Long> pathLenConstraint();

    }

    public interface NameConstraintsValue
            extends HasCriticality, ValueComposite
    {

        @UseDefaults
        Property<Set<X509GeneralSubtreeValue>> permittedSubtrees();

        @UseDefaults
        Property<Set<X509GeneralSubtreeValue>> excludedSubtrees();

    }

    public interface PolicyConstraintsValue
            extends HasCriticality, ValueComposite
    {

        @UseDefaults
        Property<Set<PolicyConstraintValue>> constraints();

    }

    public interface PolicyConstraintValue
            extends ValueComposite
    {

        @Optional
        Property<Integer> requireExplicitPolicy();

        @Optional
        Property<Integer> inhibitPolicyMapping();

    }

}
