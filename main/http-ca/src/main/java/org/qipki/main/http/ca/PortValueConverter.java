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
package org.qipki.main.http.ca;

import joptsimple.ValueConversionException;
import joptsimple.ValueConverter;

public class PortValueConverter
        implements ValueConverter<Integer>
{

    @Override
    public Integer convert( String value )
    {
        try {
            Integer port = Integer.valueOf( value );
            if ( port <= 0 ) {
                throw new ValueConversionException( "Value [" + value + "] must be positive" );
            }
            if ( port > 65535 ) {
                throw new ValueConversionException( "Value [" + value + "] must be less than or equals 65535" );
            }
            return port;
        } catch ( NumberFormatException ex ) {
            throw new ValueConversionException( "Value [" + value + "] is not an integer", ex );
        }
    }

    @Override
    public Class<Integer> valueType()
    {
        return Integer.class;
    }

    @Override
    public String valuePattern()
    {
        return "1-65535";
    }

}
