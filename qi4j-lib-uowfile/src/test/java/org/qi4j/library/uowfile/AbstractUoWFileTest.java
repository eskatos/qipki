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
package org.qi4j.library.uowfile;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import org.qi4j.core.testsupport.AbstractQi4jTest;

public abstract class AbstractUoWFileTest
        extends AbstractQi4jTest
{

    protected static File baseTestDir;

    @BeforeClass
    public static void beforeClass()
            throws IOException
    {
        File testDir = new File( "target/" + System.getProperty( "test" ) );
        if ( !testDir.exists() ) {
            if ( !testDir.mkdirs() ) {
                throw new IOException( "Unable to create directory: " + testDir );
            }
        }
        baseTestDir = testDir;
    }

    @AfterClass
    public static void afterClass()
    {
        // Delete test data
        Stack<File> stack = new Stack<File>();
        stack.push( baseTestDir );
        while ( !stack.empty() ) {
            File each = stack.peek();
            if ( each.isDirectory() ) {
                File[] children = each.listFiles();
                if ( children.length > 0 ) {
                    for ( File child : children ) {
                        stack.push( child );
                    }
                } else {
                    stack.pop().delete();
                }
            } else {
                stack.pop().delete();
            }
        }
    }

}