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
package org.qipki.clients.web.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;

import org.qipki.clients.web.client.ClientFactory;
import org.qipki.clients.web.client.configuration.ConfigurationPlace;
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

    private final ClientFactory factory;

    public Menu( ClientFactory factory )
    {
        this.factory = factory;
        initWidget( binder.createAndBindUi( this ) );
    }

    @UiHandler( "buttonWelcome" )
    public void doClickWelcome( ClickEvent click )
    {
        factory.getPlaceController().goTo( new WelcomePlace() );
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

    @UiHandler( "buttonTasks" )
    public void doTasks( ClickEvent click )
    {
    }

    @UiHandler( "buttonConfiguration" )
    public void doClickConfiguration( ClickEvent click )
    {
        factory.getPlaceController().goTo( new ConfigurationPlace() );
    }

    @UiHandler( "buttonTools" )
    public void doClickTools( ClickEvent click )
    {
    }

}
