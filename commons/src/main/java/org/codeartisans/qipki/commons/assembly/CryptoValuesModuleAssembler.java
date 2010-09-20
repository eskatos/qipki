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
package org.codeartisans.qipki.commons.assembly;

import org.codeartisans.qipki.commons.crypto.services.CryptoValuesFactory;
import org.codeartisans.qipki.commons.crypto.values.KeyPairSpecValue;
import org.codeartisans.qipki.commons.crypto.values.ValidityIntervalValue;
import org.codeartisans.qipki.commons.crypto.services.X509ExtensionsValueFactory;
import org.codeartisans.qipki.commons.crypto.values.x509.AlternativeNamesValue;
import org.codeartisans.qipki.commons.crypto.values.x509.AuthorityKeyIdentifierValue;
import org.codeartisans.qipki.commons.crypto.values.x509.BasicConstraintsValue;
import org.codeartisans.qipki.commons.crypto.values.x509.CRLDistributionPointsValue;
import org.codeartisans.qipki.commons.crypto.values.x509.CertificatePoliciesValue;
import org.codeartisans.qipki.commons.crypto.values.x509.ConstraintsExtensionsValue;
import org.codeartisans.qipki.commons.crypto.values.x509.ExtendedKeyUsagesValue;
import org.codeartisans.qipki.commons.crypto.values.x509.KeyUsagesValue;
import org.codeartisans.qipki.commons.crypto.values.x509.KeysExtensionsValue;
import org.codeartisans.qipki.commons.crypto.values.x509.NameConstraintsValue;
import org.codeartisans.qipki.commons.crypto.values.x509.NamesExtensionsValue;
import org.codeartisans.qipki.commons.crypto.values.x509.NetscapeCertTypesValue;
import org.codeartisans.qipki.commons.crypto.values.x509.PoliciesExtensionsValue;
import org.codeartisans.qipki.commons.crypto.values.x509.PolicyConstraintsValue;
import org.codeartisans.qipki.commons.crypto.values.x509.PolicyMappingsValue;
import org.codeartisans.qipki.commons.crypto.values.x509.PrivateKeyUsageIntervalValue;
import org.codeartisans.qipki.commons.crypto.values.x509.SubjectKeyIdentifierValue;
import org.codeartisans.qipki.commons.crypto.values.x509.X509GeneralNameValue;
import org.codeartisans.qipki.commons.crypto.values.x509.X509GeneralSubtreeValue;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;

public class CryptoValuesModuleAssembler
        implements Assembler
{

    private final Visibility visibility;

    public CryptoValuesModuleAssembler()
    {
        this( Visibility.module );
    }

    public CryptoValuesModuleAssembler( Visibility visibility )
    {
        this.visibility = visibility;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        module.addValues( KeyPairSpecValue.class,
                          ValidityIntervalValue.class,
                          X509GeneralNameValue.class,
                          X509GeneralSubtreeValue.class,
                          KeysExtensionsValue.class,
                          KeyUsagesValue.class,
                          ExtendedKeyUsagesValue.class,
                          NetscapeCertTypesValue.class,
                          SubjectKeyIdentifierValue.class,
                          AuthorityKeyIdentifierValue.class,
                          PrivateKeyUsageIntervalValue.class,
                          CRLDistributionPointsValue.class,
                          PoliciesExtensionsValue.class,
                          CertificatePoliciesValue.class,
                          CertificatePoliciesValue.PolicyInformationValue.class,
                          CertificatePoliciesValue.PolicyQualifierInfoValue.class,
                          PolicyMappingsValue.class,
                          PolicyMappingsValue.PolicyMappingValue.class,
                          NamesExtensionsValue.class,
                          AlternativeNamesValue.class,
                          ConstraintsExtensionsValue.class,
                          BasicConstraintsValue.class,
                          PolicyConstraintsValue.class,
                          PolicyConstraintsValue.PolicyConstraintValue.class,
                          NameConstraintsValue.class ).
                visibleIn( visibility );
        module.addServices( CryptoValuesFactory.class,
                            X509ExtensionsValueFactory.class ).
                visibleIn( visibility );
    }

}
