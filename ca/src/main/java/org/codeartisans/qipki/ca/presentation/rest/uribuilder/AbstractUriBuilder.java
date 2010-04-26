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
package org.codeartisans.qipki.ca.presentation.rest.uribuilder;

import org.codeartisans.java.toolbox.StringUtils;
import org.restlet.data.Reference;

public class AbstractUriBuilder
{

    protected final Reference baseRef;
    protected final String identity;
    protected final String special;

    protected AbstractUriBuilder( Reference baseRef, String identity, String special )
    {
        this.baseRef = baseRef;
        this.identity = identity;
        this.special = special;
    }

    public final String build()
    {
        if ( "factory".equals( special ) ) {
            return baseRef.clone().addSegment( "factory" ).toString();
        }
        if ( StringUtils.isEmpty( identity ) ) {
            return baseRef.toString();
        }
        if ( StringUtils.isEmpty( special ) ) {
            return baseRef.clone().addSegment( identity ).toString();
        }
        return baseRef.clone().addSegment( identity ).addSegment( special ).toString();
    }

}
