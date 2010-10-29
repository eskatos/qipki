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

import org.codeartisans.qipki.ca.application.contexts.RootContext;
import org.codeartisans.qipki.ca.application.contexts.ca.CAContext;
import org.codeartisans.qipki.ca.application.contexts.ca.CAListContext;
import org.codeartisans.qipki.ca.application.contexts.cryptostore.CryptoStoreContext;
import org.codeartisans.qipki.ca.application.contexts.cryptostore.CryptoStoreListContext;
import org.codeartisans.qipki.ca.application.contexts.escrowedkeypair.EscrowedKeyPairContext;
import org.codeartisans.qipki.ca.application.contexts.escrowedkeypair.EscrowedKeyPairListContext;
import org.codeartisans.qipki.ca.application.contexts.x509.X509Context;
import org.codeartisans.qipki.ca.application.contexts.x509.X509ListContext;
import org.codeartisans.qipki.ca.application.contexts.x509profile.X509ProfileContext;
import org.codeartisans.qipki.ca.application.contexts.x509profile.X509ProfileListContext;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;

public class CaDCIModuleAssembler
        implements Assembler
{

    @Override
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        module.addObjects( RootContext.class,
                           CryptoStoreListContext.class,
                           CryptoStoreContext.class,
                           CAListContext.class,
                           CAContext.class,
                           X509ProfileListContext.class,
                           X509ProfileContext.class,
                           X509ListContext.class,
                           X509Context.class,
                           EscrowedKeyPairContext.class,
                           EscrowedKeyPairListContext.class ).
                visibleIn( Visibility.application );

    }

}
