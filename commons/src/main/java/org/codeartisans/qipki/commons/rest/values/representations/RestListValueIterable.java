/*
 * Copyright (c) 2010, Paul Merlin. All Rights Reserved.
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
package org.codeartisans.qipki.commons.rest.values.representations;

import java.util.Iterator;
import java.util.List;

public class RestListValueIterable<T extends RestValue>
        implements Iterable<T>
{

    private final List<RestValue> items;

    public RestListValueIterable( RestListValue restList )
    {
        this.items = restList.items().get();
    }

    @Override
    public Iterator<T> iterator()
    {
        return new RestListValueIterator();
    }

    @SuppressWarnings( "PublicInnerClass" )
    public class RestListValueIterator
            implements Iterator<T>
    {

        private final Iterator<RestValue> delegate;

        public RestListValueIterator()
        {
            delegate = items.iterator();
        }

        @Override
        public boolean hasNext()
        {
            return delegate.hasNext();
        }

        @Override
        @SuppressWarnings( "unchecked" )
        public T next()
        {
            return ( T ) delegate.next();
        }

        @Override
        public void remove()
        {
            delegate.remove();
        }

    }

}
