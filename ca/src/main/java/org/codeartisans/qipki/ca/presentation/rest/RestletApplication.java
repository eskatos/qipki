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
package org.codeartisans.qipki.ca.presentation.rest;

import org.codeartisans.qipki.ca.presentation.rest.resources.AbstractEntityResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.ApiRootResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.ca.CAFactoryResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.ca.CAListResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.ca.CAResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.cryptostore.CryptoStoreFactoryResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.cryptostore.CryptoStoreListResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.ca.PKCS10SignerResource;
import org.codeartisans.qipki.ca.presentation.rest.resources.cryptostore.CryptoStoreResource;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.injection.scope.Uses;
import org.qi4j.api.object.ObjectBuilderFactory;
import org.qi4j.api.unitofwork.ConcurrentEntityModificationException;
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

public class RestletApplication
        extends Application
{

    @Structure
    private ObjectBuilderFactory obf;
    @Structure
    private UnitOfWorkFactory uowf;

    public RestletApplication( @Uses Context parentContext )
    {
        super( parentContext );
    }

    @Override
    public synchronized Restlet createRoot()
    {
        Router router = new Router( getContext() );

        router.attach( "/", createFinder( ApiRootResource.class ) );

        router.attach( "/cryptostore", createFinder( CryptoStoreListResource.class ) );
        router.attach( "/cryptostore/factory", createFinder( CryptoStoreFactoryResource.class ) );
        router.attach( "/cryptostore/{" + AbstractEntityResource.PARAM_IDENTITY + "}", createFinder( CryptoStoreResource.class ) );

        router.attach( "/ca", createFinder( CAListResource.class ) );
        router.attach( "/ca/factory", createFinder( CAFactoryResource.class ) );
        router.attach( "/ca/{" + AbstractEntityResource.PARAM_IDENTITY + "}", createFinder( CAResource.class ) );
        router.attach( "/ca/{" + AbstractEntityResource.PARAM_IDENTITY + "}/pkcs10signer", createFinder( PKCS10SignerResource.class ) );

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
        } catch ( UnitOfWorkException e ) {
            uow.discard();
            response.setStatus( Status.CLIENT_ERROR_NOT_ACCEPTABLE );
            response.setEntity( new ExceptionRepresentation( e ) );
            // More info to send...
        } catch ( ConcurrentEntityModificationException e ) {
            uow.discard();
            response.setStatus( Status.CLIENT_ERROR_LOCKED );
            response.setEntity( new ExceptionRepresentation( e ) );
            // Info to try again...
        } catch ( UnitOfWorkCompletionException e ) {
            uow.discard();
            response.setStatus( Status.CLIENT_ERROR_NOT_ACCEPTABLE );
            response.setEntity( new ExceptionRepresentation( e ) );
        }
    }

}
