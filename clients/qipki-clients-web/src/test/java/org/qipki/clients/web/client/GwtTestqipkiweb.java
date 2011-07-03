package org.qipki.clients.web.client;

import org.qipki.clients.web.shared.FieldVerifier;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * GWT JUnit <b>integration</b> tests must extend GWTTestCase.
 * Using <code>"GwtTest*"</code> naming pattern exclude them from running with
 * surefire during the test phase.
 */
public class GwtTestqipkiweb
        extends GWTTestCase
{

    /**
     * Must refer to a valid module that sources this class.
     */
    public String getModuleName()
    {
        return "org.qipki.clients.web.qipkiwebJUnit";
    }

    /**
     * Tests the FieldVerifier.
     */
    public void testFieldVerifier()
    {
        assertFalse( FieldVerifier.isValidName( null ) );
        assertFalse( FieldVerifier.isValidName( "" ) );
        assertFalse( FieldVerifier.isValidName( "a" ) );
        assertFalse( FieldVerifier.isValidName( "ab" ) );
        assertFalse( FieldVerifier.isValidName( "abc" ) );
        assertTrue( FieldVerifier.isValidName( "abcd" ) );
    }

}
