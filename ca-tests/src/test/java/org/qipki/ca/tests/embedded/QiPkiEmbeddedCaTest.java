/*
 * Copyright (c) 2011, Paul Merlin. All Rights Reserved.
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
package org.qipki.ca.tests.embedded;

import org.junit.BeforeClass;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QiPkiEmbeddedCaTest
        extends AbstractQiPkiCaTest
{

    private static final Logger LOGGER = LoggerFactory.getLogger( QiPkiEmbeddedCaTest.class );

    @BeforeClass
    public static void startQiPkiApplication()
    {
        qipkiApplication = new QiPkiTestApplicationEmbeddedCa( QiPkiEmbeddedCaTest.class.getSimpleName() );
        qipkiApplication.run();
    }

    @Test
    public void testInit()
    {
        System.out.println( "##################################################" );
        System.out.println( "                    Init" );
        System.out.println( "##################################################" );
    }

    @Test
    public void testCreateBaseCertificates()
    {
        System.out.println( "##################################################" );
        System.out.println( "             CreateBaseCertificates" );
        System.out.println( "##################################################" );
    }

    @Test
    public void testIssueX509()
    {
        System.out.println( "##################################################" );
        System.out.println( "                    IssueX509" );
        System.out.println( "##################################################" );
    }

    @Test
    public void testRenewX509()
    {
        System.out.println( "##################################################" );
        System.out.println( "                     RenewX509" );
        System.out.println( "##################################################" );
    }

    @Test
    public void testRevokeX509()
    {
        System.out.println( "##################################################" );
        System.out.println( "                     RevokeX509" );
        System.out.println( "##################################################" );
    }

    @Test
    public void testIssueEscrowedCertifiedKeyPair()
    {
        System.out.println( "##################################################" );
        System.out.println( "          IssueEscrowedCertifiedKeyPair" );
        System.out.println( "##################################################" );
    }

    @Test
    public void testRecoverEscrowedCertifiedKeyPair()
    {
        System.out.println( "##################################################" );
        System.out.println( "         RecoverEscrowedCertifiedKeyPair" );
        System.out.println( "##################################################" );
    }

    @Test
    public void testRenewEscrowedCertifiedKeyPair()
    {
        System.out.println( "##################################################" );
        System.out.println( "         RenewEscrowedCertifiedKeyPair" );
        System.out.println( "##################################################" );
    }

    @Test
    public void testBatchEnroll()
    {
        System.out.println( "##################################################" );
        System.out.println( "                 BatchEnroll" );
        System.out.println( "##################################################" );
    }

}
