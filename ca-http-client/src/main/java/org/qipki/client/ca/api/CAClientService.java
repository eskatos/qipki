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
package org.qipki.client.ca.api;

import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.value.ValueBuilderFactory;

import org.qipki.client.ca.spi.RestClientService;
import org.qipki.commons.rest.values.params.CAFactoryParamsValue;
import org.qipki.commons.rest.values.representations.CAValue;
import org.qipki.commons.rest.values.representations.RestListValue;
import org.qipki.commons.rest.values.representations.RestListValueIterable;

@Mixins( CAClientService.Mixin.class )
public interface CAClientService
        extends GenericClientService<CAValue>, ServiceComposite
{

    CAValue create( CAFactoryParamsValue params );

    abstract class Mixin
            implements CAClientService
    {

        @Service
        private RestClientService restClient;
        @Structure
        private ValueBuilderFactory vbf;

        @Override
        public Iterable<CAValue> list( int start )
        {
            String jsonCaList = restClient.getJSON( restClient.fetchApiURIs().caListUri().get() );
            RestListValue restList = vbf.newValueFromJSON( RestListValue.class, jsonCaList );
            return new RestListValueIterable<CAValue>( restList );
        }

        @Override
        public CAValue get( String uri )
        {
            String jsonCa = restClient.getJSON( uri );
            return vbf.newValueFromJSON( CAValue.class, jsonCa );
        }

        @Override
        public CAValue create( CAFactoryParamsValue params )
        {
            String jsonCa = restClient.postJSON( restClient.fetchApiURIs().caListUri().get(), params.toString() );
            return vbf.newValueFromJSON( CAValue.class, jsonCa );
        }

    }

}
