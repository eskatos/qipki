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
package org.qipki.crypto.x509;

import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.asn1.x509.X509Name;

import static org.junit.Assert.*;
import org.junit.Test;

import static org.qipki.crypto.x509.DistinguishedName.Format.*;

public class DistinguishedNameTemplateTest
{

    private static final String EXPECTED_RFC1779 = "CN=\"Paul Merl,=\\\"+<>#;\\\\in\", OID.1.2.840.113549.1.9.1=\"paul+zog@nosphere.org\", O=Codearti€ans, OU=Github";
    private static final String EXPECTED_RFC1779_and_RFC3383 = "CN=\"Paul Merl,=\\\"+<>#;\\\\in\", OID.1.2.840.113549.1.9.1=\"paul+zog@nosphere.org\", O=Codearti€ans, OU=Github";
    private static final String EXPECTED_RFC2253 = "CN=Paul Merl\\,\\=\\\"\\+\\<\\>\\#\\;\\\\in,1.2.840.113549.1.9.1=#16157061756c2b7a6f67406e6f7370686572652e6f7267,O=Codearti€ans,OU=Github";
    private static final String EXPECTED_RFC2253_and_RFC3383 = "CN=Paul Merl\\,\\=\\\"\\+\\<\\>\\#\\;\\\\in,1.2.840.113549.1.9.1=#16157061756c2b7a6f67406e6f7370686572652e6f7267,O=Codearti€ans,OU=Github";
    private static final String EXPECTED_RFC2253_CANONICAL = "cn=paul merl\\,=\\\"\\+\\<\\>#\\;\\\\in,1.2.840.113549.1.9.1=#16157061756c2b7a6f67406e6f7370686572652e6f7267,o=codearti€ans,ou=github";
    private static final String EXPECTED_BC_RFC2253 = "CN=Paul Merl\\,\\=\\\\\\+\\<\\>#\\;\\\\in,E=#16157061756c2b7a6f67406e6f7370686572652e6f7267,O=Codearti€ans,OU=Github";
    private static final String EXPECTED_BC_RFC2253_and_RFC3383 = "CN=Paul Merl\\,\\=\\\\\\+\\<\\>#\\;\\\\in,E=#16157061756c2b7a6f67406e6f7370686572652e6f7267,O=Codearti€ans,OU=Github";
    private static final String EXPECTED_BC_RFC2253_CANONICAL = "CN=paul merl\\,\\=\\\\\\+\\<\\>#\\;\\\\in,E=#16157061756c2b7a6f67406e6f7370686572652e6f7267,O=codearti€ans,OU=github";

    // SNIPPET BEGIN crypto.dn
    @Test
    public void testTemplate()
    {
        String template = "CN=${U_FIRSTNAME} ${U_LASTNAME}, E=${U_EMAIL}, O=${U_ORGANISATION}, OU=Github, 0.9.2342.19200300.100.1.5=${D_SERIALNUMBER}";

        Map<String, String> variables = new HashMap<String, String>();
        variables.put( "U_FIRSTNAME", "Paul" );
        variables.put( "U_LASTNAME", "Merl,=\"+<>#;\\in" );
        variables.put( "U_ORGANISATION", "Codearti€ans" );
        variables.put( "U_EMAIL", "paul+zog@nosphere.org" );

        DistinguishedName dn = new DistinguishedNameTemplate( template ).buildDN( variables );

        systemOutput( dn );

        // Assert formats
        assertEquals( EXPECTED_RFC1779, dn.toString( RFC1779 ) );
        assertEquals( EXPECTED_RFC1779_and_RFC3383, dn.toString( RFC1779_and_RFC3383 ) );

        assertEquals( EXPECTED_RFC2253, dn.toString( RFC2253 ) );
        assertEquals( EXPECTED_RFC2253_and_RFC3383, dn.toString( RFC2253_and_RFC3383 ) );
        assertEquals( EXPECTED_RFC2253_CANONICAL, dn.toString( RFC2253_CANONICAL ) );

        // Assert formats through BouncyCastle X509Name (does not support RFC1779 formatted Directory Strings)
        assertEquals( EXPECTED_BC_RFC2253, new X509Name( dn.toString( RFC2253 ) ).toString() );
        assertEquals( EXPECTED_BC_RFC2253_and_RFC3383, new X509Name( dn.toString( RFC2253_and_RFC3383 ) ).toString() );
        assertEquals( EXPECTED_BC_RFC2253_CANONICAL, new X509Name( dn.toString( RFC2253_CANONICAL ) ).toString() );
    }
    // SNIPPET END crypto.dn

    private void systemOutput( DistinguishedName dn )
    {
        System.out.println( "" );
        System.out.println( dn.toString( RFC1779 ) );
        System.out.println( dn.toString( RFC1779_and_RFC3383 ) );
        System.out.println( "" );
        System.out.println( dn.toString( RFC2253 ) );
        System.out.println( dn.toString( RFC2253_and_RFC3383 ) );
        System.out.println( dn.toString( RFC2253_CANONICAL ) );
        System.out.println( "" );

        System.out.println( new X509Name( dn.toString( RFC2253 ) ).toString() );
        System.out.println( new X509Name( dn.toString( RFC2253_and_RFC3383 ) ).toString() );
        System.out.println( new X509Name( dn.toString( RFC2253_CANONICAL ) ).toString() );
        System.out.println( "" );
    }

}
