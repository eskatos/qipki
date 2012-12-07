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
package org.qipki.ca.application.contexts.x509profile;

import org.qi4j.api.common.Optional;
import org.qi4j.api.query.Query;
import org.qipki.ca.domain.x509profile.X509Profile;
import org.qipki.ca.domain.x509profile.X509ProfileFactory;
import org.qipki.ca.domain.x509profile.X509ProfileRepository;
import org.qipki.commons.crypto.services.X509ExtensionsValueFactory;
import org.qipki.commons.crypto.values.x509.BasicConstraintsValue;
import org.qipki.commons.crypto.values.x509.ExtendedKeyUsagesValue;
import org.qipki.commons.crypto.values.x509.KeyUsagesValue;
import org.qipki.commons.crypto.values.x509.NameConstraintsValue;
import org.qipki.commons.crypto.values.x509.NetscapeCertTypesValue;
import org.qipki.core.dci.Context;

public class X509ProfileListContext
    extends Context
{

    public Query<X509Profile> list( int start )
    {
        return context.role( X509ProfileRepository.class ).findAllPaginated( start, 25 );
    }

    public Query<X509Profile> findByName( String name, int start )
    {
        return context.role( X509ProfileRepository.class ).findByNamePaginated( name, start, 25 );
    }

    public X509ExtensionsValueFactory x509ExtensionsValueFactory()
    {
        return context.role( X509ExtensionsValueFactory.class );
    }

    public X509Profile createX509Profile( String name, Integer validityDays, @Optional String comment,
                                          @Optional KeyUsagesValue keyUsages,
                                          @Optional ExtendedKeyUsagesValue extendedKeyUsages,
                                          @Optional NetscapeCertTypesValue netscapeCertTypes,
                                          @Optional BasicConstraintsValue basicConstraints,
                                          @Optional NameConstraintsValue nameConstraints )
    {
        return context.role( X509ProfileFactory.class ).create( name, validityDays, comment,
                                                                keyUsages, extendedKeyUsages, netscapeCertTypes,
                                                                basicConstraints, nameConstraints );
    }

    public X509Profile createX509ProfileForSSLServer( String name, Integer validityDays, @Optional String comment )
    {
        return context.role( X509ProfileFactory.class ).createForSSLServer( name, validityDays, comment );
    }

    public X509Profile createX509ProfileForSSLClient( String name, Integer validityDays, @Optional String comment )
    {
        return context.role( X509ProfileFactory.class ).createForSSLClient( name, validityDays, comment );
    }

    public X509Profile createX509ProfileForEncipherment( String name, Integer validityDays, @Optional String comment )
    {
        return context.role( X509ProfileFactory.class ).createForEncipherment( name, validityDays, comment );
    }

    public X509Profile createX509ProfileForSignature( String name, Integer validityDays, @Optional String comment )
    {
        return context.role( X509ProfileFactory.class ).createForSignature( name, validityDays, comment );
    }

}
