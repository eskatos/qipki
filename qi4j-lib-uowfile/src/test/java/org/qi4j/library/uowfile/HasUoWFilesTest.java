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
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import org.qi4j.api.entity.EntityComposite;
import org.qi4j.api.entity.Identity;
import org.qi4j.api.injection.scope.This;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.property.Property;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.library.uowfile.plural.HasUoWFilesLifecycle;
import org.qi4j.library.uowfile.plural.UoWFilesLocator;
import org.qi4j.test.EntityTestAssembler;

public class HasUoWFilesTest
        extends AbstractUoWFileTest
{

    public enum MyEnum
    {

        fileOne, fileTwo
    }

    @Mixins( HasUoWFilesTest.OneEntityTwoFilesMixin.class )
    public interface OneEntityTwoFilesEntity
            extends HasUoWFilesLifecycle<MyEnum>, EntityComposite
    {

        Property<String> name();

    }

    public static abstract class OneEntityTwoFilesMixin
            implements OneEntityTwoFilesEntity, UoWFilesLocator<MyEnum>
    {

        @This
        private Identity meAsIdentity;

        @Override
        public Iterable<File> locateAttachedFiles()
        {
            List<File> list = new ArrayList<File>();
            for ( MyEnum eachValue : MyEnum.values() ) {
                list.add( new File( baseTestDir, meAsIdentity.identity().get() + "." + eachValue.name() ) );
            }
            return list;
        }

        @Override
        public File locateAttachedFile( MyEnum key )
        {
            return new File( baseTestDir, meAsIdentity.identity().get() + "." + key.name() );
        }

    }

    @Override
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        module.entities( OneEntityTwoFilesEntity.class );
        new UoWFileAssembler().assemble( module );
        new EntityTestAssembler().assemble( module );
    }

    @Test
    public void test()
    {
        // TODO
    }

}
