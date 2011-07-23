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
package org.qipki.clients.web.client.config;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class ConfigPlace
        extends Place
{

    public static final String GENERAL = "general";
    public static final String MESSAGING = "messaging";
    public static final String SCHEDULER = "scheduler";
    private String token;

    public ConfigPlace()
    {
        this( GENERAL );
    }

    public ConfigPlace( String token )
    {
        this.token = token;
    }

    public String getToken()
    {
        return token;
    }

    @Prefix( "config" )
    public static class Tokenizer
            implements PlaceTokenizer<ConfigPlace>
    {

        @Override
        public String getToken( ConfigPlace place )
        {
            return place.token;
        }

        @Override
        public ConfigPlace getPlace( String token )
        {
            return new ConfigPlace( token );
        }

    }

}
