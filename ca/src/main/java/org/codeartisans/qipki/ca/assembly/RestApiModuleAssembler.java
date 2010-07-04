package org.codeartisans.qipki.ca.assembly;

import org.codeartisans.qipki.ca.presentation.rest.RestletApplication;
import org.codeartisans.qipki.ca.presentation.rest.RestletFinder;
import org.codeartisans.qipki.ca.presentation.rest.RestletValuesFactory;
import org.codeartisans.qipki.ca.presentation.rest.resources.ApiRootResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.ca.CAListResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.ca.CAResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.ca.CRLResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.cryptostore.CryptoStoreListResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.cryptostore.CryptoStoreResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.x509.X509DetailResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.x509.X509ListResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.x509.X509Resource;
import org.codeartisans.qipki.ca.presentation.rest.resources.x509.X509RevocationResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.x509profile.X509ProfileListResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.x509profile.X509ProfileResource;
import org.codeartisans.qipki.commons.assembly.RestValuesModuleAssembler;
import org.qi4j.api.common.Visibility;
import org.codeartisans.qipki.ca.presentation.rest.RestletServletServerService;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import static org.qi4j.library.http.Servlets.*;

public class RestApiModuleAssembler
        implements Assembler
{

    @Override
    @SuppressWarnings( "unchecked" )
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        module.addObjects( RestletApplication.class ).
                visibleIn( Visibility.layer );

        module.addObjects( RestletFinder.class,
                           ApiRootResource.class,
                           CryptoStoreListResource.class,
                           CryptoStoreResource.class,
                           CAListResource.class,
                           CAResource.class,
                           CRLResource.class,
                           X509ProfileListResource.class,
                           X509ProfileResource.class,
                           X509ListResource.class,
                           X509Resource.class,
                           X509DetailResource.class,
                           X509RevocationResource.class ).
                visibleIn( Visibility.module );

        new RestValuesModuleAssembler( Visibility.layer ).assemble( module );

        module.addServices( RestletValuesFactory.class ).
                visibleIn( Visibility.module );

        addServlets( serve( "/api/*" ).with( RestletServletServerService.class ) ).
                to( module );

        // Not needed, see RestletApplication
        // addFilters( filter( "/api/*" ).through( UnitOfWorkFilterService.class ).on( REQUEST ) ).to( module );
    }

}
