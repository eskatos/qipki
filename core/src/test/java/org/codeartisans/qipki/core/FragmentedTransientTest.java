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
package org.codeartisans.qipki.core;

import org.junit.Test;

import org.qi4j.api.composite.TransientComposite;
import org.qi4j.api.injection.scope.This;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.test.AbstractQi4jTest;

@SuppressWarnings( "PublicInnerClass" )
public class FragmentedTransientTest
        extends AbstractQi4jTest
{

    @SuppressWarnings( "unchecked" )
    @Override
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        module.addTransients( StoryTellerComposite.class ).withMixins( HelloMixin.class, ByeMixin.class );
    }

    @Test
    public void test()
    {
        StoryTellerComposite st = transientBuilderFactory.newTransient( StoryTellerComposite.class );
        System.out.println( st.sayHello( "Bonnie" ) );
        System.out.println( st.sayBye( "Bonnie" ) );
    }

    public static interface StoryTeller
            extends HelloTeller, ByeTeller
    {
    }

    public static interface HelloTeller
    {

        String sayHello( String name );

    }

    public static interface ByeTeller
    {

        String sayBye( String name );

    }

    // @Mixins( value = { HelloMixin.class, ByeMixin.class } )
    public static interface StoryTellerComposite
            extends StoryTeller, TransientComposite
    {
    }

    public abstract static class HelloMixin
            implements StoryTeller
    {

        @Override
        public String sayHello( String name )
        {
            return "Hello " + name;
        }

    }

    public abstract static class ByeMixin
            implements StoryTeller
    {

        @This
        private StoryTeller composited;

        @Override
        public String sayBye( String name )
        {
            System.out.println( "Composited: " + composited.sayHello( "Clyde" ) );
            return "Bye " + name;
        }

    }

}
