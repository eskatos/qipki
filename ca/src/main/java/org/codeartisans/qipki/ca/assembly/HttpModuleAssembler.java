package org.codeartisans.qipki.ca.assembly;

import org.codeartisans.qipki.ca.presentation.http.HttpService;
import org.codeartisans.qipki.ca.presentation.http.RootServletService;
import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import static org.qi4j.library.http.Servlets.*;

public class HttpModuleAssembler
        implements Assembler
{

    /**
     * Servlets are added with layer visibility.
     * 
     * @param module                the Module to assemble
     * @throws AssemblyException    thrown if the assembler tries to do something illegal
     */
    @Override
    @SuppressWarnings( "unchecked" )
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        module.addServices( HttpService.class ).visibleIn( Visibility.module ).instantiateOnStartup();
        addServlets( serve( "/" ).with( RootServletService.class ) ).to( module );
    }

}
