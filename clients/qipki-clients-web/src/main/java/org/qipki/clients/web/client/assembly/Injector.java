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
package org.qipki.clients.web.client.assembly;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.name.Named;

import org.qipki.clients.web.client.ClientFactory;
import org.qipki.clients.web.client.regions.RegionNames;
import org.qipki.clients.web.client.ui.MainLayout;

@GinModules( AssemblyModule.class )
public interface Injector
        extends Ginjector
{

    ClientFactory getClientFactory();

    EventBus getEventBus();

    PlaceController getPlaceController();

    @Named( RegionNames.WEST )
    ActivityMapper getWestActivityMapper();

    @Named( RegionNames.EAST )
    ActivityMapper getEastActivityMapper();

    @Named( RegionNames.NORTH )
    ActivityMapper getNorthActivityMapper();

    @Named( RegionNames.SOUTH )
    ActivityMapper getSouthActivityMapper();

    @Named( RegionNames.MAIN )
    ActivityMapper getMainActivityMapper();

    PlaceHistoryHandler getPlaceHistoryHandler();

    PlaceHistoryMapper getPlaceHistoryMapper();

    MainLayout getMainLayout();

}
