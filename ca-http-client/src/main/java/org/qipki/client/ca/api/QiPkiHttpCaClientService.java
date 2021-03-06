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
package org.qipki.client.ca.api;

import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qipki.commons.rest.values.params.ParamsFactory;

@Mixins( QiPkiHttpCaClientService.Mixin.class )
public interface QiPkiHttpCaClientService
        extends QiPkiHttpCaClient, ServiceComposite
{

    abstract class Mixin
            implements QiPkiHttpCaClient
    {

        @Service
        private ParamsFactory paramsFactory;
        @Service
        private CryptoStoreClientService cryptoStoreClient;
        @Service
        private CAClientService caClient;

        @Override
        public ParamsFactory paramsFactory()
        {
            return paramsFactory;
        }

        @Override
        public CryptoStoreClientService cryptoStore()
        {
            return cryptoStoreClient;
        }

        @Override
        public CAClientService ca()
        {
            return caClient;
        }

    }

}
