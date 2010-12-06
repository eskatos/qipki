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
package org.codeartisans.qipki.ca.http;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QiPkiHttpCaBundleActivator
        implements BundleActivator
{

    private static final Logger LOGGER = LoggerFactory.getLogger( QiPkiHttpCaBundleActivator.class );
    private QiPkiHttpCa qipkiServer;

    public QiPkiHttpCaBundleActivator()
    {
        LOGGER.info( "Assembling QiPki::CA::Server Bundle" );
        qipkiServer = new QiPkiHttpCa();
    }

    @Override
    public void start( BundleContext bc )
            throws Exception
    {
        LOGGER.info( "Starting   QiPki::CA::Server Bundle" );
        qipkiServer.run();
    }

    @Override
    public void stop( BundleContext bc )
            throws Exception
    {
        LOGGER.info( "Stopping   QiPki::CA::Server Bundle" );
        qipkiServer.stop();
    }

}
