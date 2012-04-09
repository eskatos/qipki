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
package org.qipki.ca.http.bootstrap;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import static org.qi4j.library.http.Servlets.*;

import org.qipki.ca.http.presentation.rest.resources.x509.X509PemResource;
import org.qipki.ca.http.presentation.rest.RestletApplication;
import org.qipki.ca.http.presentation.rest.ObjectResourceFinder;
import org.qipki.ca.http.presentation.rest.RestletServletServerService;
import org.qipki.ca.http.presentation.rest.RestletValuesFactory;
import org.qipki.ca.http.presentation.rest.api.RestApiConfiguration;
import org.qipki.ca.http.presentation.rest.api.RestApiService;
import org.qipki.ca.http.presentation.rest.resources.CaApiRootResource;
import org.qipki.ca.http.presentation.rest.resources.ca.CAExportResource;
import org.qipki.ca.http.presentation.rest.resources.ca.CAListResource;
import org.qipki.ca.http.presentation.rest.resources.ca.CAResource;
import org.qipki.ca.http.presentation.rest.resources.ca.CRLResource;
import org.qipki.ca.http.presentation.rest.resources.cryptostore.CryptoStoreListResource;
import org.qipki.ca.http.presentation.rest.resources.cryptostore.CryptoStoreResource;
import org.qipki.ca.http.presentation.rest.resources.escrowedkeypair.EscrowedKeyPairListResource;
import org.qipki.ca.http.presentation.rest.resources.escrowedkeypair.EscrowedKeyPairPemResource;
import org.qipki.ca.http.presentation.rest.resources.escrowedkeypair.EscrowedKeyPairResource;
import org.qipki.ca.http.presentation.rest.resources.tools.CryptoInspectorResource;
import org.qipki.ca.http.presentation.rest.resources.x509.X509DetailResource;
import org.qipki.ca.http.presentation.rest.resources.x509.X509ListResource;
import org.qipki.ca.http.presentation.rest.resources.x509.X509RecoveryResource;
import org.qipki.ca.http.presentation.rest.resources.x509.X509Resource;
import org.qipki.ca.http.presentation.rest.resources.x509.X509RevocationResource;
import org.qipki.ca.http.presentation.rest.resources.x509profile.X509ProfileListResource;
import org.qipki.ca.http.presentation.rest.resources.x509profile.X509ProfileResource;
import org.qipki.commons.bootstrap.RestValuesModuleAssembler;
import org.qipki.core.bootstrap.AssemblerWithConfig;

public class RestApiModuleAssembler
        implements AssemblerWithConfig
{

    @Override
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        module.objects( RestletApplication.class ).
                visibleIn( Visibility.layer );

        module.objects( ObjectResourceFinder.class,
                        CaApiRootResource.class,
                        CryptoInspectorResource.class,
                        CryptoStoreListResource.class,
                        CryptoStoreResource.class,
                        CAListResource.class,
                        CAResource.class,
                        CAExportResource.class,
                        CRLResource.class,
                        X509ProfileListResource.class,
                        X509ProfileResource.class,
                        X509ListResource.class,
                        X509Resource.class,
                        X509PemResource.class,
                        X509DetailResource.class,
                        X509RevocationResource.class,
                        X509RecoveryResource.class,
                        EscrowedKeyPairListResource.class,
                        EscrowedKeyPairResource.class,
                        EscrowedKeyPairPemResource.class ).
                visibleIn( Visibility.module );

        new RestValuesModuleAssembler( Visibility.layer ).assemble( module );

        module.services( RestApiService.class, RestletValuesFactory.class ).
                visibleIn( Visibility.module );

        addServlets( serve( "/api/*" ).
                with( RestletServletServerService.class ) ).
                to( module );

    }

    @Override
    public void assembleConfigModule( ModuleAssembly config )
            throws AssemblyException
    {
        config.entities( RestApiConfiguration.class ).visibleIn( Visibility.application );
    }

}
