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
package org.codeartisans.qipki.commons;

import org.codeartisans.qipki.commons.values.rest.RestListValue;
import org.codeartisans.qipki.commons.values.params.CAFactoryParamsValue;
import org.codeartisans.qipki.commons.values.rest.CAValue;
import org.codeartisans.qipki.commons.values.params.CryptoStoreFactoryParamsValue;
import org.codeartisans.qipki.commons.values.params.ParamsFactory;
import org.codeartisans.qipki.commons.values.params.X509FactoryParamsValue;
import org.codeartisans.qipki.commons.values.params.X509RevocationParamsValue;
import org.codeartisans.qipki.commons.values.rest.ApiURIsValue;
import org.codeartisans.qipki.commons.values.rest.CryptoStoreValue;
import org.codeartisans.qipki.commons.values.rest.RevocationValue;
import org.codeartisans.qipki.commons.values.rest.X509DetailValue;
import org.codeartisans.qipki.commons.values.rest.X509Value;
import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;

@SuppressWarnings( "unchecked" )
public class QiPkiRestValuesAssembler
        implements Assembler
{

    private final Visibility visibility;

    public QiPkiRestValuesAssembler()
    {
        this( Visibility.module );
    }

    public QiPkiRestValuesAssembler( Visibility visibility )
    {
        this.visibility = visibility;
    }

    @Override
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        // Params
        module.addValues( CryptoStoreFactoryParamsValue.class,
                          CAFactoryParamsValue.class,
                          X509FactoryParamsValue.class,
                          X509RevocationParamsValue.class ).
                visibleIn( visibility );
        module.addServices( ParamsFactory.class ).
                visibleIn( visibility );

        // Crypto values
        new QiPkiCryptoValuesAssembler( visibility ).assemble( module );

        // Rest values
        module.addValues( RestListValue.class,
                          ApiURIsValue.class,
                          CryptoStoreValue.class,
                          CAValue.class,
                          X509Value.class,
                          X509DetailValue.class,
                          RevocationValue.class ).
                visibleIn( visibility );

    }

}
