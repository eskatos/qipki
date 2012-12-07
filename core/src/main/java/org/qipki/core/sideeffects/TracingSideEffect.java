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
package org.qipki.core.sideeffects;

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
        LOGGER.trace( "{}.{}( {} );", new Object[]
            {
                method.getDeclaringClass().getSimpleName(),
                method.getName(),
                Arrays.toString( handleArgs( args ) )
            } );
    }

    private Object[] handleArgs( Object[] args )
    {
        Object[] handled = new Object[ args.length ];
        for( int idx = 0; idx < args.length; idx++ )
        {
            Object eachArg = args[idx];
            if( eachArg == null )
            {
                handled[idx] = eachArg;
            }
            else
            {
                if( X509Certificate.class.isAssignableFrom( eachArg.getClass() )
                    || PKCS10CertificationRequest.class.isAssignableFrom( eachArg.getClass() )
                    || X509CRL.class.isAssignableFrom( eachArg.getClass() ) )
                {
                    try
                    {
                        StringWriter sw = new StringWriter();
                        PEMWriter pem = new PEMWriter( sw );
                        pem.writeObject( eachArg );
                        pem.flush();
                        handled[idx] = sw.toString();
                    }
                    catch( IOException ex )
                    {
                        handled[idx] = eachArg;
                    }
                }
                else
                {
                    handled[idx] = eachArg;
                }
            }
        }
        return handled;
    }

}
