/*
 * Copyright (c) 2012, Paul Merlin. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.qipki.ca.tests.embedded;

import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.qi4j.api.common.UseDefaults;
import org.qi4j.api.common.Visibility;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.property.Property;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.structure.Module;
import org.qi4j.api.value.ValueBuilder;
import org.qi4j.api.value.ValueComposite;
import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.LayerAssembly;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.test.AbstractQi4jTest;
import org.qipki.commons.crypto.values.HasCriticality;
import org.qipki.crypto.x509.ExtendedKeyUsage;

public class ValueLookupTest
        extends AbstractQi4jTest
{

    @Override
    public void assemble( ModuleAssembly testModule )
            throws AssemblyException
    {
        LayerAssembly testLayer = testModule.layer();
        ApplicationAssembly app = testLayer.application();

        LayerAssembly presentationLayer = app.layer( "presentation" );
        LayerAssembly appLayer = app.layer( "app" );
        LayerAssembly domainLayer = app.layer( "domain" );
        LayerAssembly cryptoLayer = app.layer( "crypto" );
        LayerAssembly configLayer = app.layer( "config" );
        LayerAssembly infraLayer = app.layer( "infra" );

        ModuleAssembly presentationModule = presentationLayer.module( "presentation" );
        ModuleAssembly appModule = appLayer.module( "app" );
        ModuleAssembly domainModule = domainLayer.module( "domain" );
        ModuleAssembly cryptoModule = cryptoLayer.module( "crypto" );
        ModuleAssembly configModule = configLayer.module( "config" );
        ModuleAssembly infraModule = infraLayer.module( "infra" );

        cryptoModule.values( SomeCryptoValue.class ).visibleIn( Visibility.application );
        presentationModule.services( SomePresentationService.class );

        presentationLayer.uses( appLayer, cryptoLayer, configLayer );
        appLayer.uses( domainLayer, cryptoLayer, configLayer );
        domainLayer.uses( cryptoLayer, configLayer, infraLayer );
        cryptoLayer.uses( configLayer );
        infraLayer.uses( configLayer );
    }

    private Module presentationModule;

    @Before
    public void before()
    {
        presentationModule = application.findModule( "presentation", "presentation" );
        Module appModule = application.findModule( "app", "app" );
        Module domainModule = application.findModule( "domain", "domain" );
        Module cryptoModule = application.findModule( "crypto", "crypto" );
        Module configModule = application.findModule( "config", "config" );
        Module infraModule = application.findModule( "infra", "infra" );
    }

    public interface SomeCryptoValue
            extends HasCriticality, ValueComposite
    {

        @UseDefaults
        Property<Set<ExtendedKeyUsage>> extendedKeyUsages();

    }

    @Mixins( SomePresentationServiceImpl.class )
    public interface SomePresentationService
            extends ServiceComposite
    {

        SomeCryptoValue doPresentationSomething();

    }

    public static abstract class SomePresentationServiceImpl
            implements SomePresentationService
    {

        @Structure
        Module module;

        @Override
        public SomeCryptoValue doPresentationSomething()
        {
            ValueBuilder<SomeCryptoValue> builder = module.newValueBuilder( SomeCryptoValue.class );
            SomeCryptoValue value = builder.prototype();
            value.critical().set( Boolean.TRUE );
            return builder.newInstance();
        }

    }

    @Test
    public void testFromPresentation()
    {
        SomePresentationService someService = presentationModule.<SomePresentationService>findService( SomePresentationService.class ).get();
        SomeCryptoValue value = someService.doPresentationSomething();
        System.out.println( value );

        String json = "{\"critical\":true,\"extendedKeyUsages\":[]}";
        SomeCryptoValue valued = presentationModule.newValueFromJSON( SomeCryptoValue.class, json );
        System.out.println( valued );
    }

}
