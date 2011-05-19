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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.asn1.x509.X509Name;

import org.junit.Test;

public class DistinguishedNameTemplateTest
{

    private static final String TEMPLATE = "CN=${U_FIRSTNAME} ${U_LASTNAME}, E=${U_EMAIL}, O=${U_ORGANISATION}, OU=Github, 0.9.2342.19200300.100.1.5=${D_SERIALNUMBER}";

    @Test
    public void testTemplate()
    {
        Map<String, String> variables = new HashMap<String, String>();
        variables.put( "U_FIRSTNAME", "Paul" );
        variables.put( "U_LASTNAME", "Merl,=\"+<>#;\\in" );
        variables.put( "U_ORGANISATION", "Codearti€ans" );
        variables.put( "U_EMAIL", "paul+zog@nosphere.org" );

        DistinguishedNameTemplate dnTemplate = new DistinguishedNameTemplate( TEMPLATE );

        DistinguishedName dn = dnTemplate.buildDN( Collections.unmodifiableMap( variables ) );

        soutDN( dn );
    }

    private void soutDN( DistinguishedName dn )
    {
        System.out.println( "" );
        System.out.println( dn.toString( DistinguishedName.Format.RFC1779 ) );
        System.out.println( dn.toString( DistinguishedName.Format.RFC1779_and_RFC3383 ) );
        System.out.println( "" );
        System.out.println( dn.toString( DistinguishedName.Format.RFC2253 ) );
        System.out.println( dn.toString( DistinguishedName.Format.RFC2253_and_RFC3383 ) );
        System.out.println( dn.toString( DistinguishedName.Format.RFC2253_CANONICAL ) );
        System.out.println( "" );

        System.out.println( "" ); // BouncyCastle does not support RFC1779 formatted Directory Strings
        System.out.println( new X509Name( dn.toString( DistinguishedName.Format.RFC2253 ) ).toString() );
        System.out.println( new X509Name( dn.toString( DistinguishedName.Format.RFC2253_and_RFC3383 ) ).toString() );
        System.out.println( new X509Name( dn.toString( DistinguishedName.Format.RFC2253_CANONICAL ) ).toString() );
        System.out.println( "" );
    }

}
