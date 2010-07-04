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
package org.codeartisans.qipki.ca.assembly;

import org.codeartisans.qipki.core.assembly.InMemoryStoreAndFinderModuleAssembler;
import org.codeartisans.qipki.commons.assembly.CryptoValuesModuleAssembler;
import org.codeartisans.qipki.crypto.assembly.CryptoEngineModuleAssembler;
import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.ApplicationAssembler;
import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.ApplicationAssemblyFactory;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.LayerAssembly;
import org.qi4j.bootstrap.ModuleAssembly;

@SuppressWarnings( "unchecked" )
public class QiPkiCaAssembler
        implements ApplicationAssembler
{

    @Override
    public final ApplicationAssembly assemble( ApplicationAssemblyFactory applicationFactory )
            throws AssemblyException
    {

        ApplicationAssembly app = applicationFactory.newApplicationAssembly();

        app.setName( AssemblyNames.APPLICATION_NAME );
        app.setVersion( AssemblyNames.APPLICATION_VERSION );

        LayerAssembly presentation = app.layerAssembly( AssemblyNames.LAYER_PRESENTATION );
        {
            assembleDevTestModule( presentation.moduleAssembly( AssemblyNames.MODULE_TESTS_IN_PRESENTATION ) );

            new RestApiModuleAssembler().assemble(
                    presentation.moduleAssembly( AssemblyNames.MODULE_REST_API ) );

            new HttpModuleAssembler().assemble(
                    presentation.moduleAssembly( AssemblyNames.MODULE_HTTP ) );

            new TransientConfigurationModuleAssembler( Visibility.layer ).assemble(
                    presentation.moduleAssembly( AssemblyNames.MODULE_CONFIGURATION ) );

        }

        LayerAssembly application = app.layerAssembly( AssemblyNames.LAYER_APPLICATION );
        {
            new CaDCIModuleAssembler().assemble(
                    application.moduleAssembly( AssemblyNames.MODULE_CA_DCI ) );
        }

        LayerAssembly domain = app.layerAssembly( AssemblyNames.LAYER_DOMAIN );
        {
            new CaDomainModuleAssembler().assemble(
                    domain.moduleAssembly( AssemblyNames.MODULE_CA_DOMAIN ) );
        }

        LayerAssembly crypto = app.layerAssembly( AssemblyNames.LAYER_CRYPTO );
        {
            new CryptoEngineModuleAssembler( Visibility.application ).assemble(
                    crypto.moduleAssembly( AssemblyNames.MODULE_CRYPTO_ENGINE ) );
            new CryptoValuesModuleAssembler( Visibility.application ).assemble(
                    crypto.moduleAssembly( AssemblyNames.MODULE_CRYPTO_VALUES ) );
        }

        LayerAssembly infrastructure = app.layerAssembly( AssemblyNames.LAYER_INFRASTRUCTURE );
        {
            // TODO Add MessagingModule and make it short as qi4j will implement Message type anytime not so soon :)
            new InMemoryStoreAndFinderModuleAssembler( Visibility.application ).assemble(
                    infrastructure.moduleAssembly( AssemblyNames.MODULE_PERSISTENCE ) );
        }

        presentation.uses( application, crypto );
        application.uses( domain, crypto );
        domain.uses( crypto, infrastructure );

        return app;
    }

    protected void assembleDevTestModule( ModuleAssembly devTestModule )
            throws AssemblyException
    {
    }

}
