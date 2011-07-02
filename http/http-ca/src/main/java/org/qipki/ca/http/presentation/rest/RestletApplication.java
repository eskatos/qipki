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
package org.qipki.ca.http.presentation.rest;

import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.injection.scope.Uses;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.qi4j.api.unitofwork.ConcurrentEntityModificationException;
import org.qi4j.api.unitofwork.NoSuchEntityException;
import org.qi4j.api.unitofwork.UnitOfWork;
import org.qi4j.api.unitofwork.UnitOfWorkCompletionException;
import org.qi4j.api.unitofwork.UnitOfWorkException;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

import org.qipki.ca.application.WrongParametersException;
import org.qipki.ca.http.presentation.rest.resources.AbstractDCIResource;
import org.qipki.ca.http.presentation.rest.resources.CaApiRootResource;
import org.qipki.ca.http.presentation.rest.resources.ca.CAExportResource;
import org.qipki.ca.http.presentation.rest.resources.ca.CAListResource;
import org.qipki.ca.http.presentation.rest.resources.ca.CAResource;
import org.qipki.ca.http.presentation.rest.resources.ca.CRLResource;
import org.qipki.ca.http.presentation.rest.resources.cryptostore.CryptoStoreListResource;
import org.qipki.ca.http.presentation.rest.resources.x509.X509ListResource;
import org.qipki.ca.http.presentation.rest.resources.cryptostore.CryptoStoreResource;
import org.qipki.ca.http.presentation.rest.resources.escrowedkeypair.EscrowedKeyPairListResource;
import org.qipki.ca.http.presentation.rest.resources.escrowedkeypair.EscrowedKeyPairPemResource;
import org.qipki.ca.http.presentation.rest.resources.escrowedkeypair.EscrowedKeyPairResource;
import org.qipki.ca.http.presentation.rest.resources.tools.CryptoInspectorResource;
import org.qipki.ca.http.presentation.rest.resources.x509.X509DetailResource;
import org.qipki.ca.http.presentation.rest.resources.x509.X509PemResource;
import org.qipki.ca.http.presentation.rest.resources.x509.X509RecoveryResource;
import org.qipki.ca.http.presentation.rest.resources.x509.X509Resource;
import org.qipki.ca.http.presentation.rest.resources.x509.X509RevocationResource;
import org.qipki.ca.http.presentation.rest.resources.x509profile.X509ProfileListResource;
import org.qipki.ca.http.presentation.rest.resources.x509profile.X509ProfileResource;

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

        router.attach( "/tools/inspector", createFinder( CryptoInspectorResource.class ) );

        router.attach( "/cryptostore", createFinder( CryptoStoreListResource.class ) );
        router.attach( "/cryptostore/{" + AbstractDCIResource.PARAM_IDENTITY + "}", createFinder( CryptoStoreResource.class ) );

        router.attach( "/ca", createFinder( CAListResource.class ) );
        router.attach( "/ca/{" + AbstractDCIResource.PARAM_IDENTITY + "}", createFinder( CAResource.class ) );
        router.attach( "/ca/{" + AbstractDCIResource.PARAM_IDENTITY + "}/export", createFinder( CAExportResource.class ) );
        router.attach( "/ca/{" + AbstractDCIResource.PARAM_IDENTITY + "}/crl", createFinder( CRLResource.class ) );

        router.attach( "/x509Profile", createFinder( X509ProfileListResource.class ) );
        router.attach( "/x509Profile/{" + AbstractDCIResource.PARAM_IDENTITY + "}", createFinder( X509ProfileResource.class ) );

        router.attach( "/x509", createFinder( X509ListResource.class ) );
        router.attach( "/x509/{" + AbstractDCIResource.PARAM_IDENTITY + "}", createFinder( X509Resource.class ) );
        router.attach( "/x509/{" + AbstractDCIResource.PARAM_IDENTITY + "}/pem", createFinder( X509PemResource.class ) );
        router.attach( "/x509/{" + AbstractDCIResource.PARAM_IDENTITY + "}/detail", createFinder( X509DetailResource.class ) );
        router.attach( "/x509/{" + AbstractDCIResource.PARAM_IDENTITY + "}/revocation", createFinder( X509RevocationResource.class ) );
        router.attach( "/x509/{" + AbstractDCIResource.PARAM_IDENTITY + "}/recovery", createFinder( X509RecoveryResource.class ) );

        router.attach( "/escrow", createFinder( EscrowedKeyPairListResource.class ) );
        router.attach( "/escrow/{" + AbstractDCIResource.PARAM_IDENTITY + "}", createFinder( EscrowedKeyPairResource.class ) );
        router.attach( "/escrow/{" + AbstractDCIResource.PARAM_IDENTITY + "}/pem", createFinder( EscrowedKeyPairPemResource.class ) );

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
            LOGGER.warn( "409: {}", ex.getMessage(), ex );
            uow.discard();
            response.setStatus( Status.CLIENT_ERROR_CONFLICT );
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
