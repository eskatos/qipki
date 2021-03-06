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
package org.qipki.ca.bootstrap;

public interface CaAssemblyNames
{

    // Presentation Layer
    String LAYER_PRESENTATION = "presentation";
    String MODULE_TESTS_IN_PRESENTATION = "tests-presentation";
    // Configuration Layer
    String LAYER_CONFIGURATION = "config";
    String MODULE_CONFIGURATION = "config";
    // Management Layer
    String LAYER_MANAGEMENT = "management";
    String MODULE_JMX = "jmx";
    // Application Layer
    String LAYER_APPLICATION = "application";
    String MODULE_CA_DCI = "ca-dci";
    // Domain Layer
    String LAYER_DOMAIN = "domain";
    String MODULE_CA_DOMAIN = "ca-domain";
    // Crypto Layer
    String LAYER_CRYPTO = "crypto";
    String MODULE_CRYPTO_ENGINE = "crypto-engine";
    String MODULE_CRYPTO_VALUES = "crypto-values";
    // Infrastructure Layer
    String LAYER_INFRASTRUCTURE = "infrastructure";
    String MODULE_PERSISTENCE = "persistence";
    String MODULE_SCHEDULER = "scheduler";
}
