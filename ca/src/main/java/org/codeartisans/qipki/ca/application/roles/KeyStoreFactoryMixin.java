package org.codeartisans.qipki.ca.application.roles;

import org.codeartisans.qipki.ca.domain.keystore.KeyStoreEntity;
import org.codeartisans.qipki.commons.values.params.KeyStoreFactoryParamsValue;
import org.qi4j.api.entity.EntityBuilder;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.unitofwork.UnitOfWork;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

public abstract class KeyStoreFactoryMixin
        implements KeyStoreFactory
{

    @Structure
    private UnitOfWorkFactory uowf;

    @Override
    public KeyStoreEntity create( KeyStoreFactoryParamsValue params )
    {
        UnitOfWork uow = uowf.currentUnitOfWork();
        EntityBuilder<KeyStoreEntity> ksBuilder = uow.newEntityBuilder( KeyStoreEntity.class );
        KeyStoreEntity ks = ksBuilder.instance();
        ks.name().set( params.name().get() );
        ks.storeType().set( params.storeType().get() );
        ks.password().set( params.password().get() );
        // TODO Create new empty keystore
        ks.path().set( "TODO" );
        return ksBuilder.newInstance();
    }

}
