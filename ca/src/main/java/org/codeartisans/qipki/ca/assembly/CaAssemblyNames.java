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
package org.codeartisans.qipki.ca.assembly;

public interface CaAssemblyNames
{

    String APPLICATION_NAME = "QiPkiCa";
    String APPLICATION_VERSION = "1.0-m1";
    String MODULE_CONFIGURATION = "config";
    String LAYER_APPLICATION = "application";
    String MODULE_CA_DCI = "ca-dci";
    String LAYER_DOMAIN = "domain";
    String MODULE_CA_DOMAIN = "ca-domain";
    String LAYER_CRYPTO = "crypto";
    String MODULE_CRYPTO_ENGINE = "crypto-engine";
    String MODULE_CRYPTO_VALUES = "crypto-values";
    String LAYER_INFRASTRUCTURE = "infrastructure";
    String MODULE_PERSISTENCE = "persistence";
}
