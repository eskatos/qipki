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
package org.codeartisans.qipki.core;

import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.qi4j.api.common.InvalidApplicationException;
import org.qi4j.bootstrap.ApplicationAssembler;
import org.qi4j.bootstrap.Energy4Java;
import org.qi4j.spi.Qi4jSPI;
import org.qi4j.spi.structure.ApplicationModelSPI;
import org.qi4j.spi.structure.ApplicationSPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        ensureBouncyCastle();
    }

    private void ensureBouncyCastle()
    {
        if ( Security.getProvider( BouncyCastleProvider.PROVIDER_NAME ) == null ) {
            Security.addProvider( new BouncyCastleProvider() );
        }
    }

    @Override
    public final void run()
    {
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
