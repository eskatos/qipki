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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;

public class Ribbon
        extends Composite
{

    private static final RibbonUiBinder binder = GWT.create( RibbonUiBinder.class );

    @UiTemplate( "Ribbon.ui.xml" )
    interface RibbonUiBinder
            extends UiBinder<Panel, Ribbon>
    {
    }

    public Ribbon()
    {
        initWidget( binder.createAndBindUi( this ) );
    }

}
