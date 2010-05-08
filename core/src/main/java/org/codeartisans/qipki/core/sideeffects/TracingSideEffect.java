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
package org.codeartisans.qipki.core.sideeffects;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMWriter;
import org.qi4j.api.sideeffect.GenericSideEffect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TracingSideEffect
        extends GenericSideEffect
{

    private static final Logger LOGGER = LoggerFactory.getLogger( TracingSideEffect.class );

    @Override
    protected void invoke( Method method, Object[] args )
            throws Throwable
    {
        LOGGER.trace( "{}.{}( {} );", new Object[]{ method.getDeclaringClass().getSimpleName(), method.getName(), Arrays.toString( handleArgs( args ) ) } );
    }

    private Object[] handleArgs( Object[] args )
    {
        Object[] handled = new Object[ args.length ];
        for ( int idx = 0; idx < args.length; idx++ ) {
            Object eachArg = args[idx];
            if ( eachArg == null ) {
                handled[idx] = eachArg;
            } else {
                if ( X509Certificate.class.isAssignableFrom( eachArg.getClass() )
                        || PKCS10CertificationRequest.class.isAssignableFrom( eachArg.getClass() )
                        || X509CRL.class.isAssignableFrom( eachArg.getClass() ) ) {
                    try {
                        StringWriter sw = new StringWriter();
                        PEMWriter pem = new PEMWriter( sw );
                        pem.writeObject( eachArg );
                        pem.flush();
                        handled[idx] = sw.toString();
                    } catch ( IOException ex ) {
                        handled[idx] = eachArg;
                    }
                } else {
                    handled[idx] = eachArg;
                }
            }
        }
        return handled;
    }

}
