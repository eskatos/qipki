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
package org.codeartisans.qipki.crypto;

import org.codeartisans.qipki.crypto.asymetric.AsymetricGeneratorService;
import org.codeartisans.qipki.crypto.codec.CryptCodexService;
import org.codeartisans.qipki.crypto.digest.DigestService;
import org.codeartisans.qipki.crypto.io.CryptIOService;
import org.codeartisans.qipki.crypto.mac.MACService;
import org.codeartisans.qipki.crypto.objects.KeyInformation;
import org.codeartisans.qipki.crypto.objects.CryptObjectsFactory;
import org.codeartisans.qipki.crypto.x509.X509ExtensionsBuilderService;
import org.codeartisans.qipki.crypto.x509.X509ExtensionsReaderService;
import org.codeartisans.qipki.crypto.x509.X509GeneratorService;
import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;

public class QiCryptoAssembler
        implements Assembler
{

    private final Visibility visibility;

    public QiCryptoAssembler()
    {
        this( Visibility.module );
    }

    public QiCryptoAssembler( Visibility visibility )
    {
        this.visibility = visibility;
    }

    @Override
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        module.addServices( CryptObjectsFactory.class,
                            CryptCodexService.class,
                            X509GeneratorService.class,
                            CryptIOService.class,
                            DigestService.class,
                            MACService.class,
                            AsymetricGeneratorService.class,
                            X509ExtensionsReaderService.class,
                            X509ExtensionsBuilderService.class ).
                visibleIn( visibility );

        module.addObjects( KeyInformation.class ).
                visibleIn( visibility );

        module.addServices( QiCryptoActivator.class ).
                visibleIn( Visibility.module ).
                instantiateOnStartup();
    }

}
