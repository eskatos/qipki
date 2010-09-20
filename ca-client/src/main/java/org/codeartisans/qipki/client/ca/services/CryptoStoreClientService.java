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
package org.codeartisans.qipki.client.ca.services;

import org.codeartisans.qipki.commons.rest.values.params.CryptoStoreFactoryParamsValue;
import org.codeartisans.qipki.commons.rest.values.representations.CryptoStoreValue;
import org.codeartisans.qipki.commons.rest.values.representations.RestListValue;
import org.codeartisans.qipki.commons.rest.values.representations.RestListValueIterable;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.value.ValueBuilderFactory;

@Mixins( CryptoStoreClientService.Mixin.class )
public interface CryptoStoreClientService
        extends GenericClientService<CryptoStoreValue>, ServiceComposite
{

    CryptoStoreValue create( CryptoStoreFactoryParamsValue params );

    abstract class Mixin
            implements CryptoStoreClientService
    {

        @Structure
        private ValueBuilderFactory valueBuilderFactory;
        @Service
        private RestClientService restClientService;

        @Override
        public Iterable<CryptoStoreValue> list( int start )
        {
            String jsonCryptoStoreList = restClientService.getJSON( restClientService.fetchApiURIs().cryptoStoreListUri().get() );
            RestListValue restList = valueBuilderFactory.newValueFromJSON( RestListValue.class, jsonCryptoStoreList );
            return new RestListValueIterable<CryptoStoreValue>( restList );
        }

        @Override
        public CryptoStoreValue get( String uri )
        {
            throw new UnsupportedOperationException( "Not supported yet." );
        }

        @Override
        public CryptoStoreValue create( CryptoStoreFactoryParamsValue params )
        {
            throw new UnsupportedOperationException( "Not supported yet." );
        }

    }

}
