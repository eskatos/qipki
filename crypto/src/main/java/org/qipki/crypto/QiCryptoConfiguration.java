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
package org.qipki.crypto;

import org.qi4j.api.common.Optional;
import org.qi4j.api.configuration.ConfigurationComposite;
import org.qi4j.api.property.Property;

/**
 * If one of providerName or providerClass is empty, BouncyCastle is used by default.
 * By default the provider is inserted on activate and removed on passivate.
 */
public interface QiCryptoConfiguration
    extends ConfigurationComposite
{

    /**
     * Defaulted to Boolean.TRUE
     */
    @Optional
    Property<Boolean> ensureJCE();

    /**
     * Defaulted to BouncyCastle
     */
    @Optional
    Property<String> providerName();

    /**
     * Defaulted to BouncyCastle
     */
    @Optional
    Property<String> providerClass();

    /**
     * Defaulted to Boolean.TRUE
     */
    @Optional
    Property<Boolean> insertProviderOnActivate();

    /**
     * Defaulted to Boolean.TRUE
     */
    @Optional
    Property<Boolean> removeProviderOnPassivate();

    /**
     * @return  Defaults to SHA1PRNG
     */
    @Optional
    Property<String> randomAlgorithm();

    /**
     * @return  Defaults to 128 bytes
     */
    @Optional
    Property<Integer> randomSeedSize();

}
