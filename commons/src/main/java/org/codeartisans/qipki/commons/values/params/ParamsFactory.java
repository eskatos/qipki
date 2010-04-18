package org.codeartisans.qipki.commons.values.params;

import org.codeartisans.qipki.commons.constants.KeyStoreType;
import org.codeartisans.qipki.commons.values.crypto.KeySpecValue;
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

    KeySpecValue createKeySpec( String algorithm, Integer length );

    CAFactoryParamsValue createCAFactoryParams( String keyStoreIdentity, String name, String distinguishedName, KeySpecValue keySpec, @Optional String parentCaIdentity );

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
        public KeySpecValue createKeySpec( String algorithm, Integer length )
        {
            ValueBuilder<KeySpecValue> keySpecBuilder = vbf.newValueBuilder( KeySpecValue.class );
            KeySpecValue keySpec = keySpecBuilder.prototype();
            keySpec.algorithm().set( algorithm );
            keySpec.length().set( length );
            return keySpecBuilder.newInstance();
        }

        @Override
        public CAFactoryParamsValue createCAFactoryParams( String keyStoreIdentity, String name, String distinguishedName, KeySpecValue keySpec, String parentCaIdentity )
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

    }

}
