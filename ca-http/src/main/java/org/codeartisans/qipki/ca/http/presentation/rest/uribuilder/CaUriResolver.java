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
package org.codeartisans.qipki.ca.http.presentation.rest.uribuilder;

import org.codeartisans.java.toolbox.exceptions.NullArgumentException;
import org.codeartisans.qipki.ca.domain.ca.CA;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStore;
import org.codeartisans.qipki.ca.domain.escrowedkeypair.EscrowedKeyPair;
import org.codeartisans.qipki.ca.domain.x509.X509;
import org.codeartisans.qipki.ca.domain.x509profile.X509Profile;

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
