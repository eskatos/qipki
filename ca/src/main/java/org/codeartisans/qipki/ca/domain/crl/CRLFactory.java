package org.codeartisans.qipki.ca.domain.crl;

import java.math.BigInteger;
import org.qi4j.api.entity.EntityBuilder;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

@Mixins( CRLFactory.Mixin.class )
public interface CRLFactory
        extends ServiceComposite
{

    CRLEntity create( String pem );

    abstract class Mixin
            implements CRLFactory
    {

        @Structure
        private UnitOfWorkFactory uowf;

        @Override
        public CRLEntity create( String payload )
        {
            EntityBuilder<CRLEntity> crlBuilder = uowf.currentUnitOfWork().newEntityBuilder( CRLEntity.class );
            CRLEntity crl = crlBuilder.instance();
            crl.pem().set( payload );
            crl.lastCRLNumber().set( BigInteger.ZERO );
            return crlBuilder.newInstance();
        }

    }

}
