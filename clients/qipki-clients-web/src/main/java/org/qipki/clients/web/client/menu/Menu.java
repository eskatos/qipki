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
package org.qipki.clients.web.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;

public class Menu
        extends Composite
{

    @UiTemplate( "Menu.ui.xml" )
    interface MenuUiBinder
            extends UiBinder<Panel, Menu>
    {
    }

    private static final MenuUiBinder binder = GWT.create( MenuUiBinder.class );

    public Menu()
    {
        initWidget( binder.createAndBindUi( this ) );
    }

    @UiHandler( "buttonWelcome" )
    public void doClickWelcome( ClickEvent click )
    {
        Window.alert( "Welcome" );
    }

    @UiHandler( "buttonConfiguration" )
    public void doClickConfiguration( ClickEvent click )
    {
        Window.alert( "Configuration" );
    }

    @UiHandler( "buttonTools" )
    public void doClickTools( ClickEvent click )
    {
        Window.alert( "Tools" );
    }

}
