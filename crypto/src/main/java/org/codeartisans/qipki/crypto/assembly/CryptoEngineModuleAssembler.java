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
package org.codeartisans.qipki.crypto.assembly;

import org.codeartisans.qipki.crypto.QiCryptoEngine;
import org.codeartisans.qipki.crypto.asymetric.AsymetricGeneratorService;
import org.codeartisans.qipki.crypto.cipher.CipherFactoryService;
import org.codeartisans.qipki.crypto.codec.CryptCodexService;
import org.codeartisans.qipki.crypto.digest.DigestService;
import org.codeartisans.qipki.crypto.io.CryptIOService;
import org.codeartisans.qipki.crypto.mac.MACService;
import org.codeartisans.qipki.crypto.objects.KeyInformation;
import org.codeartisans.qipki.crypto.objects.CryptObjectsFactory;
import org.codeartisans.qipki.crypto.random.RandomService;
import org.codeartisans.qipki.crypto.symetric.SymetricGeneratorService;
import org.codeartisans.qipki.crypto.x509.X509ExtensionsBuilderService;
import org.codeartisans.qipki.crypto.x509.X509ExtensionsReaderService;
import org.codeartisans.qipki.crypto.x509.X509GeneratorService;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;

public class CryptoEngineModuleAssembler
        implements Assembler
{

    private final Visibility visibility;

    public CryptoEngineModuleAssembler()
    {
        this( Visibility.module );
    }

    public CryptoEngineModuleAssembler( Visibility visibility )
    {
        this.visibility = visibility;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        module.addServices( CryptObjectsFactory.class,
                            CryptCodexService.class,
                            RandomService.class,
                            X509GeneratorService.class,
                            CryptIOService.class,
                            DigestService.class,
                            MACService.class,
                            SymetricGeneratorService.class,
                            AsymetricGeneratorService.class,
                            CipherFactoryService.class,
                            X509ExtensionsReaderService.class,
                            X509ExtensionsBuilderService.class ).
                visibleIn( visibility );

        module.addObjects( KeyInformation.class ).
                visibleIn( visibility );

        module.addServices( QiCryptoEngine.class ).
                visibleIn( Visibility.module ).
                instantiateOnStartup();
    }

}
