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
import org.codeartisans.qipki.commons.values.crypto.X509ExtensionsValueFactory;
import org.codeartisans.qipki.commons.values.crypto.x509.AlternativeNamesValue;
import org.codeartisans.qipki.commons.values.crypto.x509.AuthorityKeyIdentifierValue;
import org.codeartisans.qipki.commons.values.crypto.x509.BasicConstraintsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.CRLDistributionPointsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.CertificatePoliciesValue;
import org.codeartisans.qipki.commons.values.crypto.x509.ConstraintsExtensionsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.ExtendedKeyUsagesValue;
import org.codeartisans.qipki.commons.values.crypto.x509.KeyUsagesValue;
import org.codeartisans.qipki.commons.values.crypto.x509.KeysExtensionsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.NameConstraintsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.NamesExtensionsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.NetscapeCertTypesValue;
import org.codeartisans.qipki.commons.values.crypto.x509.PoliciesExtensionsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.PolicyConstraintsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.PolicyMappingsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.PrivateKeyUsageIntervalValue;
import org.codeartisans.qipki.commons.values.crypto.x509.SubjectKeyIdentifierValue;
import org.codeartisans.qipki.commons.values.crypto.x509.X509GeneralNameValue;
import org.codeartisans.qipki.commons.values.crypto.x509.X509GeneralSubtreeValue;
import org.codeartisans.qipki.commons.values.params.CAFactoryParamsValue;
import org.codeartisans.qipki.commons.values.params.CryptoStoreFactoryParamsValue;
import org.codeartisans.qipki.commons.values.params.ParamsFactory;
import org.codeartisans.qipki.commons.values.params.X509FactoryParamsValue;
import org.codeartisans.qipki.commons.values.params.X509ProfileFactoryParamsValue;
import org.codeartisans.qipki.commons.values.params.X509RevocationParamsValue;
import org.codeartisans.qipki.commons.values.rest.ApiURIsValue;
import org.codeartisans.qipki.commons.values.rest.CAValue;
import org.codeartisans.qipki.commons.values.rest.CryptoStoreValue;
import org.codeartisans.qipki.commons.values.rest.RestListValue;
import org.codeartisans.qipki.commons.values.rest.RevocationValue;
import org.codeartisans.qipki.commons.values.rest.X509DetailValue;
import org.codeartisans.qipki.commons.values.rest.X509ProfileAssignmentValue;
import org.codeartisans.qipki.commons.values.rest.X509ProfileValue;
import org.codeartisans.qipki.commons.values.rest.X509Value;
import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;

@SuppressWarnings( "unchecked" )
public class QiPkiCommonsValuesAssembler
        implements Assembler
{

    private final Visibility visibility;

    public QiPkiCommonsValuesAssembler()
    {
        this( Visibility.module );
    }

    public QiPkiCommonsValuesAssembler( Visibility visibility )
    {
        this.visibility = visibility;
    }

    @Override
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        // Params
        module.addValues( CryptoStoreFactoryParamsValue.class,
                          CAFactoryParamsValue.class,
                          X509ProfileFactoryParamsValue.class,
                          X509FactoryParamsValue.class,
                          X509RevocationParamsValue.class ).
                visibleIn( visibility );
        module.addServices( ParamsFactory.class ).
                visibleIn( visibility );

        // Crypto values
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

        // Rest values
        module.addValues( RestListValue.class,
                          ApiURIsValue.class,
                          CryptoStoreValue.class,
                          CAValue.class,
                          X509ProfileAssignmentValue.class,
                          X509ProfileValue.class,
                          X509Value.class,
                          X509DetailValue.class,
                          RevocationValue.class ).
                visibleIn( visibility );

    }

}
