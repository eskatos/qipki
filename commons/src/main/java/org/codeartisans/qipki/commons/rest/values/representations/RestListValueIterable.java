/*
 * Copyright (c) 2010 Paul Merlin <paul@nosphere.org>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
