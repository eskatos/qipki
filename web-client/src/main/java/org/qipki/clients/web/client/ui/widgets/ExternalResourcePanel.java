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

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

public class ExternalResourcePanel
        extends Composite
{

    private SimplePanel panel = new SimplePanel();
    private String url;
    private IsWidget loadingPanel;

    public ExternalResourcePanel( String url )
    {
        this( url, null );
    }

    public ExternalResourcePanel( String url, IsWidget loadingPanel )
    {
        this.url = url;
        this.loadingPanel = loadingPanel;
        initWidget( panel );
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public void setLoadingPanel( IsWidget loadingPanel )
    {
        this.loadingPanel = loadingPanel;
    }

    public void load( String url )
    {
        load( url, null );
    }

    public void load( final String url, final RequestCallback callback )
    {
        RequestBuilder rb = new RequestBuilder( RequestBuilder.GET, url );
        rb.setCallback( new RequestCallback()
        {

            @Override
            public void onResponseReceived( final Request request, final Response response )
            {
                new Timer()
                {

                    @Override
                    public void run()
                    {
                        panel.clear();
                        panel.add( new HTMLPanel( response.getText() ) );
                        ExternalResourcePanel.this.url = url;
                        callback.onResponseReceived( request, response );
                    }

                }.schedule( 2000 );
            }

            @Override
            public void onError( Request request, Throwable exception )
            {
                panel.clear();
                panel.add( new Label( "Request ERROR: " + exception.getMessage() ) );
                callback.onError( request, exception );
            }

        } );
        panel.clear();
        if ( loadingPanel != null ) {
            panel.setWidget( loadingPanel );
        }
        try {
            rb.send();
        } catch ( RequestException ex ) {
            panel.add( new Label( "Request EXCEPTION: " + ex.getMessage() ) );
        }
    }

    public void reload()
    {
        load( url, null );
    }

    public void reload( RequestCallback callback )
    {
        load( url, callback );
    }

}
