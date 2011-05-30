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
package org.qipki.reflect;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import org.qi4j.spi.structure.ApplicationModelSPI;
import org.qipki.reflect.beans.ApplicationBean;
import org.qipki.reflect.beans.ApplicationBeanVisitor;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

public class StringTemplateStructureReflector
        implements StructureReflector
{

    /**
     * Output the Application structure in plain text.
     * 
     * Global sequence
     * <ul>
     * <li>Visit the whole ApplicationModel gathering all needed data in ApplicationBeanVisitor</li>
     * <li>Load a StringTemplate adding the ApplicationBean built by ApplicationBeanVisitor to its context</li>
     * <li>Render the template in the given StringWriter</li>
     * </ul>
     */
    @Override
    public void writePlainTextStructure( ApplicationModelSPI applicationModel, StringWriter writer )
    {
        writeTextStructure( applicationModel, writer, "ascii-application.st" );
    }

    @Override
    public void writeHtmlStructure( ApplicationModelSPI applicationModel, StringWriter writer )
    {
        writeTextStructure( applicationModel, writer, "html-application.st" );
    }

    private void writeTextStructure( ApplicationModelSPI applicationModel, StringWriter writer, String templateName )
    {
        ApplicationBean appBean = buildBean( applicationModel );
        ST template = readTemplate( templateName );
        template.add( "app", appBean );
        writer.append( template.render() ).append( "\n" );
    }

    private ApplicationBean buildBean( ApplicationModelSPI applicationModel )
    {
        ApplicationBeanVisitor visitor = new ApplicationBeanVisitor();
        applicationModel.visitDescriptor( visitor );
        return visitor.applicationBean();
    }

    private ST readTemplate( String templateName )
    {
        InputStream is = null;
        try {

            final char[] buffer = new char[ 0x10000 ];
            StringBuilder out = new StringBuilder();
            is = getClass().getResourceAsStream( templateName );
            Reader in = new InputStreamReader( is, "UTF-8" );
            int read;
            do {
                read = in.read( buffer, 0, buffer.length );
                if ( read > 0 ) {
                    out.append( buffer, 0, read );
                }
            } while ( read >= 0 );

            String template = out.toString();
            STGroup group = new STGroup( '$', '$' );
            group.defineTemplate( "structure", "app", template );
            return group.getInstanceOf( "structure" );

        } catch ( IOException ex ) {
            throw new RuntimeException( ex.getMessage(), ex );
        } finally {
            if ( is != null ) {
                try {
                    is.close();
                } catch ( IOException ex ) {
                }
            }
        }
    }

}
