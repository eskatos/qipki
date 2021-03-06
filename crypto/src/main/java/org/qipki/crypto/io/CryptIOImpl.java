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
package org.qipki.crypto.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
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
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.util.encoders.Base64;
import org.codeartisans.java.toolbox.io.IO;
import org.qi4j.api.injection.scope.Service;
import org.qipki.crypto.CryptoContext;
import org.qipki.crypto.CryptoFailure;
import org.qipki.crypto.constants.IOConstants;
import org.qipki.crypto.storage.KeyStoreType;

public class CryptIOImpl
    implements CryptIO
{

    private final CryptoContext cryptoContext;

    public CryptIOImpl( @Service CryptoContext cryptoContext )
    {
        this.cryptoContext = cryptoContext;
    }

    @Override
    public KeyStore createEmptyKeyStore( KeyStoreType storeType )
    {
        try
        {
            KeyStore keystore = getKeyStoreInstance( storeType );
            keystore.load( null, null );
            return keystore;
        }
        catch( IOException ex )
        {
            throw new CryptoFailure( "Unable to create empty " + storeType + " KeyStore", ex );
        }
        catch( GeneralSecurityException ex )
        {
            throw new CryptoFailure( "Unable to create empty " + storeType + " KeyStore", ex );
        }
    }

    @Override
    public void writeKeyStore( KeyStore keystore, char[] password, File file )
    {
        FileOutputStream output = null;
        try
        {
            output = new FileOutputStream( file );
            keystore.store( output, password );
            output.flush();
        }
        catch( IOException ex )
        {
            throw new CryptoFailure( "Unable to store KeyStore in " + file, ex );
        }
        catch( GeneralSecurityException ex )
        {
            throw new CryptoFailure( "Unable to store KeyStore in " + file, ex );
        }
        finally
        {
            IO.closeSilently( output );
        }
    }

    @Override
    public KeyStore readKeyStore( File file, KeyStoreType storeType, char[] password )
    {
        FileInputStream input = null;
        try
        {
            input = new FileInputStream( file );
            KeyStore keystore = getKeyStoreInstance( storeType );
            keystore.load( input, password );
            return keystore;
        }
        catch( IOException ex )
        {
            throw new CryptoFailure( "Unable to load KeyStore from " + file, ex );
        }
        catch( GeneralSecurityException ex )
        {
            throw new CryptoFailure( "Unable to load KeyStore from " + file, ex );
        }
        finally
        {
            IO.closeSilently( input );
        }
    }

    @Override
    public String base64Encode( KeyStore keystore, char[] password )
    {
        ByteArrayOutputStream baos = null;
        try
        {
            baos = new ByteArrayOutputStream();
            keystore.store( baos, password );
            baos.flush();
            return new String( Base64.encode( baos.toByteArray() ), IOConstants.UTF_8 );
        }
        catch( IOException ex )
        {
            throw new CryptoFailure( "Unable to Base64 encode KeyStore", ex );
        }
        catch( GeneralSecurityException ex )
        {
            throw new CryptoFailure( "Unable to Base64 encode KeyStore", ex );
        }
        finally
        {
            IO.closeSilently( baos );
        }
    }

    @Override
    public KeyStore base64DecodeKeyStore( String payload, KeyStoreType storeType, char[] password )
    {
        try
        {
            KeyStore keystore = getKeyStoreInstance( storeType );
            keystore.load( new ByteArrayInputStream( Base64.decode( payload.getBytes( IOConstants.UTF_8 ) ) ), password );
            return keystore;
        }
        catch( IOException ex )
        {
            throw new CryptoFailure( "Unable to Base64 decode KeyStore", ex );
        }
        catch( GeneralSecurityException ex )
        {
            throw new CryptoFailure( "Unable to Base64 decode KeyStore", ex );
        }
    }

    @Override
    public X509Certificate readX509PEM( Reader reader )
    {
        try
        {
            return (X509Certificate) new PEMReader( reader ).readObject();
        }
        catch( IOException ex )
        {
            throw new IllegalArgumentException( "Unable to read X509Certificate from PEM", ex );
        }
    }

    @Override
    public PKCS10CertificationRequest readPKCS10PEM( Reader reader )
    {
        try
        {
            return (PKCS10CertificationRequest) new PEMReader( reader ).readObject();
        }
        catch( IOException ex )
        {
            throw new IllegalArgumentException( "Unable to read PKCS#10 from PEM", ex );
        }
    }

    @Override
    public X509CRL readCRLPEM( Reader reader )
    {
        try
        {
            return (X509CRL) new PEMReader( reader ).readObject();
        }
        catch( IOException ex )
        {
            throw new IllegalArgumentException( "Unable to read CRL from PEM", ex );
        }
    }

    @Override
    public KeyPair readKeyPairPEM( Reader reader )
    {
        try
        {
            return (KeyPair) new PEMReader( reader ).readObject();
        }
        catch( IOException ex )
        {
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
        try
        {
            StringWriter sw = new StringWriter();
            PEMWriter pemWriter = new PEMWriter( sw, cryptoContext.providerName() );
            pemWriter.writeObject( object );
            pemWriter.flush();
            return sw.getBuffer();
        }
        catch( IOException ex )
        {
            throw new CryptoFailure( "Unable to write " + ilk + " as PEM", ex );
        }
    }

    private KeyStore getKeyStoreInstance( KeyStoreType storeType )
        throws KeyStoreException, NoSuchProviderException
    {
        if( KeyStoreType.PKCS12 == storeType )
        {
            return KeyStore.getInstance( storeType.typeString(), cryptoContext.providerName() );
        }
        return KeyStore.getInstance( storeType.typeString() );
    }

}
