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
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import org.qipki.clients.web.client.ui.Menu;
import org.qipki.clients.web.client.regions.EastSidebarActivityMapper;
import org.qipki.clients.web.client.regions.MainActivityMapper;
import org.qipki.clients.web.client.regions.WestSidebarActivityMapper;
import org.qipki.clients.web.client.ui.Footer;
import org.qipki.clients.web.client.ui.MainLayout;
import org.qipki.clients.web.client.ui.Ribbon;
import org.qipki.clients.web.client.welcome.WelcomePlace;

/**
 * @see http://code.google.com/intl/fr/webtoolkit/doc/latest/DevGuideMvpActivitiesAndPlaces.html
 * @see http://tbroyer.posterous.com/gwt-21-activities-nesting-yagni
 */
public class qipkiweb
        implements EntryPoint
{

    public void onModuleLoad()
    {
        try {

            ClientFactory clientFactory = new ClientFactoryImpl();
            MainLayout mainLayout = new MainLayout();

            // WestSidebar region
            ActivityMapper westSidebarActivityMapper = new WestSidebarActivityMapper( clientFactory );
            ActivityManager westSidebarActivityManager = new ActivityManager( westSidebarActivityMapper, clientFactory.getEventBus() );
            westSidebarActivityManager.setDisplay( mainLayout.getWestSidebarPanel() );

            // EastSidebar region
            ActivityMapper eastSidebarActivityMapper = new EastSidebarActivityMapper( clientFactory );
            ActivityManager eastSidebarActivityManager = new ActivityManager( eastSidebarActivityMapper, clientFactory.getEventBus() );
            eastSidebarActivityManager.setDisplay( mainLayout.getEastSidebarPanel() );

            // Main region
            ActivityMapper activityMapper = new MainActivityMapper( clientFactory );
            ActivityManager activityManager = new ActivityManager( activityMapper, clientFactory.getEventBus() );
            activityManager.setDisplay( mainLayout.getMainPanel() );

            // History handling
            PlaceHistoryMapper historyMapper = GWT.create( PlaceHistoryMapperImpl.class );
            PlaceHistoryHandler historyHandler = new PlaceHistoryHandler( historyMapper );
            historyHandler.register( clientFactory.getPlaceController(), clientFactory.getEventBus(), new WelcomePlace() );

            // Go!
            mainLayout.getRibbonPanel().setWidget( new Ribbon() );
            mainLayout.getMenuPanel().setWidget( new Menu( clientFactory ) );
            mainLayout.getFooterPanel().setWidget( new Footer() );
            RootLayoutPanel.get().add( mainLayout.getRootLayout() );
            historyHandler.handleCurrentHistory();

        } finally {
            hideLoading();
        }
    }

    private native void hideLoading()/*-{
    
    $wnd.loading_hide();
    
    }-*/;

}
