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
package org.qipki.commons.rest.values;

import org.qi4j.api.property.Property;
import org.qipki.commons.rest.values.representations.RestValue;

public interface CaApiURIsValue
    extends RestValue
{

    Property<String> cryptoInspectorUri();

    Property<String> cryptoStoreListUri();

    Property<String> caListUri();

    Property<String> x509ProfileListUri();

    Property<String> x509ListUri();

    Property<String> escrowedKeyPairListUri();

}
