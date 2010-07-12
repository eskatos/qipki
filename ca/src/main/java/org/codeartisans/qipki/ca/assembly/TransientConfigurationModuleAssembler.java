package org.codeartisans.qipki.ca.assembly;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.entitystore.memory.MemoryEntityStoreService;
import org.qi4j.library.http.JettyConfiguration;

public class TransientConfigurationModuleAssembler
        implements Assembler
{

    private final Visibility visibility;

    public TransientConfigurationModuleAssembler()
    {
        this( Visibility.layer );
    }

    public TransientConfigurationModuleAssembler( Visibility visibility )
    {
        this.visibility = visibility;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        module.addServices( MemoryEntityStoreService.class ).visibleIn( Visibility.module );
        module.addEntities( JettyConfiguration.class ).visibleIn( visibility );
    }

}
