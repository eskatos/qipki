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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import org.qipki.clients.web.client.assembly.Injector;

/**
 * @see http://code.google.com/intl/fr/webtoolkit/doc/latest/DevGuideMvpActivitiesAndPlaces.html
 * @see http://tbroyer.posterous.com/gwt-21-activities-nesting-yagni
 */
public class qipkiweb
        implements EntryPoint
{

    private final Injector injector = GWT.create( Injector.class );

    @Override
    public void onModuleLoad()
    {
        try {

            RootLayoutPanel.get().add( injector.getMainLayout().getRootLayout() );
            injector.getPlaceHistoryHandler().handleCurrentHistory();

        } finally {
            
            hideLoading();
            
        }
    }

    private native void hideLoading()/*-{
    
    $wnd.loading_hide();
    
    }-*/;

}
