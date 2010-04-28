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
package org.codeartisans.qipki.client.ca;

import org.codeartisans.qipki.client.ca.services.RestClientService;
import org.codeartisans.qipki.client.ca.services.CAClientService;
import org.codeartisans.qipki.client.ca.services.CryptoStoreClientService;
import org.codeartisans.qipki.commons.QiPkiCommonsValuesAssembler;
import org.codeartisans.qipki.crypto.QiCryptoAssembler;
import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;

public class QiPkiCaClientAssembler
        implements Assembler
{

    private final Visibility visibility;

    public QiPkiCaClientAssembler()
    {
        visibility = Visibility.module;
    }

    public QiPkiCaClientAssembler( Visibility visibility )
    {
        this.visibility = visibility;
    }

    @Override
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        new QiCryptoAssembler( visibility ).assemble( module );

        new QiPkiCommonsValuesAssembler( visibility ).assemble( module );

        module.addServices( RestClientService.class ).
                visibleIn( Visibility.module );

        module.addServices( CryptoStoreClientService.class,
                            CAClientService.class ).
                visibleIn( visibility );
    }

}
