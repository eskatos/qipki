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
package org.codeartisans.qipki.commons;

import org.codeartisans.qipki.commons.values.crypto.CryptoValuesFactory;
import org.codeartisans.qipki.commons.values.crypto.KeyPairSpecValue;
import org.codeartisans.qipki.commons.values.crypto.ValidityIntervalValue;
import org.codeartisans.qipki.commons.values.crypto.x509.ConstraintsExtensionsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.KeysExtensionsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.NamesExtensionsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.PoliciesExtensionsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.X509GeneralNameValue;
import org.codeartisans.qipki.commons.values.crypto.x509.X509GeneralSubtreeValue;
import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;

public class QiPkiCryptoValuesAssembler
        implements Assembler
{

    private final Visibility visibility;

    public QiPkiCryptoValuesAssembler()
    {
        this( Visibility.module );
    }

    public QiPkiCryptoValuesAssembler( Visibility visibility )
    {
        this.visibility = visibility;
    }

    @Override
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        module.addValues( KeyPairSpecValue.class,
                          ValidityIntervalValue.class,
                          X509GeneralNameValue.class,
                          X509GeneralSubtreeValue.class,
                          KeysExtensionsValue.class,
                          KeysExtensionsValue.KeyUsagesValue.class,
                          KeysExtensionsValue.SubjectKeyIdentifierValue.class,
                          KeysExtensionsValue.AuthorityKeyIdentifierValue.class,
                          KeysExtensionsValue.PrivateKeyUsageIntervalValue.class,
                          KeysExtensionsValue.CRLDistributionPointsValue.class,
                          PoliciesExtensionsValue.class,
                          PoliciesExtensionsValue.CertificatePoliciesValue.class,
                          PoliciesExtensionsValue.PolicyInformationValue.class,
                          PoliciesExtensionsValue.PolicyQualifierInfoValue.class,
                          PoliciesExtensionsValue.PolicyMappingsValue.class,
                          PoliciesExtensionsValue.PolicyMappingValue.class,
                          NamesExtensionsValue.class,
                          NamesExtensionsValue.AlternativeNamesValue.class,
                          ConstraintsExtensionsValue.class,
                          ConstraintsExtensionsValue.BasicConstraintsValue.class,
                          ConstraintsExtensionsValue.PolicyConstraintsValue.class,
                          ConstraintsExtensionsValue.PolicyConstraintValue.class,
                          ConstraintsExtensionsValue.NameConstraintsValue.class ).
                visibleIn( visibility );
        module.addServices( CryptoValuesFactory.class ).
                visibleIn( visibility );
    }

}
