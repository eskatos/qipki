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
package org.qipki.crypto.bootstrap;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;

import org.qipki.crypto.QiCryptoConfiguration;
import org.qipki.crypto.QiCryptoEngine;
import org.qipki.crypto.asymetric.AsymetricGeneratorService;
import org.qipki.crypto.cipher.CipherFactoryService;
import org.qipki.crypto.codec.CryptCodexService;
import org.qipki.crypto.digest.DigesterService;
import org.qipki.crypto.io.CryptIOService;
import org.qipki.crypto.jce.JceDetectorService;
import org.qipki.crypto.mac.MACService;
import org.qipki.crypto.objects.KeyInformation;
import org.qipki.crypto.objects.CryptObjectsFactory;
import org.qipki.crypto.symetric.SymetricGeneratorService;
import org.qipki.crypto.x509.X509ExtensionsBuilderService;
import org.qipki.crypto.x509.X509ExtensionsReaderService;
import org.qipki.crypto.x509.X509GeneratorService;

public class CryptoEngineModuleAssembler
        implements Assembler
{

    private final Visibility visibility;
    private ModuleAssembly configModule;
    private Visibility configVisibility = Visibility.layer;

    public CryptoEngineModuleAssembler()
    {
        this( Visibility.module );
    }

    public CryptoEngineModuleAssembler( Visibility visibility )
    {
        this.visibility = visibility;
    }

    public CryptoEngineModuleAssembler( Visibility visibility, ModuleAssembly configModule, Visibility configVisibility )
    {
        this.visibility = visibility;
        this.configModule = configModule;
        this.configVisibility = configVisibility;
    }

    public CryptoEngineModuleAssembler withConfigModule( ModuleAssembly configModule )
    {
        this.configModule = configModule;
        return this;
    }

    public CryptoEngineModuleAssembler withConfigVisibility( Visibility configVisibility )
    {
        this.configVisibility = configVisibility;
        return this;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        if ( configModule == null ) {
            configModule = module;
        }
        onAssemble( module, visibility, configModule, configVisibility );
    }

    private void onAssemble( ModuleAssembly module, Visibility visibility, ModuleAssembly configModule, Visibility configVisibility )
    {
        module.services( JceDetectorService.class,
                         CryptObjectsFactory.class,
                         CryptCodexService.class,
                         X509GeneratorService.class,
                         CryptIOService.class,
                         DigesterService.class,
                         MACService.class,
                         SymetricGeneratorService.class,
                         AsymetricGeneratorService.class,
                         CipherFactoryService.class,
                         X509ExtensionsReaderService.class,
                         X509ExtensionsBuilderService.class ).
                visibleIn( visibility );

        module.objects( KeyInformation.class ).
                visibleIn( visibility );

        module.services( QiCryptoEngine.class ).
                visibleIn( Visibility.module ).
                instantiateOnStartup();

        configModule.entities( QiCryptoConfiguration.class ).visibleIn( configVisibility );
    }

}
