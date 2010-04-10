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
package org.codeartisans.qipki.ca.presentation.rest.values;

import java.util.LinkedHashSet;
import java.util.Set;
import org.codeartisans.qipki.ca.domain.ca.CA;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.value.ValueBuilder;
import org.qi4j.api.value.ValueBuilderFactory;
import org.restlet.data.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mixins( RestValuesFactory.Mixin.class )
public interface RestValuesFactory
        extends ServiceComposite
{

    CAValue ca( Reference parentRef, CA ca );

    Iterable<RestValue> asValues( Reference parentRef, Iterable objects );

    RestListValue newListRepresentationValue( Reference parentRef, int start, Iterable<RestValue> list );

    abstract class Mixin
            implements RestValuesFactory
    {

        private static final Logger LOGGER = LoggerFactory.getLogger( RestValuesFactory.class );
        @Structure
        private ValueBuilderFactory vbf;

        @Override
        public CAValue ca( Reference parentRef, CA ca )
        {
            ValueBuilder<CAValue> caValueBuilder = vbf.newValueBuilder( CAValue.class );
            CAValue caValue = caValueBuilder.prototype();
            caValue.uri().set( new StringBuilder( parentRef.toString() ).append( "/" ).append( ca.identity().get() ).toString() );
            caValue.name().set( ca.name().get() );
            return caValueBuilder.newInstance();
        }

        @Override
        public Iterable<RestValue> asValues( Reference parentRef, Iterable objects )
        {
            Set<RestValue> set = new LinkedHashSet<RestValue>();
            for ( Object eachObj : objects ) {
                try {
                    set.add( ca( parentRef, ( CA ) eachObj ) );
                    continue;
                } catch ( ClassCastException ex ) {
                    LOGGER.trace( "Object is not a CA", ex );
                }
                throw new IllegalArgumentException( "Entity is of unsupported Type: " + eachObj );
            }
            return set;
        }

        @Override
        public RestListValue newListRepresentationValue( Reference listRef, int start, Iterable<RestValue> items )
        {
            ValueBuilder<RestListValue> builder = vbf.newValueBuilder( RestListValue.class );
            RestListValue listRepresentation = builder.prototype();
            listRepresentation.uri().set( listRef.toString() );
            listRepresentation.start().set( start );
            for ( RestValue eachItem : items ) {
                listRepresentation.items().get().add( eachItem );
            }
            return builder.newInstance();
        }

    }

}
