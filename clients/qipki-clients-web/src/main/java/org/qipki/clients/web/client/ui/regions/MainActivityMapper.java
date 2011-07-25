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
package org.qipki.clients.web.client.ui.regions;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;

import org.qipki.clients.web.client.config.ConfigMainActivity;
import org.qipki.clients.web.client.config.ConfigPlace;
import org.qipki.clients.web.client.welcome.WelcomeMainActivity;
import org.qipki.clients.web.client.welcome.WelcomePlace;

public class MainActivityMapper
        implements ActivityMapper
{

    private final Provider<WelcomeMainActivity> welcomeMainActivityProvider;
    private final Provider<ConfigMainActivity> configMainActivityProvider;

    @Inject
    public MainActivityMapper( Provider<WelcomeMainActivity> welcomeMainActivityProvider,
                               Provider<ConfigMainActivity> configMainActivityProvider )
    {
        super();
        this.welcomeMainActivityProvider = welcomeMainActivityProvider;
        this.configMainActivityProvider = configMainActivityProvider;
    }

    @Override
    public Activity getActivity( Place place )
    {
        if ( place instanceof WelcomePlace ) {
            return welcomeMainActivityProvider.get().withPlace( ( WelcomePlace ) place );
        } else if ( place instanceof ConfigPlace ) {
            return configMainActivityProvider.get().withPlace( ( ConfigPlace ) place );
        }
        return null;
    }

}
