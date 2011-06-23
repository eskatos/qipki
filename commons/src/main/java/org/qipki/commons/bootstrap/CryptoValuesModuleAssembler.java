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
package org.qipki.commons.bootstrap;

import org.qipki.commons.crypto.services.CryptoValuesFactory;
import org.qipki.commons.crypto.values.KeyPairSpecValue;
import org.qipki.commons.crypto.values.ValidityIntervalValue;
import org.qipki.commons.crypto.services.X509ExtensionsValueFactory;
import org.qipki.commons.crypto.values.x509.AlternativeNamesValue;
import org.qipki.commons.crypto.values.x509.AuthorityKeyIdentifierValue;
import org.qipki.commons.crypto.values.x509.BasicConstraintsValue;
import org.qipki.commons.crypto.values.x509.CRLDistributionPointsValue;
import org.qipki.commons.crypto.values.x509.CertificatePoliciesValue;
import org.qipki.commons.crypto.values.x509.ConstraintsExtensionsValue;
import org.qipki.commons.crypto.values.x509.ExtendedKeyUsagesValue;
import org.qipki.commons.crypto.values.x509.KeyUsagesValue;
import org.qipki.commons.crypto.values.x509.KeysExtensionsValue;
import org.qipki.commons.crypto.values.x509.NameConstraintsValue;
import org.qipki.commons.crypto.values.x509.NamesExtensionsValue;
import org.qipki.commons.crypto.values.x509.NetscapeCertTypesValue;
import org.qipki.commons.crypto.values.x509.PoliciesExtensionsValue;
import org.qipki.commons.crypto.values.x509.PolicyConstraintsValue;
import org.qipki.commons.crypto.values.x509.PolicyMappingsValue;
import org.qipki.commons.crypto.values.x509.PrivateKeyUsageIntervalValue;
import org.qipki.commons.crypto.values.x509.SubjectKeyIdentifierValue;
import org.qipki.commons.crypto.values.x509.X509GeneralNameValue;
import org.qipki.commons.crypto.values.x509.X509GeneralSubtreeValue;

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
