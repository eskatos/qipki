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
package org.codeartisans.qipki.ca.presentation.rest.resources;

import java.util.Arrays;
import java.util.List;

/**
 * TODO Generate automatic message using missings and illegals
 */
public class WrongParametersBuilder
        extends Exception
{

    private static final long serialVersionUID = 1L;
    private String title;
    private List<String> missings;
    private List<String> illegals;

    public WrongParametersBuilder()
    {
    }

    private WrongParametersBuilder( String title, List<String> missings, List<String> illegals )
    {
        this.title = title;
        this.missings = missings;
        this.illegals = illegals;
    }

    public WrongParametersBuilder title( String title )
    {
        return new WrongParametersBuilder( title, missings, illegals );
    }

    // TODO Make WrongParametersBuilder.missings() method additive
    public WrongParametersBuilder missings( String... missings )
    {
        return new WrongParametersBuilder( title, Arrays.asList( missings ), illegals );
    }

    // TODO Make WrongParametersBuilder.illegals() method additive
    public WrongParametersBuilder illegals( String... illegals )
    {
        return new WrongParametersBuilder( title, missings, Arrays.asList( illegals ) );
    }

    public WrongParametersException build()
    {
        return new WrongParametersException( title );
    }

    public WrongParametersException build( Throwable cause )
    {
        return new WrongParametersException( title, cause );
    }

}
