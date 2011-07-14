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
package org.qipki.clients.web.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

public class MainController
{

    private final EventBus eventBus;
    private HasWidgets container;

    public MainController( EventBus eventBus )
    {
        this.eventBus = eventBus;
    }

    public void go( final HasWidgets container )
    {
        this.container = container;
        if ( "".equals( History.getToken() ) ) {
            History.newItem( "welcome" );
        } else {
            History.fireCurrentHistoryState();
        }
    }

}
