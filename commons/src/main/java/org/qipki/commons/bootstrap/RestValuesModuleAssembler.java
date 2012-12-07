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
package org.qipki.commons.bootstrap;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qipki.commons.rest.values.CaApiURIsValue;
import org.qipki.commons.rest.values.params.CAFactoryParamsValue;
import org.qipki.commons.rest.values.params.CryptoStoreFactoryParamsValue;
import org.qipki.commons.rest.values.params.EscrowedKeyPairFactoryParamsValue;
import org.qipki.commons.rest.values.params.ParamsFactory;
import org.qipki.commons.rest.values.params.X509FactoryParamsValue;
import org.qipki.commons.rest.values.params.X509ProfileFactoryParamsValue;
import org.qipki.commons.rest.values.params.X509RevocationParamsValue;
import org.qipki.commons.rest.values.representations.CAValue;
import org.qipki.commons.rest.values.representations.CryptoStoreValue;
import org.qipki.commons.rest.values.representations.EscrowedKeyPairValue;
import org.qipki.commons.rest.values.representations.RestListValue;
import org.qipki.commons.rest.values.representations.RestValue;
import org.qipki.commons.rest.values.representations.RevocationValue;
import org.qipki.commons.rest.values.representations.X509DetailValue;
import org.qipki.commons.rest.values.representations.X509ProfileAssignmentValue;
import org.qipki.commons.rest.values.representations.X509ProfileValue;
import org.qipki.commons.rest.values.representations.X509Value;

public class RestValuesModuleAssembler
    implements Assembler
{

    private final Visibility visibility;

    public RestValuesModuleAssembler()
    {
        this( Visibility.module );
    }

    public RestValuesModuleAssembler( Visibility visibility )
    {
        this.visibility = visibility;
    }

    @Override
    public void assemble( ModuleAssembly module )
        throws AssemblyException
    {
        // Params
        module.values( CryptoStoreFactoryParamsValue.class,
                       CAFactoryParamsValue.class,
                       X509ProfileFactoryParamsValue.class,
                       X509FactoryParamsValue.class,
                       X509RevocationParamsValue.class,
                       EscrowedKeyPairFactoryParamsValue.class ).
            visibleIn( visibility );
        module.services( ParamsFactory.class ).
            visibleIn( visibility );

        // Rest values
        module.values( RestValue.class,
                       RestListValue.class,
                       CaApiURIsValue.class,
                       CryptoStoreValue.class,
                       CAValue.class,
                       X509ProfileAssignmentValue.class,
                       X509ProfileValue.class,
                       X509Value.class,
                       X509DetailValue.class,
                       RevocationValue.class,
                       EscrowedKeyPairValue.class ).
            visibleIn( visibility );

    }

}
