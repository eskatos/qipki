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
package org.qipki.clients.web.client.ui.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.inject.Inject;

import org.qipki.clients.web.client.config.ConfigPlace;
import org.qipki.clients.web.client.logs.LogsPlace;
import org.qipki.clients.web.client.tasks.TasksPlace;
import org.qipki.clients.web.client.tools.ToolsPlace;
import org.qipki.clients.web.client.welcome.WelcomePlace;

public class Menu
        extends Composite
{

    private static final MenuUiBinder binder = GWT.create( MenuUiBinder.class );

    @UiTemplate( "Menu.ui.xml" )
    interface MenuUiBinder
            extends UiBinder<Panel, Menu>
    {
    }

    private final PlaceController placeController;

    @Inject
    public Menu( PlaceController placeController )
    {
        this.placeController = placeController;
        initWidget( binder.createAndBindUi( this ) );
    }

    @UiHandler( "buttonWelcome" )
    public void doClickWelcome( ClickEvent click )
    {
        placeController.goTo( new WelcomePlace() );
    }

    @UiHandler( "buttonX509VA" )
    public void doX509VA( ClickEvent click )
    {
    }

    @UiHandler( "buttonX509RA" )
    public void doX509RA( ClickEvent click )
    {
    }

    @UiHandler( "buttonX509CA" )
    public void doX509CA( ClickEvent click )
    {
    }

    @UiHandler( "buttonLogs" )
    public void doLogs( ClickEvent click )
    {
        placeController.goTo( new LogsPlace() );
    }

    @UiHandler( "buttonTasks" )
    public void doTasks( ClickEvent click )
    {
        placeController.goTo( new TasksPlace() );
    }

    @UiHandler( "buttonConfiguration" )
    public void doClickConfiguration( ClickEvent click )
    {
        placeController.goTo( new ConfigPlace() );
    }

    @UiHandler( "buttonTools" )
    public void doClickTools( ClickEvent click )
    {
        placeController.goTo( new ToolsPlace() );
    }

}
