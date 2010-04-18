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
package org.codeartisans.qipki.core.crypto;

import org.codeartisans.qipki.commons.QiPkiCryptoValuesAssembler;
import org.codeartisans.qipki.core.crypto.tools.CryptGEN;
import org.codeartisans.qipki.core.crypto.tools.X509ExtensionsBuilder;
import org.codeartisans.qipki.core.crypto.tools.CryptIO;
import org.codeartisans.qipki.core.crypto.tools.X509ExtensionsReader;
import org.codeartisans.qipki.core.crypto.tools.CryptoToolFactory;
import org.codeartisans.qipki.core.crypto.tools.CryptCodex;
import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;

public class CryptoAssembler
        implements Assembler
{

    private final Visibility visibility;

    public CryptoAssembler( Visibility visibility )
    {
        this.visibility = visibility;
    }

    @Override
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        module.addServices( CryptoToolFactory.class,
                            X509ExtensionsValueFactory.class ).
                visibleIn( visibility );
        module.addObjects( CryptIO.class,
                           CryptGEN.class,
                           CryptCodex.class,
                           X509ExtensionsReader.class,
                           X509ExtensionsBuilder.class,
                           KeyInformation.class ).
                visibleIn( visibility );
        new QiPkiCryptoValuesAssembler( visibility ).assemble( module );
    }

}
