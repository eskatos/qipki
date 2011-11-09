/*
 * Copyright (c) 2010, Paul Merlin. All Rights Reserved.
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
package org.qipki.client.ca.bootstrap;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;

import org.qipki.client.ca.QiPkiCaHttpClientConfiguration;
import org.qipki.client.ca.api.CAClientService;
import org.qipki.client.ca.api.CryptoStoreClientService;
import org.qipki.client.ca.api.QiPkiHttpCaClientService;
import org.qipki.client.ca.spi.RestClientService;
import org.qipki.commons.bootstrap.CryptoValuesModuleAssembler;
import org.qipki.commons.bootstrap.RestValuesModuleAssembler;
import org.qipki.crypto.bootstrap.CryptoEngineModuleAssembler;

@SuppressWarnings( "unchecked" )
public class QiPkiCaClientAssembler
        implements Assembler
{

    private final Visibility visibility;
    private ModuleAssembly configModule;
    private Visibility configVisibility = Visibility.layer;

    public QiPkiCaClientAssembler()
    {
        visibility = Visibility.module;
    }

    public QiPkiCaClientAssembler( Visibility visibility )
    {
        this.visibility = visibility;
    }

    public QiPkiCaClientAssembler withConfigModule( ModuleAssembly configModule )
    {
        this.configModule = configModule;
        return this;
    }

    public QiPkiCaClientAssembler withConfigVisibility( Visibility configVisibility )
    {
        this.configVisibility = configVisibility;
        return this;
    }

    @Override
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        new CryptoEngineModuleAssembler( visibility ).withConfigModule( configModule ).withConfigVisibility( configVisibility ).assemble( module );
        new CryptoValuesModuleAssembler( visibility ).assemble( module );
        new RestValuesModuleAssembler( visibility ).assemble( module );

        module.addServices( RestClientService.class ).
                visibleIn( Visibility.module );

        module.addServices( QiPkiHttpCaClientService.class,
                            CryptoStoreClientService.class,
                            CAClientService.class ).
                visibleIn( visibility );

        configModule.entities( QiPkiCaHttpClientConfiguration.class );

    }

}
