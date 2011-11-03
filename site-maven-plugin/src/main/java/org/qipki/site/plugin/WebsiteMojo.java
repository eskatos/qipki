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
package org.qipki.site.plugin;

import com.petebevin.markdown.MarkdownProcessor;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Stack;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.FileUtils;

/**
 * @goal site
 */
//
// @parameter [alias="someAlias"] [expression="${someExpression}"] [default-value="value"]
// @required
// @readonly
// @component role="org.codehaus.plexus.archiver.Archiver" roleHint="zip"
// @parameter expression="${component.org.codehaus.plexus.archiver.Archiver#zip}"
//
public class WebsiteMojo
        extends AbstractMojo
{

    /**
     * @parameter default-value="${project.basedir}/src/main/website/static"
     */
    private File staticDirectory;
    /**
     * @parameter default-value="${project.basedir}/src/main/website/markup"
     */
    private File markupDirectory;
    /**
     * @parameter default-value="false"
     */
    private boolean staticLast;
    /**
     * @parameter default-value="${project.build.directory}/website"
     */
    private File outputDirectory;

    @Override
    public void execute()
            throws MojoExecutionException, MojoFailureException
    {
        FileUtils.mkdir( outputDirectory.getAbsolutePath() );
        if ( staticLast ) {
            if ( validateSource( "markup", markupDirectory ) ) {
                doMarkup();
            }
            if ( validateSource( "static", staticDirectory ) ) {
                doStatic();
            }
        } else {
            if ( validateSource( "static", staticDirectory ) ) {
                doStatic();
            }
            if ( validateSource( "markup", markupDirectory ) ) {
                doMarkup();
            }
        }
    }

    private void doStatic()
            throws MojoExecutionException
    {
        getLog().info( ">>> Website Maven Plugin :: doStatic( " + staticDirectory + " )" );
        try {
            FileUtils.copyDirectoryStructure( staticDirectory, outputDirectory );
        } catch ( IOException ex ) {
            throw new MojoExecutionException( "Unable to copy static content: " + ex.getMessage(), ex );
        }
    }

    private void doMarkup()
            throws MojoExecutionException
    {
        getLog().info( ">>> Website Maven Plugin :: doMarkup( " + markupDirectory + ")" );
        try {
            MarkdownProcessor md = new MarkdownProcessor();
            FileFilter fileFilter = new MarkdownFileFilter();
            Stack<File> stack = new Stack<File>();
            stack.addAll( Arrays.asList( markupDirectory.listFiles( fileFilter ) ) );
            while ( !stack.empty() ) {
                File eachFile = stack.pop();
                // Recurse if needed
                if ( eachFile.isDirectory() ) {
                    stack.addAll( Arrays.asList( eachFile.listFiles( fileFilter ) ) );
                    getLog().info( "Added descendant of " + eachFile );
                    continue;
                }
                // Resolve target path
                File target = resolveTargetPath( eachFile, "html", markupDirectory );
                getLog().info( "Will process " + eachFile + " to " + target );
                // Process Markdown
                String input = FileUtils.fileRead( eachFile, "UTF-8" );
                String output = md.markdown( input ).trim();
                // Save to target path
                FileUtils.fileWrite( target, "UTF-8", output );
            }
        } catch ( IOException ex ) {
            throw new MojoExecutionException( "Unable to process markup content: " + ex.getMessage(), ex );
        }
    }

    private File resolveTargetPath( File file, String newExtension, File inputBaseDir )
    {
        String originalRelativePath = inputBaseDir.toURI().relativize( file.toURI() ).getPath();
        String targetRelativePath = originalRelativePath.replaceAll( "md$", newExtension );
        return new File( outputDirectory, targetRelativePath );
    }

    /**
     * Accept only directories or files with the .md extension.
     */
    private static class MarkdownFileFilter
            implements FileFilter
    {

        @Override
        public boolean accept( File file )
        {
            return file.isDirectory() || file.getName().endsWith( ".md" );
        }

    }

    private boolean validateSource( String name, File directory )
    {
        if ( !directory.exists() || directoryIsEmpty( directory ) ) {
            getLog().warn( name + "Directory does not exists or is empty" );
            return false;
        }
        return true;
    }

    private static boolean directoryIsEmpty( File directory )
    {
        File[] files = directory.listFiles();
        return files == null || files.length == 0;
    }

}
