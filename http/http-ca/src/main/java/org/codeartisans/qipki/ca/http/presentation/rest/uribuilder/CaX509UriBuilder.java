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
package org.codeartisans.qipki.ca.http.presentation.rest.uribuilder;

import org.restlet.data.Reference;

public final class CaX509UriBuilder
        extends AbstractUriBuilder
{


    /* package */ CaX509UriBuilder( Reference baseRef, String identity, String special )
    {
        super( baseRef, identity, special );
    }

    public CaX509UriBuilder withIdentity( String identity )
    {
        return new CaX509UriBuilder( baseRef, identity, special );
    }

    public CaX509UriBuilder pem()
    {
        return new CaX509UriBuilder( baseRef, identity, "pem" );
    }

    public CaX509UriBuilder detail()
    {
        return new CaX509UriBuilder( baseRef, identity, "detail" );
    }

    public CaX509UriBuilder revocation()
    {
        return new CaX509UriBuilder( baseRef, identity, "revocation" );
    }

    public CaX509UriBuilder recovery()
    {
        return new CaX509UriBuilder( baseRef, identity, "recovery" );
    }

}
