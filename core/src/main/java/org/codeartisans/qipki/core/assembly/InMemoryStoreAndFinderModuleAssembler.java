package org.codeartisans.qipki.core.assembly;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.entitystore.memory.MemoryEntityStoreService;
import org.qi4j.index.rdf.assembly.RdfMemoryStoreAssembler;
import org.qi4j.spi.uuid.UuidIdentityGeneratorService;

public class InMemoryStoreAndFinderModuleAssembler
        implements Assembler
{

    private final Visibility visibility;

    public InMemoryStoreAndFinderModuleAssembler()
    {
        this( Visibility.application );
    }

    public InMemoryStoreAndFinderModuleAssembler( Visibility visibility )
    {
        this.visibility = visibility;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        module.addServices( MemoryEntityStoreService.class, UuidIdentityGeneratorService.class ).visibleIn( visibility );
        new RdfMemoryStoreAssembler( null, visibility, visibility ).assemble( module );
    }

}
