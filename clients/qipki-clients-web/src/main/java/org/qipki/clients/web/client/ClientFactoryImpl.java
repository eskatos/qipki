/*w
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

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import org.qipki.clients.web.client.configuration.ConfigurationView;
import org.qipki.clients.web.client.configuration.ConfigurationViewImpl;
import org.qipki.clients.web.client.welcome.WelcomeView;
import org.qipki.clients.web.client.welcome.WelcomeViewImpl;

public class ClientFactoryImpl
        implements ClientFactory
{

    private final EventBus eventBus = new SimpleEventBus();
    private final PlaceController placeController = new PlaceController( eventBus );
    private final WelcomeView welcomeView = new WelcomeViewImpl();
    private final ConfigurationView goodbyeView = new ConfigurationViewImpl();

    public EventBus getEventBus()
    {
        return eventBus;
    }

    public PlaceController getPlaceController()
    {
        return placeController;
    }

    public WelcomeView getWelcomeView()
    {
        return welcomeView;
    }

    public ConfigurationView getConfigurationView()
    {
        return goodbyeView;
    }

}
