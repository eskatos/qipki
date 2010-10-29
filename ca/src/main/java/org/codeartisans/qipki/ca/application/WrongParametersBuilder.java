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
package org.codeartisans.qipki.ca.application;

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
