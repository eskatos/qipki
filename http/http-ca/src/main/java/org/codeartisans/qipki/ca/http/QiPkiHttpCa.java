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

import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.codeartisans.qipki.ca.http.assembly.QiPkiHttpCaAssembler;
import org.codeartisans.qipki.core.AbstractQiPkiApplication;

import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 *   An engineer, a chemist, and a standards designer are stranded on a desert
 *   island with absolutely nothing on it.  One of them finds a can of spam washed
 *   up by the waves.
 *
 *   The engineer says "Taking the strength of the seams into account, we can
 *   calculate that bashing it against a rock with a given force will open it up
 *   without destroying the contents".
 *
 *   The chemist says "Taking the type of metal the can is made of into account,
 *   we can calculate that further immersion in salt water will corrode it enough
 *   to allow it to be easily opened after a day".
 *
 *   The standards designer gives the other two a condescending look, gazes into
 *   the middle distance, and begins "Assuming we have an electric can opener...".
 *
 *                                                       X.509 Style Guide - October 2000
 *                                               Peter Gutmann, pgut001@cs.auckland.ac.nz
 *                               http://www.cs.auckland.ac.nz/~pgut001/pubs/x509guide.txt
 *
 *   TODO Find citation from http://www.cs.auckland.ac.nz/~pgut001/pubs/pkitutorial.pdf
 * 
 *   TODO Find a way to declare Resources as interfaces so we can use TransientComposites instead of injected Objects
 *      See {@link org.codeartisans.qipki.server.presentation.rest.RestletFinder#create}
 */
public final class QiPkiHttpCa
        extends AbstractQiPkiApplication
{

    public QiPkiHttpCa()
    {
        super( new QiPkiHttpCaAssembler() );
        setUpLogging();
    }

    private void setUpLogging()
    {
        Logger rootLogger = LogManager.getLogManager().getLogger( "" );
        for ( Handler eachHandler : rootLogger.getHandlers() ) {
            rootLogger.removeHandler( eachHandler );
        }
        SLF4JBridgeHandler.install();
    }

}
