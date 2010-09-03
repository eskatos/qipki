/*
 * Copyright (c) 2010 Paul Merlin <paul@nosphere.org>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.codeartisans.qipki.ca;

import org.codeartisans.qipki.ca.application.contexts.RootContext;
import org.codeartisans.qipki.ca.assembly.AssemblyNames;
import org.codeartisans.qipki.ca.assembly.QiPkiEmbeddedCaAssembler;
import org.codeartisans.qipki.core.AbstractQiPkiApplication;
import org.codeartisans.qipki.core.dci.InteractionContext;

import org.qi4j.api.structure.Module;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

public final class QiPkiEmbeddedCa
        extends AbstractQiPkiApplication
{

    private Module dciModule;

    public QiPkiEmbeddedCa()
    {
        super( new QiPkiEmbeddedCaAssembler() );
    }

    public UnitOfWorkFactory unitOfWorkFactory()
    {
        return ensureDCIModule().unitOfWorkFactory();
    }

    public RootContext newRootContext()
    {
        return ensureDCIModule().objectBuilderFactory().newObjectBuilder( RootContext.class ).use( new InteractionContext() ).newInstance();
    }

    private Module ensureDCIModule()
    {
        if ( dciModule == null ) {
            dciModule = application.findModule( AssemblyNames.LAYER_APPLICATION, AssemblyNames.MODULE_CA_DCI );
        }
        return dciModule;
    }

    @Override
    protected void afterPassivate()
    {
        dciModule = null;
    }

}
