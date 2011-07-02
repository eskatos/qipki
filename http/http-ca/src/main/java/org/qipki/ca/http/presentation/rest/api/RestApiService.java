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
package org.qipki.ca.http.presentation.rest.api;

import org.codeartisans.java.toolbox.StringUtils;

import org.qi4j.api.common.Optional;
import org.qi4j.api.configuration.Configuration;
import org.qi4j.api.injection.scope.This;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;

import org.restlet.data.Reference;

@Mixins( RestApiService.Mixin.class )
public interface RestApiService
        extends ServiceComposite
{

    void setApiRootRef( Reference detectedRootRef );

    Reference apiRootRef();

    abstract class Mixin
            implements RestApiService
    {

        private Reference apiRootRef = new Reference();
        @This
        @Optional
        private Configuration<RestApiConfiguration> config;

        @Override
        public void setApiRootRef( Reference detected )
        {
            config.refresh();
            if ( config == null || config.configuration() == null ) {
                // Configuration was null, will use detected
                apiRootRef = detected;
                return;
            }
            String configured = config.configuration().clientBaseUrl().get();
            if ( StringUtils.isEmpty( configured ) ) {
                // Configured clientBaseUrl was empty, will use detected
                apiRootRef = detected;
                return;
            }
            // Using configured clientBaseUrl
            apiRootRef = new Reference( configured );
        }

        @Override
        public Reference apiRootRef()
        {
            return apiRootRef;
        }

    }

}
