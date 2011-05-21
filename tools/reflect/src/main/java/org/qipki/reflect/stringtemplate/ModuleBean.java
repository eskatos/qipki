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
package org.qipki.reflect.stringtemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModuleBean
{

    String name;
    List<ObjectBean> objects = new ArrayList<ObjectBean>();
    List<TransientBean> transients = new ArrayList<TransientBean>();
    List<ValueBean> values = new ArrayList<ValueBean>();
    List<EntityBean> entities = new ArrayList<EntityBean>();
    List<ServiceBean> services = new ArrayList<ServiceBean>();

    public String getName()
    {
        return name;
    }

    public Iterable<ObjectBean> getObjects()
    {
        return Collections.unmodifiableCollection( objects );
    }

    public Iterable<TransientBean> getTransients()
    {
        return Collections.unmodifiableCollection( transients );
    }

    public Iterable<ValueBean> getValues()
    {
        return Collections.unmodifiableCollection( values );
    }

    public Iterable<EntityBean> getEntities()
    {
        return Collections.unmodifiableCollection( entities );
    }

    public Iterable<ServiceBean> getServices()
    {
        return Collections.unmodifiableCollection( services );
    }

    public boolean getHasObjects()
    {
        return !objects.isEmpty();
    }

    public boolean getHasTransients()
    {
        return !transients.isEmpty();
    }

    public boolean getHasValues()
    {
        return !values.isEmpty();
    }

    public boolean getHasEntities()
    {
        return !entities.isEmpty();
    }

    public boolean getHasServices()
    {
        return !services.isEmpty();
    }

}
