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

import org.qipki.clients.web.client.config.ConfigWestSidebarView;
import org.qipki.clients.web.client.config.ConfigGeneralView;
import org.qipki.clients.web.client.config.ConfigMessagingView;
import org.qipki.clients.web.client.config.ConfigSchedulerView;
import org.qipki.clients.web.client.tools.ToolsWestSidebarView;

public interface ClientFactory
{

    ConfigWestSidebarView getConfigWestSidebarView();

    ConfigGeneralView getConfigGeneralView();

    ConfigMessagingView getConfigMessagingView();

    ConfigSchedulerView getConfigSchedulerView();

    ToolsWestSidebarView getToolsWestSidebarView();

}