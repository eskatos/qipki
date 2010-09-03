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
package org.codeartisans.qipki.ca;

import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.codeartisans.qipki.ca.assembly.QiPkiCaAssembler;
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
public final class QiPkiCa
        extends AbstractQiPkiApplication
{

    public QiPkiCa()
    {
        super( new QiPkiCaAssembler() );
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
