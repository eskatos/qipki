package org.codeartisans.qipki.ca.assembly;

import org.codeartisans.qipki.ca.domain.ca.CAFactory;
import org.codeartisans.qipki.ca.domain.ca.CARepository;
import org.codeartisans.qipki.ca.domain.ca.profileassignment.X509ProfileAssignmentEntity;
import org.codeartisans.qipki.ca.domain.ca.profileassignment.X509ProfileAssignmentFactory;
import org.codeartisans.qipki.ca.domain.ca.root.RootCAEntity;
import org.codeartisans.qipki.ca.domain.ca.sub.SubCAEntity;
import org.codeartisans.qipki.ca.domain.crl.CRLEntity;
import org.codeartisans.qipki.ca.domain.crl.CRLFactory;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStoreEntity;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStoreFactory;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStoreRepository;
import org.codeartisans.qipki.ca.domain.revocation.RevocationEntity;
import org.codeartisans.qipki.ca.domain.revocation.RevocationFactory;
import org.codeartisans.qipki.ca.domain.x509.X509Entity;
import org.codeartisans.qipki.ca.domain.x509.X509Factory;
import org.codeartisans.qipki.ca.domain.x509.X509Repository;
import org.codeartisans.qipki.ca.domain.x509profile.X509ProfileEntity;
import org.codeartisans.qipki.ca.domain.x509profile.X509ProfileFactory;
import org.codeartisans.qipki.ca.domain.x509profile.X509ProfileRepository;
import org.codeartisans.qipki.commons.crypto.services.CryptoValuesFactory;
import org.codeartisans.qipki.commons.crypto.values.ValidityIntervalValue;
import org.codeartisans.qipki.core.sideeffects.TracingSideEffect;
import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;

public class CaDomainModuleAssembler
        implements Assembler
{

    @Override
    @SuppressWarnings( "unchecked" )
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        // Values
        module.addValues( ValidityIntervalValue.class );

        // Entities
        module.addEntities( CryptoStoreEntity.class,
                            RootCAEntity.class,
                            SubCAEntity.class,
                            CRLEntity.class,
                            X509ProfileAssignmentEntity.class,
                            X509ProfileEntity.class,
                            X509Entity.class,
                            RevocationEntity.class ).
                visibleIn( Visibility.application );

        // Services
        module.addServices( CryptoStoreRepository.class,
                            CryptoStoreFactory.class,
                            CARepository.class,
                            CAFactory.class,
                            CRLFactory.class,
                            X509ProfileAssignmentFactory.class,
                            X509ProfileRepository.class,
                            X509ProfileFactory.class,
                            X509Repository.class,
                            X509Factory.class,
                            RevocationFactory.class,
                            CryptoValuesFactory.class ).
                visibleIn( Visibility.application ).
                withSideEffects( TracingSideEffect.class );
    }

}
