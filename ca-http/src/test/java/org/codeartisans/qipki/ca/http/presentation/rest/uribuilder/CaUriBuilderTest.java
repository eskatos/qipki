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
package org.codeartisans.qipki.ca.http.presentation.rest.uribuilder;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import org.restlet.data.Reference;

public class CaUriBuilderTest
{

    private static final String ROOT_REF = "http://qipki.codeartisans.org/api";

    @Test
    public void testTools()
    {
        String cryptoInspectorUri = new CaUriBuilder( new Reference( ROOT_REF ) ).tools().cryptoInspector().build();
        Assert.assertEquals( ROOT_REF + "/tools/inspector", cryptoInspectorUri );
    }

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
