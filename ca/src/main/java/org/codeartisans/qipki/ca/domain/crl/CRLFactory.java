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

    CRL create( String pem );

    abstract class Mixin
            implements CRLFactory
    {

        @Structure
        private UnitOfWorkFactory uowf;

        @Override
        public CRL create( String payload )
        {
            EntityBuilder<CRL> crlBuilder = uowf.currentUnitOfWork().newEntityBuilder( CRL.class );
            CRL crl = crlBuilder.instance();
            crl.pem().set( payload );
            crl.lastCRLNumber().set( BigInteger.ZERO );
            return crlBuilder.newInstance();
        }

    }

}
