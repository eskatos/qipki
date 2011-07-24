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

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import org.qipki.clients.web.client.ClientFactory;
import org.qipki.clients.web.client.ClientFactoryImpl;
import org.qipki.clients.web.client.Context;
import org.qipki.clients.web.client.ContextImpl;
import org.qipki.clients.web.client.PlaceHistoryMapperImpl;
import org.qipki.clients.web.client.config.ConfigWestSidebarView;
import org.qipki.clients.web.client.regions.EastSidebarActivityMapper;
import org.qipki.clients.web.client.regions.MainActivityMapper;
import org.qipki.clients.web.client.regions.NorthSidebarActivityMapper;
import static org.qipki.clients.web.client.regions.RegionNames.*;
import org.qipki.clients.web.client.regions.SouthSidebarActivityMapper;
import org.qipki.clients.web.client.regions.WestSidebarActivityMapper;
import org.qipki.clients.web.client.ui.MainLayout;
import org.qipki.clients.web.client.ui.Menu;
import org.qipki.clients.web.client.welcome.WelcomeMainActivity;
import org.qipki.clients.web.client.welcome.WelcomeMainView;
import org.qipki.clients.web.client.welcome.WelcomePlace;

/**
 * @see http://wanderingcanadian.posterous.com/hello-mvp-with-gin
 */
public class AssemblyModule
        extends AbstractGinModule
{

    @Override
    protected void configure()
    {
        bind( ClientFactory.class ).to( ClientFactoryImpl.class ).in( Singleton.class );

        bind( Context.class ).to( ContextImpl.class ).in( Singleton.class );
        bind( EventBus.class ).to( SimpleEventBus.class ).in( Singleton.class );

        bind( ActivityMapper.class ).annotatedWith( Names.named( WEST ) ).to( WestSidebarActivityMapper.class ).in( Singleton.class );
        bind( ActivityMapper.class ).annotatedWith( Names.named( EAST ) ).to( EastSidebarActivityMapper.class ).in( Singleton.class );
        bind( ActivityMapper.class ).annotatedWith( Names.named( NORTH ) ).to( NorthSidebarActivityMapper.class ).in( Singleton.class );
        bind( ActivityMapper.class ).annotatedWith( Names.named( SOUTH ) ).to( SouthSidebarActivityMapper.class ).in( Singleton.class );
        bind( ActivityMapper.class ).annotatedWith( Names.named( MAIN ) ).to( MainActivityMapper.class ).in( Singleton.class );
        bind( PlaceHistoryMapper.class ).to( PlaceHistoryMapperImpl.class ).in( Singleton.class );

        bind( MainLayout.class ).in( Singleton.class );
        bind( Menu.class ).in( Singleton.class );

        bind( ConfigWestSidebarView.class ).in( Singleton.class );

        bind( WelcomeMainView.class ).in( Singleton.class );
        bind( WelcomeMainActivity.class );
    }

    @Provides
    @Singleton
    @Named( WEST )
    public ActivityManager getWestActivityManager( @Named( WEST ) ActivityMapper mapper, EventBus eventBus, MainLayout mainLayout )
    {
        ActivityManager activityManager = new ActivityManager( mapper, eventBus );
        activityManager.setDisplay( mainLayout.getWestSidebarPanel() );
        return activityManager;
    }

    @Provides
    @Singleton
    @Named( EAST )
    public ActivityManager getEastActivityManager( @Named( EAST ) ActivityMapper mapper, EventBus eventBus, MainLayout mainLayout )
    {
        ActivityManager activityManager = new ActivityManager( mapper, eventBus );
        activityManager.setDisplay( mainLayout.getEastSidebarPanel() );
        return activityManager;
    }

    @Provides
    @Singleton
    @Named( NORTH )
    public ActivityManager getNorthActivityManager( @Named( NORTH ) ActivityMapper mapper, EventBus eventBus, MainLayout mainLayout )
    {
        ActivityManager activityManager = new ActivityManager( mapper, eventBus );
        activityManager.setDisplay( mainLayout.getNorthSidebarPanel() );
        return activityManager;
    }

    @Provides
    @Singleton
    @Named( SOUTH )
    public ActivityManager getSouthActivityManager( @Named( SOUTH ) ActivityMapper mapper, EventBus eventBus, MainLayout mainLayout )
    {
        ActivityManager activityManager = new ActivityManager( mapper, eventBus );
        activityManager.setDisplay( mainLayout.getSouthSidebarPanel() );
        return activityManager;
    }

    @Provides
    @Singleton
    @Named( MAIN )
    public ActivityManager getMainActivityManager( @Named( MAIN ) ActivityMapper mapper, EventBus eventBus, MainLayout mainLayout )
    {
        ActivityManager activityManager = new ActivityManager( mapper, eventBus );
        activityManager.setDisplay( mainLayout.getMainPanel() );
        return activityManager;
    }

    @Provides
    @Singleton
    // ActivityManager are injected here to force their instanciation when PlaceHistoryHandler is requested
    public PlaceHistoryHandler getPlaceHistoryHandler( PlaceController placeController,
                                                       PlaceHistoryMapper placeHistoryMapper,
                                                       EventBus eventBus,
                                                       @Named( WEST ) ActivityManager westActivityManager,
                                                       @Named( EAST ) ActivityManager eastActivityManager,
                                                       @Named( NORTH ) ActivityManager northActivityManager,
                                                       @Named( SOUTH ) ActivityManager southActivityManager,
                                                       @Named( MAIN ) ActivityManager mainActivityManager )
    {
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler( placeHistoryMapper );
        historyHandler.register( placeController, eventBus, new WelcomePlace() );
        return historyHandler;
    }

    @Provides
    @Singleton
    public PlaceController getPlaceController( EventBus eventBus )
    {
        return new PlaceController( eventBus );
    }

}
