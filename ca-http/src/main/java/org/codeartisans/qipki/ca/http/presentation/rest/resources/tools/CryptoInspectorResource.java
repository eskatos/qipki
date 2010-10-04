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
package org.codeartisans.qipki.ca.http.presentation.rest.resources.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchProviderException;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collections;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;

import org.codeartisans.java.toolbox.StringUtils;
import org.codeartisans.qipki.crypto.storage.KeyStoreType;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.engine.util.FormUtils;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CryptoInspectorResource
        extends ServerResource
{

    private static final Logger LOGGER = LoggerFactory.getLogger( CryptoInspectorResource.class );

    private final RestletFileUpload fileUpload;

    public CryptoInspectorResource()
    {
        setAllowedMethods( Collections.singleton( Method.POST ) );
        setNegotiated( true );
        File uploadRepository = new File( System.getProperty( "java.io.tmpdir" ), "qipki-crypto-inspector-upload-repository" );
        uploadRepository.mkdirs();
        fileUpload = new RestletFileUpload( new DiskFileItemFactory( 10000, uploadRepository ) );
    }

    @Override
    protected Representation post( Representation entity, Variant responseVariant )
            throws ResourceException
    {
        if ( !entity.isAvailable() ) {
            throw new ResourceException( Status.CLIENT_ERROR_BAD_REQUEST, "Empty request" );
        }
        if ( !MediaType.APPLICATION_WWW_FORM.equals( entity.getMediaType() ) ) {
            throw new ResourceException( Status.CLIENT_ERROR_BAD_REQUEST, "Not a WWW FORM request" );
        }

        InputStream fileInputStream = null;
        try {

            char[] password = getPassword( entity );

            List<FileItem> fileItems = fileUpload.parseRepresentation( entity );
            if ( fileItems.size() != 1 ) {
                throw new ResourceException( Status.CLIENT_ERROR_BAD_REQUEST, "Need one and only one file in request" );
            }
            FileItem fileItem = fileItems.get( 0 );
            fileInputStream = fileItem.getInputStream();

            // TODO Implement CryptoInspectorResource

            return new EmptyRepresentation();

        } catch ( IOException ex ) {
            LOGGER.warn( "500: {}", ex.getMessage(), ex );
            throw new ResourceException( Status.SERVER_ERROR_INTERNAL, "Unable to read posted value", ex );
        } catch ( FileUploadException ex ) {
            LOGGER.warn( "500: {}", ex.getMessage(), ex );
            throw new ResourceException( Status.SERVER_ERROR_INTERNAL, "Unable to read posted value", ex );
        } finally {
            if ( fileInputStream != null ) {
                try {
                    fileInputStream.close();
                } catch ( IOException ignored ) {
                }
            }
        }
    }

    private char[] getPassword( Representation entity )
    {
        char[] password = "".toCharArray();
        Form form = new Form();
        FormUtils.parse( form, entity );
        Parameter passwordParam = form.getFirst( "password" );
        if ( passwordParam != null ) {
            if ( !StringUtils.isEmpty( passwordParam.getValue() ) ) {
                password = passwordParam.getValue().toCharArray();
            }
        }
        return password;
    }

    private boolean isPEM( InputStream stream, final char[] password )
    {
        PEMReader pemReader = null;
        try {
            PasswordFinder passwordFinder = new PasswordFinder()
            {

                @Override
                public char[] getPassword()
                {
                    return password;
                }

            };
            pemReader = new PEMReader( new BufferedReader( new InputStreamReader( stream ) ), passwordFinder );
            return pemReader.readObject() != null;
        } catch ( IOException ex ) {
            return false;
        } finally {
            try {
                if ( pemReader != null ) {
                    pemReader.close();
                }
            } catch ( IOException ignored ) {
            }
        }
    }

    private boolean isDER( InputStream stream )
    {
        CertificateFactory certFactory = null;
        try {
            certFactory = CertificateFactory.getInstance( "X.509", BouncyCastleProvider.PROVIDER_NAME );
        } catch ( GeneralSecurityException ignored ) {
            return false;
        }
        try {
            certFactory.generateCRLs( stream );
            return true;
        } catch ( CRLException ignored ) {
        }
        try {
            certFactory.generateCertificates( stream );
            return true;
        } catch ( CertificateException ignored ) {
        }
        // TODO : all other types ........
        return false;
    }

    private boolean isKeyStore( KeyStoreType ksType, InputStream stream, char[] password )
    {
        try {
            getKeyStoreInstance( ksType ).load( stream, password );
            return true;
        } catch ( IOException ignored ) {
            return false;
        } catch ( GeneralSecurityException ignored ) {
            return false;
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
