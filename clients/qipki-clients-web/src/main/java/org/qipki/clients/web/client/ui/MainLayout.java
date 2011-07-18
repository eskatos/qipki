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

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.layout.client.Layout.Alignment;
import com.google.gwt.layout.client.Layout.AnimationCallback;
import com.google.gwt.layout.client.Layout.Layer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

// TODO Extract SidebarPanel class (MainLayout will hold sidebar/activated Map)
public class MainLayout
{

    // Constants
    private final static int WE_SB_WIDTH = 192;
    private final static int NS_SB_HEIGHT = 128;

    private class SidebarPanel
            extends ScrollPanel
    {

        private boolean active = false;

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
                    setSidebarActivated( this, false, new AnimationCallback()
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
                    setSidebarActivated( this, true, null );
                } else {
                    // Should not happen
                    Window.alert( "SidebarPanel\n\n!oldWidget && !newWidget\nThis should not happen\n\nExpect bad layout behaviour!" );
                }
            }
        }

    }

    // Panels
    private final ScrollPanel mainPanel = new ScrollPanel();
    private final SidebarPanel westSidebarPanel = new SidebarPanel();
    private final SidebarPanel eastSidebarPanel = new SidebarPanel();
    private final SidebarPanel northSidebarPanel = new SidebarPanel();
    private final SidebarPanel southSidebarPanel = new SidebarPanel();
    private final SimplePanel ribbonPanel = new SimplePanel( new HTML( "Ribbon" ) );
    private final SimplePanel footerPanel = new SimplePanel( new HTML( "Footer" ) );
    private final SimplePanel menuPanel = new SimplePanel( new HTML( "Menu" ) );
    // Layout
    private final DockLayoutPanel rootLayout = new DockLayoutPanel( Unit.PX );
    private final LayoutPanel splittedLayout = new LayoutPanel();

    public MainLayout()
    {
        splittedLayout.add( mainPanel );
        splittedLayout.add( westSidebarPanel );
        splittedLayout.add( eastSidebarPanel );
        splittedLayout.add( northSidebarPanel );
        splittedLayout.add( southSidebarPanel );
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
        splittedLayout.setWidgetLeftWidth( westSidebarPanel, -WE_SB_WIDTH, Unit.PX, WE_SB_WIDTH, Unit.PX );
        splittedLayout.setWidgetRightWidth( eastSidebarPanel, -WE_SB_WIDTH, Unit.PX, WE_SB_WIDTH, Unit.PX );
        splittedLayout.setWidgetTopHeight( northSidebarPanel, -WE_SB_WIDTH, Unit.PX, WE_SB_WIDTH, Unit.PX );
        splittedLayout.setWidgetBottomHeight( southSidebarPanel, -WE_SB_WIDTH, Unit.PX, WE_SB_WIDTH, Unit.PX );
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

    private void setSidebarActivated( SidebarPanel sidebar, boolean activated, AnimationCallback animationCallback )
    {
        if ( sidebar.active != activated ) {
            sidebar.setHorizontalScrollPosition( 0 );
            sidebar.setVerticalScrollPosition( 0 );
            splittedLayout.forceLayout();
            if ( sidebar == westSidebarPanel ) {
                splittedLayout.setWidgetLeftWidth( westSidebarPanel,
                                                   activated ? 0 : -WE_SB_WIDTH, Unit.PX,
                                                   WE_SB_WIDTH, Unit.PX );
            } else if ( sidebar == eastSidebarPanel ) {
                splittedLayout.setWidgetRightWidth( eastSidebarPanel,
                                                    activated ? 0 : -WE_SB_WIDTH, Unit.PX,
                                                    WE_SB_WIDTH, Unit.PX );
            } else if ( sidebar == northSidebarPanel ) {
                splittedLayout.setWidgetTopHeight( northSidebarPanel,
                                                   activated ? 0 : -NS_SB_HEIGHT, Unit.PX,
                                                   NS_SB_HEIGHT, Unit.PX );
            } else if ( sidebar == southSidebarPanel ) {
                splittedLayout.setWidgetBottomHeight( southSidebarPanel,
                                                      activated ? 0 : -NS_SB_HEIGHT, Unit.PX,
                                                      NS_SB_HEIGHT, Unit.PX );
            }
            sidebar.active = activated;
            splittedLayout.setWidgetTopBottom( mainPanel,
                                               northSidebarPanel.active ? NS_SB_HEIGHT : 0, Unit.PX,
                                               southSidebarPanel.active ? NS_SB_HEIGHT : 0, Unit.PX );
            splittedLayout.setWidgetLeftRight( mainPanel,
                                               westSidebarPanel.active ? WE_SB_WIDTH : 0, Unit.PX,
                                               eastSidebarPanel.active ? WE_SB_WIDTH : 0, Unit.PX );

            splittedLayout.animate( 150, animationCallback );
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
