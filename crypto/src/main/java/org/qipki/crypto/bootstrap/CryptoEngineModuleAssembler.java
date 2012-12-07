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
import org.qipki.crypto.asymetric.AsymetricGenerator;
import org.qipki.crypto.asymetric.AsymetricGeneratorImpl;
import org.qipki.crypto.cipher.CipherFactory;
import org.qipki.crypto.cipher.CipherFactoryImpl;
import org.qipki.crypto.codec.CryptCodex;
import org.qipki.crypto.codec.CryptCodexImpl;
import org.qipki.crypto.digest.Digester;
import org.qipki.crypto.digest.DigesterImpl;
import org.qipki.crypto.io.CryptIO;
import org.qipki.crypto.io.CryptIOImpl;
import org.qipki.crypto.jce.JceDetector;
import org.qipki.crypto.jce.JceDetectorImpl;
import org.qipki.crypto.mac.MAC;
import org.qipki.crypto.mac.MACImpl;
import org.qipki.crypto.objects.CryptObjectsFactory;
import org.qipki.crypto.objects.KeyInformation;
import org.qipki.crypto.symetric.SymetricGenerator;
import org.qipki.crypto.symetric.SymetricGeneratorImpl;
import org.qipki.crypto.x509.X509ExtensionsBuilder;
import org.qipki.crypto.x509.X509ExtensionsBuilderImpl;
import org.qipki.crypto.x509.X509ExtensionsReader;
import org.qipki.crypto.x509.X509ExtensionsReaderImpl;
import org.qipki.crypto.x509.X509Generator;
import org.qipki.crypto.x509.X509GeneratorImpl;

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
        if( configModule == null )
        {
            configModule = module;
        }
        onAssemble( module, visibility, configModule, configVisibility );
    }

    private void onAssemble( ModuleAssembly module, Visibility visibility, ModuleAssembly configModule, Visibility configVisibility )
        throws AssemblyException
    {
        module.services( JceDetector.class ).withMixins( JceDetectorImpl.class ).visibleIn( visibility );
        module.services( CryptCodex.class ).withMixins( CryptCodexImpl.class ).visibleIn( visibility );
        module.services( X509Generator.class ).withMixins( X509GeneratorImpl.class ).visibleIn( visibility );
        module.services( CryptIO.class ).withMixins( CryptIOImpl.class ).visibleIn( visibility );
        module.services( Digester.class ).withMixins( DigesterImpl.class ).visibleIn( visibility );
        module.services( MAC.class ).withMixins( MACImpl.class ).visibleIn( visibility );
        module.services( SymetricGenerator.class ).withMixins( SymetricGeneratorImpl.class ).visibleIn( visibility );
        module.services( AsymetricGenerator.class ).withMixins( AsymetricGeneratorImpl.class ).visibleIn( visibility );
        module.services( CipherFactory.class ).withMixins( CipherFactoryImpl.class ).visibleIn( visibility );
        module.services( X509ExtensionsReader.class ).withMixins( X509ExtensionsReaderImpl.class ).visibleIn( visibility );
        module.services( X509ExtensionsBuilder.class ).withMixins( X509ExtensionsBuilderImpl.class ).visibleIn( visibility );

        module.services( CryptObjectsFactory.class ).visibleIn( visibility );

        module.objects( KeyInformation.class ).
            visibleIn( visibility );

        module.services( QiCryptoEngine.class ).
            visibleIn( Visibility.module ).
            instantiateOnStartup();

        configModule.entities( QiCryptoConfiguration.class ).visibleIn( configVisibility );
    }

}
