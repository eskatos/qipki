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
import org.codeartisans.qipki.commons.constants.KeyUsage;
import org.codeartisans.qipki.commons.constants.RevocationReason;
import org.codeartisans.qipki.commons.fragments.HasCriticality;
import org.codeartisans.qipki.commons.values.ValidityIntervalValue;
import org.qi4j.api.common.Optional;
import org.qi4j.api.common.UseDefaults;
import org.qi4j.api.property.Property;
import org.qi4j.api.value.ValueComposite;

public interface KeysExtensionsValue
        extends ValueComposite
{

    @UseDefaults
    Property<KeyUsagesValue> keyUsages();

    @Optional
    Property<SubjectKeyIdentifierValue> subjectKeyIdentifier();

    @Optional
    Property<AuthorityKeyIdentifierValue> authorityKeyIdentifier();

    @Optional
    Property<PrivateKeyUsageIntervalValue> privateKeyUsageInterval();

    @Optional
    Property<CRLDistributionPointsValue> crlDistributionPoints();

    public interface KeyUsagesValue
            extends HasCriticality, ValueComposite
    {

        @UseDefaults
        Property<Set<KeyUsage>> keyUsages();

    }

    public interface SubjectKeyIdentifierValue
            extends HasCriticality, ValueComposite
    {

        Property<String> keyIdentifier();

    }

    public interface AuthorityKeyIdentifierValue
            extends HasCriticality, ValueComposite
    {

        Property<String> keyIdentifier();

        Property<Long> serialNumber();

        @UseDefaults
        Property<Set<X509GeneralNameValue>> names();

    }

    public interface PrivateKeyUsageIntervalValue
            extends HasCriticality, ValidityIntervalValue
    {
    }

    public interface CRLDistributionPointsValue
            extends HasCriticality, ValueComposite
    {

        @UseDefaults
        Property<Set<String>> endpoints();

        @UseDefaults
        Property<Set<RevocationReason>> reasons();

        @UseDefaults
        Property<Set<X509GeneralNameValue>> issuerNames();

    }

}
