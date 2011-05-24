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
package org.qipki.core;

import org.qi4j.api.common.InvalidApplicationException;
import org.qi4j.bootstrap.ApplicationAssembler;
import org.qi4j.bootstrap.Energy4Java;
import org.qi4j.spi.Qi4jSPI;
import org.qi4j.spi.structure.ApplicationModelSPI;
import org.qi4j.spi.structure.ApplicationSPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings( "ProtectedField" )
public abstract class AbstractQiPkiApplication
        implements QiPkiApplication
{

    private static final Logger LOGGER = LoggerFactory.getLogger( AbstractQiPkiApplication.class );
    private final ApplicationAssembler appAssembler;
    protected Energy4Java qi4j;
    protected ApplicationModelSPI applicationModel;
    protected ApplicationSPI application;
    protected Qi4jSPI spi;
    protected Qi4jSPI api;

    protected AbstractQiPkiApplication( ApplicationAssembler appAssembler )
    {
        this.appAssembler = appAssembler;
    }

    @Override
    public final void run()
    {
        if ( application != null ) {
            throw new IllegalStateException( "QiPkiApplication named " + application.name() + " is already active, cannot continue" );
        }
        try {

            LOGGER.debug( "Assembling QiPKI Application" );
            qi4j = new Energy4Java();
            applicationModel = qi4j.newApplicationModel( appAssembler );
            LOGGER.debug( "Instanciating QiPKI Application" );
            application = applicationModel.newInstance( qi4j.spi() );
            spi = qi4j.spi();
            api = spi;

            LOGGER.debug( "Activating QiPKI Application named: " + application.name() );
            Runtime.getRuntime().addShutdownHook( new Thread( new Runnable()
            {

                @Override
                public void run()
                {
                    stop();
                }

            }, application.name() + "-shutdownhook" ) );
            beforeActivate();
            application.activate();
            afterActivate();

        } catch ( Exception ex ) {
            if ( application != null ) {
                try {
                    application.passivate();
                } catch ( Exception ex1 ) {
                    LOGGER.warn( "QiPKI Application named {} not null and could not passivate it.", application.name(), ex1 );
                }
            }
            throw new InvalidApplicationException( "Unexpected error during QiPKI Application initialization", ex );
        }
    }

    @Override
    public final void stop()
    {
        try {
            if ( application != null ) {
                LOGGER.info( "Shutting down QiPKI Application {}", application.name() );
                beforePassivate();
                application.passivate();
                afterPassivate();
                application = null;
            }
        } catch ( Exception ex ) {
            LOGGER.warn( "Unable to passivate QiPKI Application", ex );
        }
    }

    protected void beforeActivate()
    {
    }

    protected void afterActivate()
    {
    }

    protected void beforePassivate()
    {
    }

    protected void afterPassivate()
    {
    }

}