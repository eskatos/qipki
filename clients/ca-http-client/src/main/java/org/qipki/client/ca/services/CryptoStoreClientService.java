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
package org.qipki.client.ca.services;

import org.qipki.commons.rest.values.params.CryptoStoreFactoryParamsValue;
import org.qipki.commons.rest.values.representations.CryptoStoreValue;
import org.qipki.commons.rest.values.representations.RestListValue;
import org.qipki.commons.rest.values.representations.RestListValueIterable;

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
