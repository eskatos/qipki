package org.codeartisans.qipki.commons.values.params;

import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.value.ValueBuilder;
import org.qi4j.api.value.ValueBuilderFactory;

@Mixins( ParamsFactory.Mixin.class )
public interface ParamsFactory
        extends ServiceComposite
{

    KeyStoreFactoryParamsValue createKeyStoreFactoryParams( String name, String storeType, char[] password );

    abstract class Mixin
            implements ParamsFactory
    {

        @Structure
        private ValueBuilderFactory vbf;

        @Override
        public KeyStoreFactoryParamsValue createKeyStoreFactoryParams( String name, String storeType, char[] password )
        {
            ValueBuilder<KeyStoreFactoryParamsValue> paramsBuilder = vbf.newValueBuilder( KeyStoreFactoryParamsValue.class );
            KeyStoreFactoryParamsValue params = paramsBuilder.prototype();
            params.name().set( name );
            params.storeType().set( storeType );
            params.password().set( password );
            return paramsBuilder.newInstance();
        }

    }

}
