package org.codeartisans.qipki.ca.assembly;

import org.codeartisans.qipki.ca.application.contexts.RootContext;
import org.codeartisans.qipki.ca.application.contexts.ca.CAContext;
import org.codeartisans.qipki.ca.application.contexts.ca.CAListContext;
import org.codeartisans.qipki.ca.application.contexts.cryptostore.CryptoStoreContext;
import org.codeartisans.qipki.ca.application.contexts.cryptostore.CryptoStoreListContext;
import org.codeartisans.qipki.ca.application.contexts.x509.X509Context;
import org.codeartisans.qipki.ca.application.contexts.x509.X509ListContext;
import org.codeartisans.qipki.ca.application.contexts.x509profile.X509ProfileContext;
import org.codeartisans.qipki.ca.application.contexts.x509profile.X509ProfileListContext;
import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;

public class CaDCIModuleAssembler
        implements Assembler
{

    @Override
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        module.addObjects( RootContext.class,
                           CryptoStoreListContext.class,
                           CryptoStoreContext.class,
                           CAListContext.class,
                           CAContext.class,
                           X509ProfileListContext.class,
                           X509ProfileContext.class,
                           X509ListContext.class,
                           X509Context.class ).
                visibleIn( Visibility.application );

    }

}
