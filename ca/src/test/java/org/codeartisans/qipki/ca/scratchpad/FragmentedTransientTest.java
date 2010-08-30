package org.codeartisans.qipki.ca.scratchpad;

import org.junit.Test;

import org.qi4j.api.composite.TransientComposite;
import org.qi4j.api.injection.scope.This;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.test.AbstractQi4jTest;

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