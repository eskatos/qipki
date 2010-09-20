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
package org.codeartisans.qipki.ca.http.assembly;

import org.codeartisans.qipki.ca.assembly.CaAssemblyNames;
import org.codeartisans.qipki.ca.assembly.QiPkiVolatileEmbeddedCaAssembler;
import org.codeartisans.qipki.core.assembly.AssemblyUtil;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.ApplicationAssemblyFactory;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.LayerAssembly;

@SuppressWarnings( "unchecked" )
public class QiPkiHttpCaAssembler
        extends QiPkiVolatileEmbeddedCaAssembler
{

    @Override
    public final ApplicationAssembly assemble( ApplicationAssemblyFactory applicationFactory )
            throws AssemblyException
    {

        ApplicationAssembly app = super.assemble( applicationFactory );

        LayerAssembly presentation = app.layerAssembly( HttpCaAssemblyNames.LAYER_PRESENTATION );
        {
            assembleDevTestModule( presentation.moduleAssembly( HttpCaAssemblyNames.MODULE_TESTS_IN_PRESENTATION ) );

            new RestApiModuleAssembler().assemble(
                    presentation.moduleAssembly( HttpCaAssemblyNames.MODULE_REST_API ) );

            new HttpModuleAssembler().assemble(
                    presentation.moduleAssembly( HttpCaAssemblyNames.MODULE_HTTP ) );

            new TransientConfigurationModuleAssembler( Visibility.layer ).assemble(
                    presentation.moduleAssembly( CaAssemblyNames.MODULE_CONFIGURATION ) );
        }

        presentation.uses( AssemblyUtil.getLayerAssembly( app, CaAssemblyNames.LAYER_APPLICATION ),
                           AssemblyUtil.getLayerAssembly( app, CaAssemblyNames.LAYER_CRYPTO ) );

        return app;
    }

}
