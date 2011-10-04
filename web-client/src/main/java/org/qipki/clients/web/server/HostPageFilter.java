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
package org.qipki.clients.web.server;

import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.codeartisans.java.toolbox.Strings;
import org.codeartisans.spicyplates.SpicyContext;
import org.codeartisans.spicyplates.eruby.ERubySpicyFilter;

public class HostPageFilter
        extends ERubySpicyFilter
{

    @Override
    protected String mapTemplateName( String originalTemplateName )
    {
        if ( Strings.isEmpty( originalTemplateName ) ) {
            return "index.erb";
        }
        if ( originalTemplateName.endsWith( "/" ) ) {
            return originalTemplateName + "index.erb";
        }
        if ( originalTemplateName.endsWith( ".html" ) ) {
            return originalTemplateName.substring( 0, originalTemplateName.length() - 5 ) + ".erb";
        }
        return originalTemplateName;
    }

    @Override
    protected void populateRequestContext( HttpServletRequest req, SpicyContext requestContext )
            throws ServletException
    {
        try {
            URL requestURL = new URL( req.getRequestURL().toString() );
            String x509CaUri = new URL( requestURL.getProtocol(), requestURL.getHost(), requestURL.getPort(), "/api" ).toString();
            requestContext.put( "x509_ca_api_url", x509CaUri );
        } catch ( MalformedURLException ex ) {
            throw new ServletException( "Unable to populate request context", ex );
        }
    }

}
