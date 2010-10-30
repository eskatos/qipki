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
