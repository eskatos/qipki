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
package org.codeartisans.qipki.ca.http.presentation.rest;

import org.codeartisans.qipki.ca.http.presentation.rest.resources.AbstractResource;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.CaApiRootResource;
import org.codeartisans.qipki.ca.application.WrongParametersException;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.ca.CAListResource;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.ca.CAResource;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.ca.CRLResource;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.cryptostore.CryptoStoreListResource;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.x509.X509ListResource;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.cryptostore.CryptoStoreResource;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.escrowedkeypair.EscrowedKeyPairListResource;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.escrowedkeypair.EscrowedKeyPairPemResource;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.escrowedkeypair.EscrowedKeyPairResource;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.x509.X509DetailResource;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.x509.X509PemResource;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.x509.X509Resource;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.x509.X509RevocationResource;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.x509profile.X509ProfileListResource;
import org.codeartisans.qipki.ca.http.presentation.rest.resources.x509profile.X509ProfileResource;

import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.injection.scope.Uses;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.qi4j.api.unitofwork.ConcurrentEntityModificationException;
import org.qi4j.api.unitofwork.NoSuchEntityException;
import org.qi4j.api.unitofwork.UnitOfWork;
import org.qi4j.api.unitofwork.UnitOfWorkCompletionException;
import org.qi4j.api.unitofwork.UnitOfWorkException;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Status;
import org.restlet.resource.Finder;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestletApplication
        extends Application
{

    private static final Logger LOGGER = LoggerFactory.getLogger( RestletApplication.class );

    @Structure
    private ObjectBuilderFactory obf;

    @Structure
    private UnitOfWorkFactory uowf;

    public RestletApplication( @Uses Context parentContext )
    {
        super( parentContext );
    }

    @Override
    public synchronized Restlet createInboundRoot()
    {
        Router router = new Router( getContext() );

        Finder caApiFinder = createFinder( CaApiRootResource.class );
        router.attach( "", caApiFinder );
        router.attach( "/", caApiFinder );

        router.attach( "/cryptostore", createFinder( CryptoStoreListResource.class ) );
        router.attach( "/cryptostore/{" + AbstractResource.PARAM_IDENTITY + "}", createFinder( CryptoStoreResource.class ) );

        router.attach( "/ca", createFinder( CAListResource.class ) );
        router.attach( "/ca/{" + AbstractResource.PARAM_IDENTITY + "}", createFinder( CAResource.class ) );
        router.attach( "/ca/{" + AbstractResource.PARAM_IDENTITY + "}/crl", createFinder( CRLResource.class ) );

        router.attach( "/x509Profile", createFinder( X509ProfileListResource.class ) );
        router.attach( "/x509Profile/{" + AbstractResource.PARAM_IDENTITY + "}", createFinder( X509ProfileResource.class ) );

        router.attach( "/x509", createFinder( X509ListResource.class ) );
        router.attach( "/x509/{" + AbstractResource.PARAM_IDENTITY + "}", createFinder( X509Resource.class ) );
        router.attach( "/x509/{" + AbstractResource.PARAM_IDENTITY + "}/pem", createFinder( X509PemResource.class ) );
        router.attach( "/x509/{" + AbstractResource.PARAM_IDENTITY + "}/detail", createFinder( X509DetailResource.class ) );
        router.attach( "/x509/{" + AbstractResource.PARAM_IDENTITY + "}/revocation", createFinder( X509RevocationResource.class ) );

        router.attach( "/escrow", createFinder( EscrowedKeyPairListResource.class ) );
        router.attach( "/escrow/{" + AbstractResource.PARAM_IDENTITY + "}", createFinder( EscrowedKeyPairResource.class ) );
        router.attach( "/escrow/{" + AbstractResource.PARAM_IDENTITY + "}/pem", createFinder( EscrowedKeyPairPemResource.class ) );

        return new ExtensionMediaTypeFilter( getContext(), router );
    }

    private Finder createFinder( Class<? extends ServerResource> resource )
    {
        Finder finder = obf.newObject( Finder.class );
        finder.setTargetClass( resource );
        return finder;
    }

    @Override
    public void handle( Request request, Response response )
    {
        UnitOfWork uow = uowf.newUnitOfWork();
        try {
            super.handle( request, response );
            uow.complete();
        } catch ( NoSuchEntityException ex ) {
            LOGGER.debug( "404: No Entity found for the requested identity ('{}')", ex.identity().identity(), ex );
            uow.discard();
            response.setStatus( Status.CLIENT_ERROR_NOT_FOUND );
            response.setEntity( new ExceptionRepresentation( ex ) );
        } catch ( UnitOfWorkException ex ) {
            LOGGER.warn( "406: {}", ex.getMessage(), ex );
            uow.discard();
            response.setStatus( Status.CLIENT_ERROR_NOT_ACCEPTABLE );
            response.setEntity( new ExceptionRepresentation( ex ) );
            // More info to send...
        } catch ( ConcurrentEntityModificationException ex ) {
            LOGGER.warn( "423: {}", ex.getMessage(), ex );
            uow.discard();
            response.setStatus( Status.CLIENT_ERROR_LOCKED );
            response.setEntity( new ExceptionRepresentation( ex ) );
            // Info to try again...
        } catch ( UnitOfWorkCompletionException ex ) {
            LOGGER.warn( "406: {}", ex.getMessage(), ex );
            uow.discard();
            response.setStatus( Status.CLIENT_ERROR_NOT_ACCEPTABLE );
            response.setEntity( new ExceptionRepresentation( ex ) );
        } catch ( WrongParametersException ex ) {
            LOGGER.warn( "400: {}", ex.getMessage(), ex );
            uow.discard();
            response.setStatus( Status.CLIENT_ERROR_BAD_REQUEST );
            response.setEntity( new ExceptionRepresentation( ex ) );

        }
    }

}
