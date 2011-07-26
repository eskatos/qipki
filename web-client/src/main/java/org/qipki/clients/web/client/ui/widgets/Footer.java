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
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class Footer
        extends Composite
{

    private static final FooterUiBinder binder = GWT.create( FooterUiBinder.class );
    private AboutBox aboutBox;

    @UiTemplate( "Footer.ui.xml" )
    interface FooterUiBinder
            extends UiBinder<Panel, Footer>
    {
    }

    public Footer()
    {
        initWidget( binder.createAndBindUi( this ) );
    }

    @UiHandler( "aboutLabel" )
    public void onAbout( ClickEvent click )
    {
        if ( aboutBox == null ) {
            aboutBox = new AboutBox();
        }
        aboutBox.show();
    }

    private static class AboutBox
            extends DialogBox
    {

        private final ExternalResourcePanel about;
        private boolean loaded;

        public AboutBox()
        {

            about = new ExternalResourcePanel( "about.html" );

            Button close = new Button( "Close" );
            close.addClickHandler( new ClickHandler()
            {

                @Override
                public void onClick( ClickEvent event )
                {
                    AboutBox.this.hide();
                }

            } );

            ScrollPanel scroll = new ScrollPanel();
            scroll.setHeight( "364px" );
            scroll.add( about );

            FlowPanel panel = new FlowPanel();
            panel.setWidth( "480px" );
            panel.setHeight( "384px" );
            panel.add( scroll );
            panel.add( close );

            setText( "About" );
            setAnimationEnabled( true );
            setGlassEnabled( true );
            setWidget( panel );
            center();
        }

        @Override
        public void show()
        {
            if ( !loaded ) {
                about.reload();
                loaded = true;
            }
            super.show();
        }

    }

}
