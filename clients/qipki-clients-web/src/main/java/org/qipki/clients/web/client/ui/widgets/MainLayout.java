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

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.layout.client.Layout.Alignment;
import com.google.gwt.layout.client.Layout.AnimationCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

public class MainLayout
{

    // Constants
    private final static int HORIZ_SIDEBAR_WIDTH = 192;
    private final static int VERT_SIDEBAR_HEIGHT = 128;
    // Panels
    private final ScrollPanel mainPanel = new ScrollPanel();
    private final SidebarPanel westSidebarPanel = new SidebarPanel( this );
    private final SidebarPanel eastSidebarPanel = new SidebarPanel( this );
    private final SidebarPanel northSidebarPanel = new SidebarPanel( this );
    private final SidebarPanel southSidebarPanel = new SidebarPanel( this );
    private final SimplePanel ribbonPanel = new SimplePanel();
    private final SimplePanel footerPanel = new SimplePanel();
    private final SimplePanel menuPanel = new SimplePanel();
    // Layout
    private final DockLayoutPanel rootLayout = new DockLayoutPanel( Unit.PX );
    private final LayoutPanel splittedLayout = new LayoutPanel();

    @Inject
    public MainLayout( Menu menu )
    {
        splittedLayout.add( mainPanel );
        splittedLayout.add( westSidebarPanel );
        splittedLayout.add( eastSidebarPanel );
        splittedLayout.add( northSidebarPanel );
        splittedLayout.add( southSidebarPanel );

        ribbonPanel.setWidget( new Ribbon() );
        menuPanel.setWidget( menu );
        footerPanel.setWidget( new Footer() );

        firstLayout();
        firstStyle();

        rootLayout.addNorth( ribbonPanel, 24 );
        rootLayout.addNorth( menuPanel, 42 );
        rootLayout.addSouth( footerPanel, 24 );
        rootLayout.add( splittedLayout );
    }

    private void firstLayout()
    {
        mainPanel.setAlwaysShowScrollBars( true );
        splittedLayout.setWidgetHorizontalPosition( mainPanel, Alignment.STRETCH );
        splittedLayout.setWidgetVerticalPosition( mainPanel, Alignment.STRETCH );
        splittedLayout.setWidgetLeftWidth( westSidebarPanel, -HORIZ_SIDEBAR_WIDTH, Unit.PX, HORIZ_SIDEBAR_WIDTH, Unit.PX );
        splittedLayout.setWidgetRightWidth( eastSidebarPanel, -HORIZ_SIDEBAR_WIDTH, Unit.PX, HORIZ_SIDEBAR_WIDTH, Unit.PX );
        splittedLayout.setWidgetTopHeight( northSidebarPanel, -HORIZ_SIDEBAR_WIDTH, Unit.PX, HORIZ_SIDEBAR_WIDTH, Unit.PX );
        splittedLayout.setWidgetBottomHeight( southSidebarPanel, -HORIZ_SIDEBAR_WIDTH, Unit.PX, HORIZ_SIDEBAR_WIDTH, Unit.PX );
    }

    private void firstStyle()
    {
        rootLayout.addStyleName( "qipki-Root" );
        ribbonPanel.addStyleName( "qipki-Ribbon" );
        menuPanel.addStyleName( "qipki-Menu" );
        footerPanel.addStyleName( "qipki-Footer" );

        splittedLayout.addStyleName( "qipki-Splitted" );
        mainPanel.addStyleName( "qipki-Main" );
        westSidebarPanel.addStyleName( "qipki-WestSidebar" );
        eastSidebarPanel.addStyleName( "qipki-EastSidebar" );
        northSidebarPanel.addStyleName( "qipki-NorthSidebar" );
        southSidebarPanel.addStyleName( "qipki-SouthSidebar" );
    }

    /* package */ void setSidebarActivated( SidebarPanel sidebar, boolean activated, boolean animate, AnimationCallback animationCallback )
    {
        if ( sidebar.isActive() != activated ) {
            sidebar.setHorizontalScrollPosition( 0 );
            sidebar.setVerticalScrollPosition( 0 );
            if ( animate ) {
                splittedLayout.forceLayout();
            }
            if ( sidebar == westSidebarPanel ) {
                splittedLayout.setWidgetLeftWidth( westSidebarPanel,
                                                   activated ? 0 : -HORIZ_SIDEBAR_WIDTH, Unit.PX,
                                                   HORIZ_SIDEBAR_WIDTH, Unit.PX );
            } else if ( sidebar == eastSidebarPanel ) {
                splittedLayout.setWidgetRightWidth( eastSidebarPanel,
                                                    activated ? 0 : -HORIZ_SIDEBAR_WIDTH, Unit.PX,
                                                    HORIZ_SIDEBAR_WIDTH, Unit.PX );
            } else if ( sidebar == northSidebarPanel ) {
                splittedLayout.setWidgetTopHeight( northSidebarPanel,
                                                   activated ? 0 : -VERT_SIDEBAR_HEIGHT, Unit.PX,
                                                   VERT_SIDEBAR_HEIGHT, Unit.PX );
            } else if ( sidebar == southSidebarPanel ) {
                splittedLayout.setWidgetBottomHeight( southSidebarPanel,
                                                      activated ? 0 : -VERT_SIDEBAR_HEIGHT, Unit.PX,
                                                      VERT_SIDEBAR_HEIGHT, Unit.PX );
            }
            sidebar.setActive( activated );
            splittedLayout.setWidgetTopBottom( mainPanel,
                                               northSidebarPanel.isActive() ? VERT_SIDEBAR_HEIGHT : 0, Unit.PX,
                                               southSidebarPanel.isActive() ? VERT_SIDEBAR_HEIGHT : 0, Unit.PX );
            splittedLayout.setWidgetLeftRight( mainPanel,
                                               westSidebarPanel.isActive() ? HORIZ_SIDEBAR_WIDTH : 0, Unit.PX,
                                               eastSidebarPanel.isActive() ? HORIZ_SIDEBAR_WIDTH : 0, Unit.PX );

            if ( animate ) {
                splittedLayout.animate( 150, animationCallback );
            } else {
                splittedLayout.forceLayout();
            }
        }
    }

    public DockLayoutPanel getRootLayout()
    {
        return rootLayout;
    }

    public AcceptsOneWidget getRibbonPanel()
    {
        return ribbonPanel;
    }

    public AcceptsOneWidget getMenuPanel()
    {
        return menuPanel;
    }

    public AcceptsOneWidget getMainPanel()
    {
        return mainPanel;
    }

    public AcceptsOneWidget getWestSidebarPanel()
    {
        return westSidebarPanel;
    }

    public AcceptsOneWidget getEastSidebarPanel()
    {
        return eastSidebarPanel;
    }

    public AcceptsOneWidget getNorthSidebarPanel()
    {
        return northSidebarPanel;
    }

    public AcceptsOneWidget getSouthSidebarPanel()
    {
        return southSidebarPanel;
    }

    public AcceptsOneWidget getFooterPanel()
    {
        return footerPanel;
    }

}
