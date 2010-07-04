package org.codeartisans.qipki.ca.utils;

import org.codeartisans.qipki.ca.assembly.QiPkiCaAssembler;
import org.codeartisans.qipki.core.AbstractQiPkiApplication;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;

public class QiPkiTestApplicationCa
        extends AbstractQiPkiApplication
{

    public QiPkiTestApplicationCa()
    {
        super( new QiPkiCaAssembler()
        {

            @Override
            protected void assembleDevTestModule( ModuleAssembly devTestModule )
                    throws AssemblyException
            {
                devTestModule.addServices( QiPkiCaFixtures.class ).instantiateOnStartup();
            }

        } );
    }

}
