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
package org.qipki.clients.web.client.tools;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class ToolsWestActivity
        extends AbstractActivity
{

    private final ToolsWestView view;
    private ToolsPlace place;

    @Inject
    public ToolsWestActivity( ToolsWestView view )
    {
        this.view = view;
    }

    public ToolsWestActivity withPlace( ToolsPlace place )
    {
        this.place = place;
        return this;
    }

    @Override
    public void start( AcceptsOneWidget panel, EventBus eventBus )
    {
        panel.setWidget( view.asWidget() );
    }

}
