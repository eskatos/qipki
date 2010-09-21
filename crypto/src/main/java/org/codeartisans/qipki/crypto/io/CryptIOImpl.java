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
package org.codeartisans.qipki.crypto.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.util.encoders.Base64;

import org.codeartisans.qipki.crypto.QiCryptoFailure;
import org.codeartisans.qipki.crypto.constants.IOConstants;
import org.codeartisans.qipki.crypto.storage.KeyStoreType;

public class CryptIOImpl
        implements CryptIO
{

    @Override
    public KeyStore createEmptyKeyStore( KeyStoreType storeType )
    {
        try {
            KeyStore keystore = getKeyStoreInstance( storeType );
            keystore.load( null, null );
            return keystore;
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to create empty" + storeType + " KeyStore", ex );
        } catch ( GeneralSecurityException ex ) {
            throw new QiCryptoFailure( "Unable to create empty" + storeType + " KeyStore", ex );
        }
    }

    @Override
    public String base64Encode( KeyStore keystore, char[] password )
    {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            keystore.store( baos, password );
            baos.flush();
            return new String( Base64.encode( baos.toByteArray() ), IOConstants.UTF_8 );
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to Base64 encode KeyStore", ex );
        } catch ( GeneralSecurityException ex ) {
            throw new QiCryptoFailure( "Unable to Base64 encode KeyStore", ex );
        }
    }

    @Override
    public KeyStore base64DecodeKeyStore( String payload, KeyStoreType storeType, char[] password )
    {
        try {
            KeyStore keystore = getKeyStoreInstance( storeType );
            keystore.load( new ByteArrayInputStream( Base64.decode( payload.getBytes( IOConstants.UTF_8 ) ) ), password );
            return keystore;
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to Base64 decode KeyStore", ex );
        } catch ( GeneralSecurityException ex ) {
            throw new QiCryptoFailure( "Unable to Base64 decode KeyStore", ex );
        }
    }

    @Override
    public X509Certificate readX509PEM( StringReader reader )
    {
        try {
            return ( X509Certificate ) new PEMReader( reader ).readObject();
        } catch ( IOException ex ) {
            throw new IllegalArgumentException( "Unable to read X509Certificate from PEM", ex );
        }
    }

    @Override
    public PKCS10CertificationRequest readPKCS10PEM( Reader reader )
    {
        try {
            return ( PKCS10CertificationRequest ) new PEMReader( reader ).readObject();
        } catch ( IOException ex ) {
            throw new IllegalArgumentException( "Unable to read PKCS#10 from PEM", ex );
        }
    }

    @Override
    public X509CRL readCRLPEM( Reader reader )
    {
        try {
            return ( X509CRL ) new PEMReader( reader ).readObject();
        } catch ( IOException ex ) {
            throw new IllegalArgumentException( "Unable to read CRL from PEM", ex );
        }
    }

    @Override
    public KeyPair readKeyPairPEM( Reader reader )
    {
        try {
            return ( KeyPair ) new PEMReader( reader ).readObject();
        } catch ( IOException ex ) {
            throw new IllegalArgumentException( "Unable to read KeyPair from PEM", ex );
        }
    }

    @Override
    public CharSequence asPEM( X509Certificate certificate )
    {
        return asPEM( certificate.getClass().getSimpleName(), certificate );
    }

    @Override
    public CharSequence asPEM( PKCS10CertificationRequest pkcs10 )
    {
        return asPEM( pkcs10.getClass().getSimpleName(), pkcs10 );
    }

    @Override
    public CharSequence asPEM( X509CRL x509CRL )
    {
        return asPEM( x509CRL.getClass().getSimpleName(), x509CRL );
    }

    @Override
    public CharSequence asPEM( PublicKey pubKey )
    {
        return asPEM( pubKey.getClass().getSimpleName(), pubKey );
    }

    @Override
    public CharSequence asPEM( KeyPair keyPair )
    {
        return asPEM( keyPair.getClass().getSimpleName(), keyPair );
    }

    @Override
    public CharSequence asPEM( KeyPair keyPair, char[] password )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    private CharSequence asPEM( String ilk, Object object )
    {
        try {
            StringWriter sw = new StringWriter();
            PEMWriter pemWriter = new PEMWriter( sw, BouncyCastleProvider.PROVIDER_NAME );
            pemWriter.writeObject( object );
            pemWriter.flush();
            return sw.getBuffer();
        } catch ( IOException ex ) {
            throw new QiCryptoFailure( "Unable to write " + ilk + " as PEM", ex );
        }
    }

    private KeyStore getKeyStoreInstance( KeyStoreType storeType )
            throws KeyStoreException, NoSuchProviderException
    {
        if ( KeyStoreType.PKCS12 == storeType ) {
            return KeyStore.getInstance( storeType.typeString(), BouncyCastleProvider.PROVIDER_NAME );
        }
        return KeyStore.getInstance( storeType.typeString() );
    }

}
