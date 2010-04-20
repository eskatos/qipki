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
package org.codeartisans.qipki.ca.domain.cryptostore;

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import org.codeartisans.qipki.core.crypto.tools.CryptIO;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.This;

public class CryptoStoreMixin
        implements CryptoStoreBehavior
{

    @Service
    private CryptIO cryptIO;
    @This
    private CryptoStoreEntity state;

    @Override
    public KeyStore loadKeyStore()
    {
        return cryptIO.base64DecodeKeyStore( state.payload().get(),
                                             state.storeType().get(),
                                             state.password().get() );
    }

    @Override
    public String storeCertificate( X509Certificate certificate )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public void storeCertificate( String slotId, X509Certificate certificate )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public String storeKeyPair( KeyPair keyPair )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public void storeKeyPair( String slotId, KeyPair keyPair )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

}
