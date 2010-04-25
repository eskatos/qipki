package org.codeartisans.qipki.commons.values.params;

import org.codeartisans.qipki.crypto.storage.KeyStoreType;
import org.codeartisans.qipki.commons.values.crypto.KeyPairSpecValue;
import org.codeartisans.qipki.crypto.algorithms.AsymetricAlgorithm;
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

    KeyPairSpecValue createKeySpec( AsymetricAlgorithm algorithm, Integer length );

    CAFactoryParamsValue createCAFactoryParams( String keyStoreIdentity, String name, String distinguishedName, KeyPairSpecValue keySpec, @Optional String parentCaIdentity );

    X509FactoryParamsValue createX509FactoryParams( String caIdentity, String pemPkcs10 );

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
        public KeyPairSpecValue createKeySpec( AsymetricAlgorithm algorithm, Integer length )
        {
            ValueBuilder<KeyPairSpecValue> keySpecBuilder = vbf.newValueBuilder( KeyPairSpecValue.class );
            KeyPairSpecValue keySpec = keySpecBuilder.prototype();
            keySpec.algorithm().set( algorithm );
            keySpec.length().set( length );
            return keySpecBuilder.newInstance();
        }

        @Override
        public CAFactoryParamsValue createCAFactoryParams( String keyStoreIdentity, String name, String distinguishedName, KeyPairSpecValue keySpec, String parentCaIdentity )
        {
            ValueBuilder<CAFactoryParamsValue> paramsBuilder = vbf.newValueBuilder( CAFactoryParamsValue.class );
            CAFactoryParamsValue params = paramsBuilder.prototype();
            params.keyStoreIdentity().set( keyStoreIdentity );
            params.name().set( name );
            params.distinguishedName().set( distinguishedName );
            params.keySpec().set( keySpec );
            params.parentCaIdentity().set( parentCaIdentity );
            return paramsBuilder.newInstance();
        }

        @Override
        public X509FactoryParamsValue createX509FactoryParams( String caIdentity, String pemPkcs10 )
        {
            ValueBuilder<X509FactoryParamsValue> paramsBuilder = vbf.newValueBuilder( X509FactoryParamsValue.class );
            X509FactoryParamsValue params = paramsBuilder.prototype();
            params.caIdentity().set( caIdentity );
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
