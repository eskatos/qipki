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

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import org.restlet.data.Reference;

public class CaUriBuilderTest
{

    private static final String ROOT_REF = "http://qipki.codeartisans.org/api";

    @Test
    public void testCryptoStore()
    {
        String listUri = new CaUriBuilder( new Reference( ROOT_REF ) ).cryptoStore().build();
        Assert.assertEquals( ROOT_REF + "/cryptostore", listUri );

        String id = UUID.randomUUID().toString();

        String entityUri = new CaUriBuilder( new Reference( ROOT_REF ) ).cryptoStore().withIdentity( id ).build();
        Assert.assertEquals( ROOT_REF + "/cryptostore/" + id, entityUri );
    }

    @Test
    public void testCA()
    {
        String listUri = new CaUriBuilder( new Reference( ROOT_REF ) ).ca().build();
        Assert.assertEquals( ROOT_REF + "/ca", listUri );

        String id = UUID.randomUUID().toString();

        String entityUri = new CaUriBuilder( new Reference( ROOT_REF ) ).ca().withIdentity( id ).build();
        Assert.assertEquals( ROOT_REF + "/ca/" + id, entityUri );

        String exportUri = new CaUriBuilder( new Reference( ROOT_REF ) ).ca().withIdentity( id ).export().build();
        Assert.assertEquals( ROOT_REF + "/ca/" + id + "/export", exportUri );

        String crlUri = new CaUriBuilder( new Reference( ROOT_REF ) ).ca().withIdentity( id ).crl().build();
        Assert.assertEquals( ROOT_REF + "/ca/" + id + "/crl", crlUri );
    }

    @Test
    public void testX509Profile()
    {
        String listUri = new CaUriBuilder( new Reference( ROOT_REF ) ).x509Profile().build();
        Assert.assertEquals( ROOT_REF + "/x509Profile", listUri );

        String id = UUID.randomUUID().toString();

        String entityUri = new CaUriBuilder( new Reference( ROOT_REF ) ).x509Profile().withIdentity( id ).build();
        Assert.assertEquals( ROOT_REF + "/x509Profile/" + id, entityUri );
    }

    @Test
    public void testX509()
    {
        String listUri = new CaUriBuilder( new Reference( ROOT_REF ) ).x509().build();
        Assert.assertEquals( ROOT_REF + "/x509", listUri );

        String id = UUID.randomUUID().toString();

        String entityUri = new CaUriBuilder( new Reference( ROOT_REF ) ).x509().withIdentity( id ).build();
        Assert.assertEquals( ROOT_REF + "/x509/" + id, entityUri );

        String detailUri = new CaUriBuilder( new Reference( ROOT_REF ) ).x509().withIdentity( id ).detail().build();
        Assert.assertEquals( ROOT_REF + "/x509/" + id + "/detail", detailUri );

        String revocationUri = new CaUriBuilder( new Reference( ROOT_REF ) ).x509().withIdentity( id ).revocation().build();
        Assert.assertEquals( ROOT_REF + "/x509/" + id + "/revocation", revocationUri );
    }

}
