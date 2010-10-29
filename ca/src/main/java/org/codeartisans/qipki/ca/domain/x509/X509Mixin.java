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
package org.codeartisans.qipki.ca.domain.x509;

import java.io.IOException;
import java.io.StringReader;
import java.security.cert.X509Certificate;

import org.bouncycastle.openssl.PEMReader;

import org.codeartisans.qipki.core.QiPkiFailure;

import org.qi4j.api.injection.scope.This;

public class X509Mixin
        implements X509Behavior
{

    @This
    private X509 state;

    @Override
    public X509Certificate x509Certificate()
    {
        try {
            return ( X509Certificate ) new PEMReader( new StringReader( state.pem().get() ) ).readObject();
        } catch ( IOException ex ) {
            throw new QiPkiFailure( "Unable to read X509 pem", ex );
        }
    }

}
