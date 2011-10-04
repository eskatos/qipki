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
package org.qipki.testsupport;

import java.io.File;
import java.io.IOException;
import org.codeartisans.java.toolbox.Strings;
import org.qi4j.library.fileconfig.FileConfigurationOverride;

public class QiPkiTestSupport
{

    public static File ensureTestDirectory( String subdir )
            throws IOException
    {
        File testDir = new File( "target/" + System.getProperty( "test" ) );
        if ( !Strings.isEmpty( subdir ) ) {
            testDir = new File( testDir, subdir );
        }
        if ( !testDir.exists() ) {
            if ( !testDir.mkdirs() ) {
                throw new IOException( "Unable to create directory: " + testDir );
            }
        }
        return testDir;
    }

    public static File ensureTestDirectory()
            throws IOException
    {
        return ensureTestDirectory( null );
    }

    public static FileConfigurationOverride fileConfigTestOverride( String testCodeName )
    {
        return new FileConfigurationOverride().withConfiguration( new File( "target/" + testCodeName + "-qi4j-configuration" ) ).
                withData( new File( "target/" + testCodeName + "-qi4j-data" ) ).
                withCache( new File( "target/" + testCodeName + "-qi4j-cache" ) ).
                withTemporary( new File( "target/" + testCodeName + "-qi4j-temporary" ) ).
                withLog( new File( "target/" + testCodeName + "-qi4j-log" ) );
    }

}
