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

import org.codeartisans.qipki.ca.application.contexts.ca.CAContext;
import org.codeartisans.qipki.ca.application.contexts.ca.CAListContext;
import org.codeartisans.qipki.ca.application.contexts.cryptostore.CryptoStoreContext;
import org.codeartisans.qipki.ca.application.contexts.cryptostore.CryptoStoreListContext;
import org.codeartisans.qipki.ca.application.contexts.RootContext;
import org.codeartisans.qipki.ca.application.contexts.x509.X509Context;
import org.codeartisans.qipki.ca.application.contexts.x509.X509ListContext;
import org.codeartisans.qipki.ca.domain.ca.CAFactory;
import org.codeartisans.qipki.ca.domain.ca.CARepository;
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
import org.codeartisans.qipki.ca.presentation.http.HttpService;
import org.codeartisans.qipki.ca.presentation.http.RootServletService;
import org.codeartisans.qipki.ca.presentation.rest.RestletApplication;
import org.codeartisans.qipki.ca.presentation.rest.RestletFinder;
import org.codeartisans.qipki.ca.presentation.rest.RestletServletServerService;
import org.codeartisans.qipki.ca.presentation.rest.RestletValuesFactory;
import org.codeartisans.qipki.ca.presentation.rest.resources.ApiRootResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.ca.CAFactoryResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.ca.CAListResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.ca.CAResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.ca.CRLResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.x509.X509ListResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.cryptostore.CryptoStoreFactoryResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.cryptostore.CryptoStoreListResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.cryptostore.CryptoStoreResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.x509.X509DetailResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.x509.X509FactoryResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.x509.X509Resource;
import org.codeartisans.qipki.ca.presentation.rest.resources.x509.X509RevocationResource;
import org.codeartisans.qipki.commons.QiPkiCommonsValuesAssembler;
import org.codeartisans.qipki.commons.values.crypto.CryptoValuesFactory;
import org.codeartisans.qipki.commons.values.crypto.ValidityIntervalValue;
import org.codeartisans.qipki.crypto.QiCryptoAssembler;
import org.codeartisans.qipki.core.sideeffects.TracingSideEffect;
import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.ApplicationAssembler;
import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.ApplicationAssemblyFactory;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.LayerAssembly;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.entitystore.memory.MemoryEntityStoreService;
import org.qi4j.index.rdf.assembly.RdfMemoryStoreAssembler;
import org.qi4j.library.http.JettyConfiguration;
import static org.qi4j.library.http.Servlets.*;
import org.qi4j.spi.uuid.UuidIdentityGeneratorService;

@SuppressWarnings( "unchecked" )
public class QiPkiCaAssembler
        implements ApplicationAssembler
{

    @Override
    public final ApplicationAssembly assemble( ApplicationAssemblyFactory applicationFactory )
            throws AssemblyException
    {

        ApplicationAssembly app = applicationFactory.newApplicationAssembly();

        app.setName( "QiPKIServer" );
        app.setVersion( "1.0-SNAPSHOT" );

        LayerAssembly presentation = app.layerAssembly( "presentation" );
        {
            assembleDevTestModule( presentation.moduleAssembly( "test" ) );

            new Assembler() // REST
            {

                @Override
                public void assemble( ModuleAssembly module )
                        throws AssemblyException
                {
                    module.addObjects( RestletApplication.class ).
                            visibleIn( Visibility.layer );

                    module.addObjects( RestletFinder.class,
                                       ApiRootResource.class,
                                       CryptoStoreListResource.class,
                                       CryptoStoreFactoryResource.class,
                                       CryptoStoreResource.class,
                                       CAListResource.class,
                                       CAFactoryResource.class,
                                       CAResource.class,
                                       CRLResource.class,
                                       X509ListResource.class,
                                       X509FactoryResource.class,
                                       X509Resource.class,
                                       X509DetailResource.class,
                                       X509RevocationResource.class ).
                            visibleIn( Visibility.module );

                    new QiPkiCommonsValuesAssembler( Visibility.layer ).assemble( module );

                    module.addServices( RestletValuesFactory.class ).
                            visibleIn( Visibility.module );

                    addServlets( serve( "/api/*" ).with( RestletServletServerService.class ) ).
                            to( module );

                    // Not needed, see RestletApplication
                    // addFilters( filter( "/api/*" ).through( UnitOfWorkFilterService.class ).on( REQUEST ) ).to( module );
                }

            }.assemble( presentation.moduleAssembly( "rest" ) );

            new Assembler() // Http Service
            {

                // NOTE Servlets are added with layer visibility
                @Override
                public void assemble( ModuleAssembly module )
                        throws AssemblyException
                {
                    module.addServices( HttpService.class ).
                            visibleIn( Visibility.module ).
                            instantiateOnStartup();

                    addServlets( serve( "/" ).with( RootServletService.class ) ).
                            to( module );
                }

            }.assemble( presentation.moduleAssembly( "http" ) );

            new Assembler() // UI Configuration
            {

                @Override
                public void assemble( ModuleAssembly module )
                        throws AssemblyException
                {
                    module.addServices( MemoryEntityStoreService.class ).
                            visibleIn( Visibility.layer );

                    module.addEntities( JettyConfiguration.class ).
                            visibleIn( Visibility.layer );
                }

            }.assemble( presentation.moduleAssembly( "config" ) );

        }

        LayerAssembly application = app.layerAssembly( "application" );
        {
            new Assembler()
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
                                       X509ListContext.class,
                                       X509Context.class ).
                            visibleIn( Visibility.application );
                }

            }.assemble( application.moduleAssembly( "dci" ) );
        }

        LayerAssembly domain = app.layerAssembly( "domain" );
        {
            new Assembler() // PKI/CA Domain
            {

                @Override
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
                                        X509Entity.class,
                                        RevocationEntity.class ).
                            visibleIn( Visibility.application );

                    // Services
                    module.addServices( CryptoStoreRepository.class,
                                        CryptoStoreFactory.class,
                                        CARepository.class,
                                        CAFactory.class,
                                        CRLFactory.class,
                                        X509Repository.class,
                                        X509Factory.class,
                                        RevocationFactory.class,
                                        CryptoValuesFactory.class ).
                            visibleIn( Visibility.application ).
                            withSideEffects( TracingSideEffect.class );
                }

            }.assemble( domain.moduleAssembly( "domain" ) );

        }

        LayerAssembly crypto = app.layerAssembly( "crypto" );
        {
            new QiCryptoAssembler( Visibility.application ).assemble( crypto.moduleAssembly( "crypto-tools" ) );
        }

        LayerAssembly infrastructure = app.layerAssembly( "infrastructure" );
        {
            new Assembler() // Store
            {

                @Override
                public void assemble( ModuleAssembly module )
                        throws AssemblyException
                {
                    module.addServices( MemoryEntityStoreService.class,
                                        UuidIdentityGeneratorService.class ).
                            visibleIn( Visibility.application );
                    new RdfMemoryStoreAssembler( null, Visibility.application, Visibility.application ).assemble( module );
                }

            }.assemble( infrastructure.moduleAssembly( "store" ) );
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
