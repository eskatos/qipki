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
package org.qipki.clients.web.client.ui.activity;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

import org.qipki.clients.web.client.config.ConfigPlace;
import org.qipki.clients.web.client.config.ConfigWestActivity;
import org.qipki.clients.web.client.tasks.TasksPlace;
import org.qipki.clients.web.client.tasks.TasksWestActivity;
import org.qipki.clients.web.client.tools.ToolsPlace;
import org.qipki.clients.web.client.tools.ToolsWestActivity;

public class WestActivityMapper
        implements ActivityMapper
{

    private final Provider<TasksWestActivity> tasksActivityProvider;
    private final Provider<ConfigWestActivity> configActivityProvider;
    private final Provider<ToolsWestActivity> toolsActivityProvider;

    @Inject
    public WestActivityMapper( Provider<TasksWestActivity> tasksActivityProvider,
                               Provider<ConfigWestActivity> configActivityProvider,
                               Provider<ToolsWestActivity> toolsActivityProvider )
    {
        this.tasksActivityProvider = tasksActivityProvider;
        this.configActivityProvider = configActivityProvider;
        this.toolsActivityProvider = toolsActivityProvider;
    }

    @Override
    public Activity getActivity( Place place )
    {
        if ( place instanceof TasksPlace ) {
            return tasksActivityProvider.get().withPlace( ( TasksPlace ) place );
        } else if ( place instanceof ConfigPlace ) {
            return configActivityProvider.get().withPlace( ( ConfigPlace ) place );
        } else if ( place instanceof ToolsPlace ) {
            return toolsActivityProvider.get().withPlace( ( ToolsPlace ) place );
        }
        return null;
    }

}
