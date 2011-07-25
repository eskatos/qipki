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
package org.qipki.clients.web.client.tasks;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import org.qipki.clients.web.client.ui.place.StringTokenPlace;

public class TasksPlace
        extends StringTokenPlace
{

    public static final String TIMELINE = "timeline";

    public TasksPlace()
    {
        super( TIMELINE );
    }

    TasksPlace( String token )
    {
        super( token );
    }

    @Prefix( "tasks" )
    public static class Tokenizer
            implements PlaceTokenizer<TasksPlace>
    {

        @Override
        public TasksPlace getPlace( String token )
        {
            return new TasksPlace( token );
        }

        @Override
        public String getToken( TasksPlace place )
        {
            return place.getToken();
        }

    }

}
