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

import org.restlet.data.Reference;

public final class CaUriBuilder
{

    private final Reference root;

    public CaUriBuilder( Reference root )
    {
        this.root = root;
    }

    public CaCryptoStoreUriBuilder cryptoStore()
    {
        return new CaCryptoStoreUriBuilder( root.clone().addSegment( "cryptostore" ), null, null );
    }

    public CaCaUriBuilder ca()
    {
        return new CaCaUriBuilder( root.clone().addSegment( "ca" ), null, null );
    }

    public CaX509ProfileUriBuilder x509Profile()
    {
        return new CaX509ProfileUriBuilder( root.clone().addSegment( "x509Profile" ), null, null );
    }

    public CaX509UriBuilder x509()
    {
        return new CaX509UriBuilder( root.clone().addSegment( "x509" ), null, null );
    }

    public CaEscrowedKeyPairUriBuilder escrowedKeyPair()
    {
        return new CaEscrowedKeyPairUriBuilder( root.clone().addSegment( "escrow" ), null, null );
    }

}
