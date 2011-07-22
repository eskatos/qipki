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

import com.google.gwt.layout.client.Layout.AnimationCallback;
import com.google.gwt.layout.client.Layout.Layer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class SidebarPanel
        extends ScrollPanel
{

    private final MainLayout mainLayout;
    private boolean active = false;

    public SidebarPanel( MainLayout mainLayout )
    {
        this.mainLayout = mainLayout;
    }

    @Override
    public void setWidget( IsWidget w )
    {
        final Widget widget = Widget.asWidgetOrNull( w );
        if ( widget != getWidget() ) {
            setHorizontalScrollPosition( 0 );
            setVerticalScrollPosition( 0 );
            boolean oldWidget = getWidget() != null;
            boolean newWidget = widget != null;
            if ( oldWidget && newWidget ) {
                // Replace
                setWidget( widget );
            } else if ( oldWidget ) {
                // Hide
                mainLayout.setSidebarActivated( this, false, new AnimationCallback()
                {

                    public void onAnimationComplete()
                    {
                        setWidget( widget );
                    }

                    public void onLayout( Layer layer, double progress )
                    {
                    }

                } );
            } else if ( newWidget ) {
                // Show
                setWidget( widget );
                mainLayout.setSidebarActivated( this, true, null );
            } else {
                // Should not happen
                Window.alert( "SidebarPanel\n\n!oldWidget && !newWidget\nThis should not happen\n\nExpect bad layout behaviour!" );
            }
        }
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive( boolean active )
    {
        this.active = active;
    }

}
