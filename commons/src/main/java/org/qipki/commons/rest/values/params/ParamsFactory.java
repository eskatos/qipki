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
package org.qipki.commons.rest.values.params;

import java.util.Collections;
import java.util.List;
import org.qi4j.api.common.Optional;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.value.ValueBuilder;
import org.qi4j.api.value.ValueBuilderFactory;

import org.qipki.commons.crypto.states.KeyEscrowPolicy;
import org.qipki.commons.crypto.values.KeyPairSpecValue;
import org.qipki.commons.crypto.values.x509.BasicConstraintsValue;
import org.qipki.commons.crypto.values.x509.ExtendedKeyUsagesValue;
import org.qipki.commons.crypto.values.x509.KeyUsagesValue;
import org.qipki.commons.crypto.values.x509.NameConstraintsValue;
import org.qipki.commons.crypto.values.x509.NetscapeCertTypesValue;
import org.qipki.commons.rest.values.representations.X509ProfileAssignmentValue;
import org.qipki.crypto.algorithms.AsymetricAlgorithm;
import org.qipki.crypto.storage.KeyStoreType;
import org.qipki.crypto.x509.RevocationReason;

@Mixins( ParamsFactory.Mixin.class )
public interface ParamsFactory
        extends ServiceComposite
{

    CryptoStoreFactoryParamsValue createCryptoStoreFactoryParams( String name, KeyStoreType storeType, char[] password );

    CAFactoryParamsValue createCAFactoryParams( String keyStoreUri,
                                                String name, Integer validityDays,
                                                String distinguishedName, KeyPairSpecValue keySpec,
                                                @Optional String parentCaUri );

    CAFactoryParamsValue createCAFactoryParams( String keyStoreUri,
                                                String name, Integer validityDays,
                                                String distinguishedName, KeyPairSpecValue keySpec,
                                                List<String> crlDistPoints,
                                                @Optional String parentCaUri );

    X509ProfileFactoryParamsValue createX509ProfileFactoryParams( String name,
                                                                  Integer validityDays,
                                                                  @Optional String comment,
                                                                  @Optional KeyUsagesValue keyUsages,
                                                                  @Optional ExtendedKeyUsagesValue extendedKeyUsages,
                                                                  @Optional NetscapeCertTypesValue netscapeCertTypes,
                                                                  @Optional BasicConstraintsValue basicConstraints,
                                                                  @Optional NameConstraintsValue nameConstraints );

    X509ProfileAssignmentValue createX509ProfileAssignment( String x509ProfileUri, KeyEscrowPolicy keyEscrowPolicy );

    X509FactoryParamsValue createX509FactoryParams( String caUri, String x509ProfileUri, String pemPkcs10 );

    X509FactoryParamsValue createX509FactoryParams( String caUri, String x509ProfileUri, String escrowedKeyPairUri, String distinguishedName );

    X509RevocationParamsValue createX509RevocationParams( RevocationReason reason );

    EscrowedKeyPairFactoryParamsValue createEscrowedKeyPairFactoryParams( AsymetricAlgorithm algorithm, Integer length );

    @SuppressWarnings( "PublicInnerClass" )
    abstract class Mixin
            implements ParamsFactory
    {

        @Structure
        private ValueBuilderFactory vbf;

        @Override
        public CryptoStoreFactoryParamsValue createCryptoStoreFactoryParams( String name, KeyStoreType storeType, char[] password )
        {
            ValueBuilder<CryptoStoreFactoryParamsValue> paramsBuilder = vbf.newValueBuilder( CryptoStoreFactoryParamsValue.class );
            CryptoStoreFactoryParamsValue params = paramsBuilder.prototype();
            params.name().set( name );
            params.storeType().set( storeType );
            params.password().set( password );
            return paramsBuilder.newInstance();
        }

        @Override
        public CAFactoryParamsValue createCAFactoryParams( String keyStoreUri,
                                                           String name, Integer validityDays, String distinguishedName,
                                                           KeyPairSpecValue keySpec,
                                                           String parentCaUri )
        {
            return createCAFactoryParams( keyStoreUri, name, validityDays, distinguishedName, keySpec, Collections.<String>emptyList(), parentCaUri );
        }

        @Override
        public CAFactoryParamsValue createCAFactoryParams( String keyStoreUri,
                                                           String name, Integer validityDays, String distinguishedName,
                                                           KeyPairSpecValue keySpec, List<String> crlDistPoints,
                                                           String parentCaUri )
        {
            ValueBuilder<CAFactoryParamsValue> paramsBuilder = vbf.newValueBuilder( CAFactoryParamsValue.class );
            CAFactoryParamsValue params = paramsBuilder.prototype();
            params.cryptoStoreUri().set( keyStoreUri );
            params.name().set( name );
            params.validityDays().set( validityDays );
            params.distinguishedName().set( distinguishedName );
            params.keySpec().set( keySpec );
            params.crlDistPoints().get().addAll( crlDistPoints );
            params.parentCaUri().set( parentCaUri );
            return paramsBuilder.newInstance();
        }

        @Override
        public X509ProfileFactoryParamsValue createX509ProfileFactoryParams( String name, Integer validityDays, String comment,
                                                                             KeyUsagesValue keyUsages, ExtendedKeyUsagesValue extendedKeyUsages, NetscapeCertTypesValue netscapeCertTypes,
                                                                             BasicConstraintsValue basicConstraints, NameConstraintsValue nameConstraints )
        {
            ValueBuilder<X509ProfileFactoryParamsValue> paramsBuilder = vbf.newValueBuilder( X509ProfileFactoryParamsValue.class );
            X509ProfileFactoryParamsValue params = paramsBuilder.prototype();
            params.name().set( name );
            params.validityDays().set( validityDays );
            params.netscapeCertComment().set( comment );
            params.keyUsages().set( keyUsages );
            params.extendedKeyUsages().set( extendedKeyUsages );
            params.netscapeCertTypes().set( netscapeCertTypes );
            params.basicConstraints().set( basicConstraints );
            params.nameConstraints().set( nameConstraints );
            return paramsBuilder.newInstance();
        }

        @Override
        public X509ProfileAssignmentValue createX509ProfileAssignment( String x509ProfileUri, KeyEscrowPolicy keyEscrowPolicy )
        {
            ValueBuilder<X509ProfileAssignmentValue> profileBuilder = vbf.newValueBuilder( X509ProfileAssignmentValue.class );
            X509ProfileAssignmentValue profile = profileBuilder.prototype();
            profile.keyEscrowPolicy().set( keyEscrowPolicy );
            profile.x509ProfileUri().set( x509ProfileUri );
            return profileBuilder.newInstance();
        }

        @Override
        public X509FactoryParamsValue createX509FactoryParams( String caUri, String x509ProfileUri, String pemPkcs10 )
        {
            ValueBuilder<X509FactoryParamsValue> paramsBuilder = vbf.newValueBuilder( X509FactoryParamsValue.class );
            X509FactoryParamsValue params = paramsBuilder.prototype();
            params.caUri().set( caUri );
            params.x509ProfileUri().set( x509ProfileUri );
            params.pemPkcs10().set( pemPkcs10 );
            return paramsBuilder.newInstance();
        }

        @Override
        public X509FactoryParamsValue createX509FactoryParams( String caUri, String x509ProfileUri, String escrowedKeyPairUri, String distinguishedName )
        {
            ValueBuilder<X509FactoryParamsValue> paramsBuilder = vbf.newValueBuilder( X509FactoryParamsValue.class );
            X509FactoryParamsValue params = paramsBuilder.prototype();
            params.caUri().set( caUri );
            params.x509ProfileUri().set( x509ProfileUri );
            params.escrowedKeyPairUri().set( escrowedKeyPairUri );
            params.distinguishedName().set( distinguishedName );
            return paramsBuilder.newInstance();
        }

        @Override
        public X509RevocationParamsValue createX509RevocationParams( RevocationReason reason )
        {
            ValueBuilder<X509RevocationParamsValue> paramsBuilder = vbf.newValueBuilder( X509RevocationParamsValue.class );
            X509RevocationParamsValue params = paramsBuilder.prototype();
            params.reason().set( reason );
            return paramsBuilder.newInstance();
        }

        @Override
        public EscrowedKeyPairFactoryParamsValue createEscrowedKeyPairFactoryParams( AsymetricAlgorithm algorithm, Integer length )
        {
            ValueBuilder<EscrowedKeyPairFactoryParamsValue> paramsBuilder = vbf.newValueBuilder( EscrowedKeyPairFactoryParamsValue.class );
            EscrowedKeyPairFactoryParamsValue params = paramsBuilder.prototype();
            params.algorithm().set( algorithm );
            params.length().set( length );
            return paramsBuilder.newInstance();
        }

    }

}
