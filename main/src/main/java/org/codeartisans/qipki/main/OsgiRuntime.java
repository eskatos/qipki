/*
 * Copyright (c) 2010 Paul Merlin <paul@nosphere.org>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.codeartisans.qipki.main;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import org.codeartisans.java.toolbox.exceptions.NullArgumentException;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OsgiRuntime
{

    private static final Logger LOGGER = LoggerFactory.getLogger( OsgiRuntime.class );
    private final File bundleDir;
    private final File cacheDir;
    private Framework osgiFramework;

    public OsgiRuntime( File bundleDir, File cacheDir )
    {
        ensureDirectories( bundleDir, cacheDir );
        this.bundleDir = bundleDir;
        this.cacheDir = cacheDir;
    }

    public void start()
            throws BundleException
    {
        Map<String, String> felixConfig = new HashMap<String, String>();
        felixConfig.put( Constants.FRAMEWORK_STORAGE, cacheDir.getPath() );
        osgiFramework = ServiceLoader.load( FrameworkFactory.class ).iterator().next().newFramework( felixConfig );
        osgiFramework.init();
        if ( bundleDir.exists() && bundleDir.isDirectory() ) {
            File[] bundleJars = bundleDir.listFiles( new FilenameFilter()
            {

                @Override
                public boolean accept( File dir, String name )
                {
                    return name.endsWith( ".jar" );
                }

            } );
            Set<Bundle> installedBundles = new HashSet<Bundle>();
            for ( File eachBundleJar : bundleJars ) {
                Bundle eachBundle = osgiFramework.getBundleContext().installBundle( eachBundleJar.toURI().toString() );
                installedBundles.add( eachBundle );
            }
            for ( Bundle eachBundle : installedBundles ) {
                eachBundle.start();
            }
        }
        osgiFramework.start();
    }

    public void stop()
            throws BundleException
    {
        osgiFramework.stop();
    }

    public void waitForStop()
            throws InterruptedException
    {
        osgiFramework.waitForStop( 0 );
    }

    private void ensureDirectories( File bundleDir, File cacheDir )
    {
        NullArgumentException.ensureNotNull( "Bundle Dir", bundleDir );
        NullArgumentException.ensureNotNull( "Cache Dir", cacheDir );
        if ( !bundleDir.exists() ) {
            throw new IllegalArgumentException( "Bundle Dir does not exists: " + bundleDir );
        }
        if ( !bundleDir.isDirectory() ) {
            throw new IllegalArgumentException( "Bundle Dir is not a directory: " + bundleDir );
        }
        if ( !cacheDir.mkdirs() ) {
            throw new IllegalArgumentException( "Unable to create Cache Dir: " + cacheDir );
        }
    }

}
