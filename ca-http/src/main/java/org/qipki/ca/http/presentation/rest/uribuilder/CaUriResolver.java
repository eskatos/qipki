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
package org.qipki.ca.http.presentation.rest.uribuilder;

import org.codeartisans.java.toolbox.exceptions.NullArgumentException;

import org.qipki.ca.domain.ca.CA;
import org.qipki.ca.domain.cryptostore.CryptoStore;
import org.qipki.ca.domain.escrowedkeypair.EscrowedKeyPair;
import org.qipki.ca.domain.x509.X509;
import org.qipki.ca.domain.x509profile.X509Profile;

import org.restlet.data.Reference;

public class CaUriResolver
{

    private final Class<?> clazz;
    private final String identity;

    public CaUriResolver( Reference rootRef, String uri )
    {
        NullArgumentException.ensureNotNull( "rootRef", rootRef );
        NullArgumentException.ensureNotEmpty( "uri", uri );
        String interestingPath = new Reference( uri ).getPath().replaceAll( "^" + rootRef.getPath() + "/", "" );
        String[] splitted = interestingPath.split( "/" );
        if ( splitted.length != 2 ) {
            throw new IllegalArgumentException( "Unable to resolve URI: " + uri );
        }
        clazz = resolveClass( splitted[0] );
        identity = splitted[1];
    }

    public Class<?> clazz()
    {
        return clazz;
    }

    public String identity()
    {
        return identity;
    }

    private Class<?> resolveClass( String fragment )
    {
        if ( "cryptostore".equals( fragment ) ) {
            return CryptoStore.class;
        } else if ( "ca".equals( fragment ) ) {
            return CA.class;
        } else if ( "x509Profile".equals( fragment ) ) {
            return X509Profile.class;
        } else if ( "x509".equals( fragment ) ) {
            return X509.class;
        } else if ( "escrow".equals( fragment ) ) {
            return EscrowedKeyPair.class;
        }
        throw new IllegalArgumentException( "Unknown class fragment: " + fragment );
    }

}
