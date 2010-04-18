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
package org.codeartisans.qipki.core.crypto.tools;

import java.security.PublicKey;
import org.codeartisans.qipki.core.crypto.KeyInformation;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.qi4j.api.service.ServiceComposite;

@Mixins( CryptoToolFactory.Mixin.class )
public interface CryptoToolFactory
        extends ServiceComposite
{

    CryptIO newCryptIOInstance();

    CryptGEN newCryptGENInstance();

    CryptCodex newCryptCodexInstance();

    X509ExtensionsReader newX509ExtensionsReaderInstance();

    X509ExtensionsBuilder newX509ExtensionsBuilderInstance();

    KeyInformation newKeyInformationInstance( PublicKey publicKey );

    abstract class Mixin
            implements CryptoToolFactory
    {

        @Structure
        private ObjectBuilderFactory obf;

        @Override
        public CryptIO newCryptIOInstance()
        {
            return obf.newObject( CryptIO.class );
        }

        @Override
        public CryptGEN newCryptGENInstance()
        {
            return obf.newObject( CryptGEN.class );
        }

        @Override
        public CryptCodex newCryptCodexInstance()
        {
            return obf.newObject( CryptCodex.class );
        }

        @Override
        public X509ExtensionsReader newX509ExtensionsReaderInstance()
        {
            return obf.newObjectBuilder( X509ExtensionsReader.class ).use( newCryptCodexInstance() ).newInstance();
        }

        @Override
        public X509ExtensionsBuilder newX509ExtensionsBuilderInstance()
        {
            return obf.newObject( X509ExtensionsBuilder.class );
        }

        @Override
        public KeyInformation newKeyInformationInstance( PublicKey publicKey )
        {
            return obf.newObjectBuilder( KeyInformation.class ).use( publicKey ).newInstance();
        }

    }

}
