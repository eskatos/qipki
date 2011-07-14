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

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import org.qipki.clients.web.client.menu.Menu;

import org.qipki.clients.web.client.welcome.WelcomePlace;

/**
 * @see http://code.google.com/intl/fr/webtoolkit/doc/latest/DevGuideMvpActivitiesAndPlaces.html
 * @see http://tbroyer.posterous.com/gwt-21-activities-nesting-yagni
 */
public class qipkiweb
        implements EntryPoint
{

    private final Place defaultPlace = new WelcomePlace();
    private final SimplePanel mainPanel = new SimplePanel();
    private DockLayoutPanel globalLayout;

    public void onModuleLoad()
    {
        setupGlobalLayout();
        setupPlacesAndActivities();
    }

    private void setupGlobalLayout()
    {
        SplitLayoutPanel mainLayout = new SplitLayoutPanel();
        mainLayout.addWest( new HTML( "Vertical Master" ), 192 );
        mainLayout.addEast( new HTML( "Sidebar" ), 192 );
        mainLayout.addNorth( new HTML( "Horizontal Master" ), 192 );
        mainLayout.add( mainPanel );

        globalLayout = new DockLayoutPanel( Unit.PX );
        globalLayout.addNorth( new HTML( "Ribbon" ), 20 );
        globalLayout.addNorth( new Menu(), 80 );
        globalLayout.addSouth( new HTML( "Footer" ), 40 );
        globalLayout.add( mainLayout );
    }

    private void setupPlacesAndActivities()
    {
        ClientFactory clientFactory = new ClientFactoryImpl();

        ActivityMapper activityMapper = new MainActivityMapper( clientFactory );
        ActivityManager activityManager = new ActivityManager( activityMapper, clientFactory.getEventBus() );
        activityManager.setDisplay( mainPanel );

        MainPlaceHistoryMapper historyMapper = GWT.create( MainPlaceHistoryMapper.class );
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler( historyMapper );
        historyHandler.register( clientFactory.getPlaceController(), clientFactory.getEventBus(), defaultPlace );

        RootLayoutPanel.get().add( globalLayout );

        historyHandler.handleCurrentHistory();
    }

}
