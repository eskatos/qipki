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
package org.codeartisans.qipki.ca.application.contexts.x509;

import java.io.StringReader;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.PKCS10CertificationRequest;

import org.codeartisans.qipki.ca.application.WrongParametersBuilder;
import org.codeartisans.qipki.ca.domain.ca.CA;
import org.codeartisans.qipki.ca.domain.ca.CARepository;
import org.codeartisans.qipki.ca.domain.escrowedkeypair.EscrowedKeyPair;
import org.codeartisans.qipki.ca.domain.escrowedkeypair.EscrowedKeyPairRepository;
import org.codeartisans.qipki.ca.domain.x509.X509;
import org.codeartisans.qipki.ca.domain.x509.X509Factory;
import org.codeartisans.qipki.ca.domain.x509.X509Repository;
import org.codeartisans.qipki.ca.domain.x509profile.X509Profile;
import org.codeartisans.qipki.ca.domain.x509profile.X509ProfileRepository;
import org.codeartisans.qipki.crypto.io.CryptIO;
import org.codeartisans.qipki.core.dci.Context;
import org.codeartisans.qipki.crypto.x509.DistinguishedName;
import org.codeartisans.qipki.crypto.x509.X509Generator;

import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.query.Query;
import org.qi4j.api.unitofwork.NoSuchEntityException;

public class X509ListContext
        extends Context
{

    @Service
    private CryptIO cryptIO;
    @Service
    private X509Generator x509Generator;

    public Query<X509> list( int start )
    {
        return context.role( X509Repository.class ).findAllPaginated( start, 25 );
    }

    public X509 createX509( String caIdentity, String x509ProfileIdentity, String pkcs10PEM )
    {
        return createX509( caIdentity, x509ProfileIdentity, cryptIO.readPKCS10PEM( new StringReader( pkcs10PEM ) ) );
    }

    public X509 createX509( String caIdentity, String x509ProfileIdentity, PKCS10CertificationRequest pkcs10 )
    {
        try {

            CA ca = context.role( CARepository.class ).findByIdentity( caIdentity );
            X509Profile x509Profile = context.role( X509ProfileRepository.class ).findByIdentity( x509ProfileIdentity );

            X509Certificate cert = ca.sign( x509Profile, pkcs10 );
            X509 x509 = context.role( X509Factory.class ).create( cert, ca, x509Profile );

            return x509;

        } catch ( NoSuchEntityException ex ) {
            throw new WrongParametersBuilder().title( "Invalid CA or X509Profile identity" ).build( ex );
        }
    }

    public X509 createX509( String caIdentity, String x509ProfileIdentity, String escrowedKeyPairIdentity, String distinguishedName )
    {
        try {

            EscrowedKeyPair ekp = context.role( EscrowedKeyPairRepository.class ).findByIdentity( escrowedKeyPairIdentity );

            PKCS10CertificationRequest pkcs10 = x509Generator.generatePKCS10( new DistinguishedName( distinguishedName ), ekp.keyPair() );
            X509 x509 = createX509( caIdentity, x509ProfileIdentity, pkcs10 );
            ekp.x509s().add( x509 );

            return x509;

        } catch ( NoSuchEntityException ex ) {
            throw new WrongParametersBuilder().title( "Invalid CA or X509Profile or EscrowedKeyPair identity" ).build( ex );
        }
    }

    public X509 findByHexSha256( String hexSha256 )
    {
        return context.role( X509Repository.class ).findByHexSha256( hexSha256 );
    }

}
