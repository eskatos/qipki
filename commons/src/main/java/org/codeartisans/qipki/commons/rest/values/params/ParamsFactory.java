package org.codeartisans.qipki.commons.rest.values.params;

import org.codeartisans.qipki.commons.crypto.states.KeyEscrowPolicy;
import org.codeartisans.qipki.crypto.storage.KeyStoreType;
import org.codeartisans.qipki.commons.crypto.values.KeyPairSpecValue;
import org.codeartisans.qipki.commons.crypto.values.x509.BasicConstraintsValue;
import org.codeartisans.qipki.commons.crypto.values.x509.ExtendedKeyUsagesValue;
import org.codeartisans.qipki.commons.crypto.values.x509.KeyUsagesValue;
import org.codeartisans.qipki.commons.crypto.values.x509.NameConstraintsValue;
import org.codeartisans.qipki.commons.crypto.values.x509.NetscapeCertTypesValue;
import org.codeartisans.qipki.commons.rest.values.representations.X509ProfileAssignmentValue;
import org.codeartisans.qipki.crypto.x509.RevocationReason;
import org.qi4j.api.common.Optional;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.value.ValueBuilder;
import org.qi4j.api.value.ValueBuilderFactory;

@Mixins( ParamsFactory.Mixin.class )
public interface ParamsFactory
        extends ServiceComposite
{

    CryptoStoreFactoryParamsValue createKeyStoreFactoryParams( String name, KeyStoreType storeType, char[] password );

    CAFactoryParamsValue createCAFactoryParams( String keyStoreUri, String name, String distinguishedName, KeyPairSpecValue keySpec, @Optional String parentCaUri );

    X509ProfileFactoryParamsValue createX509ProfileFactoryParams( String name,
                                                                  @Optional String comment,
                                                                  @Optional KeyUsagesValue keyUsages,
                                                                  @Optional ExtendedKeyUsagesValue extendedKeyUsages,
                                                                  @Optional NetscapeCertTypesValue netscapeCertTypes,
                                                                  @Optional BasicConstraintsValue basicConstraints,
                                                                  @Optional NameConstraintsValue nameConstraints );

    X509ProfileAssignmentValue createX509ProfileAssignment( String x509ProfileUri, KeyEscrowPolicy keyEscrowPolicy );

    X509FactoryParamsValue createX509FactoryParams( String caUri, String x509ProfileUri, String pemPkcs10 );

    X509RevocationParamsValue createX509RevocationParams( RevocationReason reason );

    abstract class Mixin
            implements ParamsFactory
    {

        @Structure
        private ValueBuilderFactory vbf;

        @Override
        public CryptoStoreFactoryParamsValue createKeyStoreFactoryParams( String name, KeyStoreType storeType, char[] password )
        {
            ValueBuilder<CryptoStoreFactoryParamsValue> paramsBuilder = vbf.newValueBuilder( CryptoStoreFactoryParamsValue.class );
            CryptoStoreFactoryParamsValue params = paramsBuilder.prototype();
            params.name().set( name );
            params.storeType().set( storeType );
            params.password().set( password );
            return paramsBuilder.newInstance();
        }

        @Override
        public CAFactoryParamsValue createCAFactoryParams( String keyStoreUri, String name, String distinguishedName, KeyPairSpecValue keySpec, String parentCaUri )
        {
            ValueBuilder<CAFactoryParamsValue> paramsBuilder = vbf.newValueBuilder( CAFactoryParamsValue.class );
            CAFactoryParamsValue params = paramsBuilder.prototype();
            params.cryptoStoreUri().set( keyStoreUri );
            params.name().set( name );
            params.distinguishedName().set( distinguishedName );
            params.keySpec().set( keySpec );
            params.parentCaUri().set( parentCaUri );
            return paramsBuilder.newInstance();
        }

        @Override
        public X509ProfileFactoryParamsValue createX509ProfileFactoryParams( String name, String comment,
                                                                             KeyUsagesValue keyUsages, ExtendedKeyUsagesValue extendedKeyUsages, NetscapeCertTypesValue netscapeCertTypes,
                                                                             BasicConstraintsValue basicConstraints, NameConstraintsValue nameConstraints )
        {
            ValueBuilder<X509ProfileFactoryParamsValue> paramsBuilder = vbf.newValueBuilder( X509ProfileFactoryParamsValue.class );
            X509ProfileFactoryParamsValue params = paramsBuilder.prototype();
            params.name().set( name );
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
        public X509RevocationParamsValue createX509RevocationParams( RevocationReason reason )
        {
            ValueBuilder<X509RevocationParamsValue> paramsBuilder = vbf.newValueBuilder( X509RevocationParamsValue.class );
            X509RevocationParamsValue params = paramsBuilder.prototype();
            params.reason().set( reason );
            return paramsBuilder.newInstance();
        }

    }

}