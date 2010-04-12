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
package org.codeartisans.qipki.ca.presentation.rest;

import java.util.LinkedHashSet;
import java.util.Set;
import org.codeartisans.qipki.ca.domain.ca.CAEntity;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStoreEntity;
import org.codeartisans.qipki.commons.values.rest.RestListValue;
import org.codeartisans.qipki.commons.values.rest.RestValue;
import org.codeartisans.qipki.commons.values.rest.CAValue;
import org.codeartisans.qipki.commons.values.rest.CryptoStoreValue;
import org.qi4j.api.entity.Identity;
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

    CAValue ca( Reference parentRef, CAEntity ca );

    CryptoStoreValue cryptoStore( Reference parentRef, CryptoStoreEntity ks );

    Iterable<RestValue> asValues( Reference parentRef, Iterable objects );

    RestListValue newListRepresentationValue( Reference parentRef, int start, Iterable<RestValue> list );

    abstract class Mixin
            implements RestValuesFactory
    {

        private static final Logger LOGGER = LoggerFactory.getLogger( RestValuesFactory.class );
        @Structure
        private ValueBuilderFactory vbf;

        @Override
        public CAValue ca( Reference parentRef, CAEntity ca )
        {
            ValueBuilder<CAValue> caValueBuilder = vbf.newValueBuilder( CAValue.class );
            CAValue caValue = caValueBuilder.prototype();
            caValue.uri().set( appendIdentity( parentRef, ca ) );
            caValue.name().set( ca.name().get() );
            caValue.keystoreIdentity().set( ca.cryptoStore().get().identity().get() );
            return caValueBuilder.newInstance();
        }

        @Override
        public CryptoStoreValue cryptoStore( Reference parentRef, CryptoStoreEntity ks )
        {
            ValueBuilder<CryptoStoreValue> ksValueBuilder = vbf.newValueBuilder( CryptoStoreValue.class );
            CryptoStoreValue ksValue = ksValueBuilder.prototype();
            ksValue.name().set( ks.name().get() );
            ksValue.uri().set( appendIdentity( parentRef, ks ) );
            ksValue.storeType().set( ks.storeType().get() );
            ksValue.password().set( ks.password().get() );
            return ksValueBuilder.newInstance();
        }

        @Override
        public Iterable<RestValue> asValues( Reference parentRef, Iterable objects )
        {
            Set<RestValue> set = new LinkedHashSet<RestValue>();
            for ( Object eachObj : objects ) {
                try {
                    set.add( ca( parentRef, ( CAEntity ) eachObj ) );
                    continue;
                } catch ( ClassCastException ex ) {
                    LOGGER.trace( "Object is not a CAEntity: {}", ex.getMessage() );
                }
                try {
                    set.add( cryptoStore( parentRef, ( CryptoStoreEntity ) eachObj ) );
                    continue;
                } catch ( ClassCastException ex ) {
                    LOGGER.trace( "Object is not a CryptoStoreEntity: {}", ex.getMessage() );
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

        private String appendIdentity( Reference parentRef, Identity identity )
        {
            return new StringBuilder( parentRef.toString() ).append( "/" ).append( identity.identity().get() ).toString();
        }

    }

}
