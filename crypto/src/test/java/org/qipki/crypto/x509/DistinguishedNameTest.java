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

import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;

public class DistinguishedNameTest
    extends TestCase
{

    private static final String WRONG_SAMPLE = "WRONG=ROI, BAD=HENOK";
    private static final String RFC1779_SAMPLE = "CN=Paul Merlin, OID.2.5.4.45=J0290952, OU=Codeartisans Github, O=Codeartisans, C=FR";
    private static final String RFC2253_SAMPLE = "CN=Paul Merlin,2.5.4.45=#13084a30323930393532,OU=Codeartisans Github,O=Codeartisans,C=FR";
    private static final String RFC1779_and_3383_SAMPLE = "CN=Paul Merlin, x500UniqueIdentifier=J0290952, OU=Codeartisans Github, O=Codeartisans, C=FR";
    private static final String RFC2253_and_3383_SAMPLE = "CN=Paul Merlin,x500UniqueIdentifier=J0290952,OU=Codeartisans Github,O=Codeartisans,C=FR";

    @SuppressWarnings( "ResultOfObjectAllocationIgnored" )
    public void test_wrong()
    {
        try
        {
            new DistinguishedName( WRONG_SAMPLE );
            fail( "Was expecting an IllegalArgumentException with the following DN string representation: " + WRONG_SAMPLE );
        }
        catch( IllegalArgumentException ex )
        {
        }
    }

    public void test_from_RFC1779_to_RFC1779()
    {
        doTestWithSample( "RFC1779", new DistinguishedName( RFC1779_SAMPLE ),
                          DistinguishedName.Format.RFC1779, RFC1779_SAMPLE );
    }

    public void test_from_RFC1779_to_RFC2253()
    {
        doTestWithSample( "RFC1779", new DistinguishedName( RFC1779_SAMPLE ),
                          DistinguishedName.Format.RFC2253, RFC2253_SAMPLE );
    }

    public void test_from_RFC1779_to_RFC1779_and_RFC3383()
    {
        doTestWithSample( "RFC1779", new DistinguishedName( RFC1779_SAMPLE ),
                          DistinguishedName.Format.RFC1779_and_RFC3383, RFC1779_and_3383_SAMPLE );
    }

    public void test_from_RFC1779_to_RFC2253_and_RFC3383()
    {
        doTestWithSample( "RFC1779", new DistinguishedName( RFC1779_SAMPLE ),
                          DistinguishedName.Format.RFC2253_and_RFC3383, RFC2253_and_3383_SAMPLE );
    }

    public void test_from_RFC2253_to_RFC1779()
    {
        doTestWithSample( "RFC2253", new DistinguishedName( RFC2253_SAMPLE ),
                          DistinguishedName.Format.RFC1779, RFC1779_SAMPLE );
    }

    public void test_from_RFC2253_to_RFC2253()
    {
        doTestWithSample( "RFC2253", new DistinguishedName( RFC2253_SAMPLE ),
                          DistinguishedName.Format.RFC2253, RFC2253_SAMPLE );
    }

    public void test_from_RFC2253_to_RFC1779_and_RFC3383()
    {
        doTestWithSample( "RFC2253", new DistinguishedName( RFC2253_SAMPLE ),
                          DistinguishedName.Format.RFC1779_and_RFC3383, RFC1779_and_3383_SAMPLE );
    }

    public void test_from_RFC2253_to_RFC2253_and_RFC3383()
    {
        doTestWithSample( "RFC2253", new DistinguishedName( RFC2253_SAMPLE ),
                          DistinguishedName.Format.RFC2253_and_RFC3383, RFC2253_and_3383_SAMPLE );
    }

    public void test_from_RFC1779_and_RFC3383_to_RFC1779()
    {
        doTestWithSample( "RFC1779_and_RFC3383", new DistinguishedName( RFC1779_and_3383_SAMPLE ),
                          DistinguishedName.Format.RFC1779, RFC1779_SAMPLE );
    }

    public void test_from_RFC1779_and_RFC3383_to_RFC2253()
    {
        doTestWithSample( "RFC1779_and_RFC3383", new DistinguishedName( RFC1779_and_3383_SAMPLE ),
                          DistinguishedName.Format.RFC2253, RFC2253_SAMPLE );
    }

    public void test_from_RFC1779_and_RFC3383_to_RFC1779_and_RFC3383()
    {
        doTestWithSample( "RFC1779_and_RFC3383", new DistinguishedName( RFC1779_and_3383_SAMPLE ),
                          DistinguishedName.Format.RFC1779_and_RFC3383, RFC1779_and_3383_SAMPLE );
    }

    public void test_from_RFC1779_and_RFC3383_to_RFC2253_and_RFC3383()
    {
        doTestWithSample( "RFC1779_and_RFC3383", new DistinguishedName( RFC1779_and_3383_SAMPLE ),
                          DistinguishedName.Format.RFC2253_and_RFC3383, RFC2253_and_3383_SAMPLE );
    }

    public void test_from_RFC2253_and_RFC3383_to_RFC1779()
    {
        doTestWithSample( "RFC2253_and_RFC3383", new DistinguishedName( RFC2253_and_3383_SAMPLE ),
                          DistinguishedName.Format.RFC1779, RFC1779_SAMPLE );
    }

    public void test_from_RFC2253_and_RFC3383_to_RFC2253()
    {
        doTestWithSample( "RFC2253_and_RFC3383", new DistinguishedName( RFC2253_and_3383_SAMPLE ),
                          DistinguishedName.Format.RFC2253, RFC2253_SAMPLE );
    }

    public void test_from_RFC2253_and_RFC3383_to_RFC1779_and_RFC3383()
    {
        doTestWithSample( "RFC2253_and_RFC3383", new DistinguishedName( RFC2253_and_3383_SAMPLE ),
                          DistinguishedName.Format.RFC1779_and_RFC3383, RFC1779_and_3383_SAMPLE );
    }

    public void test_from_RFC2253_and_RFC3383_to_RFC2253_and_RFC3383()
    {
        doTestWithSample( "RFC2253_and_RFC3383", new DistinguishedName( RFC2253_and_3383_SAMPLE ),
                          DistinguishedName.Format.RFC2253_and_RFC3383, RFC2253_and_3383_SAMPLE );
    }

    private void doTestWithSample( String fromFormat, DistinguishedName dn, DistinguishedName.Format toFormat, String expected )
    {
        String testedRepresentation = dn.toString( toFormat );
        System.out.println( StringUtils.leftPad( fromFormat + " as " + toFormat.name() + ": ", 45 ) + testedRepresentation );
        assertEquals( expected, testedRepresentation );
    }

}
