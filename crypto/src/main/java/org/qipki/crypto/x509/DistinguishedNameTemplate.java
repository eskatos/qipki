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
package org.qipki.crypto.x509;

import java.util.HashMap;
import java.util.Map;
import org.codeartisans.java.toolbox.Strings;

public final class DistinguishedNameTemplate
{

    private final String template;

    public DistinguishedNameTemplate( final String template )
    {
        this.template = template;
    }

    public String getTemplate()
    {
        return template;
    }

    public DistinguishedName buildDN( final Map<String, String> data )
    {
        Map<String, String> escapedData = new HashMap<String, String>();
        for( Map.Entry<String, String> eachEntry : data.entrySet() )
        {
            escapedData.put( eachEntry.getKey(), DistinguishedName.escapeRDNData( eachEntry.getValue() ) );
        }
        StringBuffer rendered = Strings.renderTemplate( new StringBuffer( template ), escapedData, true );
        DistinguishedName dn = new DistinguishedName( rendered.toString() );
        dn.setRemoveEmptyRDNs( true );
        return dn;
    }

}
