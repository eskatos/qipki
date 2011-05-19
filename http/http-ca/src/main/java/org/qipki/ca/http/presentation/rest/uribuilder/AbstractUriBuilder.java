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
package org.qipki.ca.http.presentation.rest.uribuilder;

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
            if ( !StringUtils.isEmpty( special ) ) {
                return baseRef.clone().addSegment( special ).toString();
            }
            return baseRef.toString();
        }
        if ( StringUtils.isEmpty( special ) ) {
            return baseRef.clone().addSegment( identity ).toString();
        }
        return baseRef.clone().addSegment( identity ).addSegment( special ).toString();
    }

}
