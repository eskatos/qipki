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
package org.qipki.ca.domain.x509profile;

import java.util.EnumSet;

import org.qi4j.api.common.Optional;
import org.qi4j.api.entity.EntityBuilder;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;
import org.qi4j.api.value.ValueBuilderFactory;

import org.qipki.commons.crypto.values.x509.BasicConstraintsValue;
import org.qipki.commons.crypto.values.x509.ExtendedKeyUsagesValue;
import org.qipki.commons.crypto.values.x509.KeyUsagesValue;
import org.qipki.commons.crypto.values.x509.NameConstraintsValue;
import org.qipki.commons.crypto.values.x509.NetscapeCertTypesValue;
import org.qipki.commons.crypto.services.X509ExtensionsValueFactory;
import org.qipki.crypto.x509.ExtendedKeyUsage;
import org.qipki.crypto.x509.KeyUsage;
import org.qipki.crypto.x509.NetscapeCertType;

@Mixins( X509ProfileFactory.Mixin.class )
public interface X509ProfileFactory
        extends ServiceComposite
{

    X509Profile create( String name, Integer validityDays, @Optional String comment,
                        @Optional KeyUsagesValue keyUsages,
                        @Optional ExtendedKeyUsagesValue extendedKeyUsages,
                        @Optional NetscapeCertTypesValue netscapeCertTypes,
                        @Optional BasicConstraintsValue basicConstraints,
                        @Optional NameConstraintsValue nameConstraints );

    X509Profile createForSSLServer( String name, Integer validityDays, @Optional String comment );

    X509Profile createForSSLClient( String name, Integer validityDays, @Optional String comment );

    X509Profile createForSignature( String name, Integer validityDays, @Optional String comment );

    X509Profile createForEncipherment( String name, Integer validityDays, @Optional String comment );

    @SuppressWarnings( "PublicInnerClass" )
    abstract class Mixin
            implements X509ProfileFactory
    {

        @Structure
        private UnitOfWorkFactory uowf;
        @Structure
        private ValueBuilderFactory vbf;
        @Service
        private X509ExtensionsValueFactory x509ExtFactory;

        @Override
        public X509Profile create( String name,
                                   Integer validityDays,
                                   String comment,
                                   KeyUsagesValue keyUsages,
                                   ExtendedKeyUsagesValue extendedKeyUsages,
                                   NetscapeCertTypesValue netscapeCertTypes,
                                   BasicConstraintsValue basicConstraints,
                                   NameConstraintsValue nameConstraints )
        {
            EntityBuilder<X509Profile> builder = uowf.currentUnitOfWork().newEntityBuilder( X509Profile.class );
            X509Profile profile = builder.instance();
            profile.name().set( name );
            profile.validityDays().set( validityDays );
            profile.netscapeCertComment().set( comment );
            if ( keyUsages != null ) {
                profile.keyUsages().set( vbf.newValueBuilder( KeyUsagesValue.class ).
                        withPrototype( keyUsages ).newInstance() );
            }
            if ( extendedKeyUsages != null ) {
                profile.extendedKeyUsages().set( vbf.newValueBuilder( ExtendedKeyUsagesValue.class ).
                        withPrototype( extendedKeyUsages ).newInstance() );
            }
            if ( netscapeCertTypes != null ) {
                profile.netscapeCertTypes().set( vbf.newValueBuilder( NetscapeCertTypesValue.class ).
                        withPrototype( netscapeCertTypes ).newInstance() );
            }
            if ( basicConstraints != null ) {
                profile.basicConstraints().set( vbf.newValueBuilder( BasicConstraintsValue.class ).
                        withPrototype( basicConstraints ).newInstance() );
            }
            if ( nameConstraints != null ) {
                profile.nameConstraints().set( vbf.newValueBuilder( NameConstraintsValue.class ).
                        withPrototype( nameConstraints ).newInstance() );
            }
            return builder.newInstance();
        }

        @Override
        public X509Profile createForSSLServer( String name, Integer validityDays, String comment )
        {
            return create( name, validityDays, comment,
                           x509ExtFactory.buildKeyUsagesValue( true, EnumSet.of( KeyUsage.keyEncipherment, KeyUsage.digitalSignature ) ),
                           x509ExtFactory.buildExtendedKeyUsagesValue( false, EnumSet.of( ExtendedKeyUsage.serverAuth ) ),
                           x509ExtFactory.buildNetscapeCertTypesValue( false, EnumSet.of( NetscapeCertType.sslServer ) ),
                           x509ExtFactory.buildBasicConstraintsValue( true, false, 0L ),
                           null );
        }

        @Override
        public X509Profile createForSSLClient( String name, Integer validityDays, String comment )
        {
            return create( name, validityDays, comment,
                           x509ExtFactory.buildKeyUsagesValue( true, EnumSet.of( KeyUsage.keyEncipherment, KeyUsage.digitalSignature ) ),
                           x509ExtFactory.buildExtendedKeyUsagesValue( false, EnumSet.of( ExtendedKeyUsage.clientAuth ) ),
                           x509ExtFactory.buildNetscapeCertTypesValue( false, EnumSet.of( NetscapeCertType.sslClient ) ),
                           x509ExtFactory.buildBasicConstraintsValue( true, false, 0L ),
                           null );
        }

        @Override
        public X509Profile createForEncipherment( String name, Integer validityDays, String comment )
        {
            return create( name, validityDays, comment,
                           x509ExtFactory.buildKeyUsagesValue( true, EnumSet.of( KeyUsage.keyEncipherment ) ),
                           x509ExtFactory.buildExtendedKeyUsagesValue( false, EnumSet.of( ExtendedKeyUsage.emailProtection ) ),
                           x509ExtFactory.buildNetscapeCertTypesValue( false, EnumSet.of( NetscapeCertType.smime ) ),
                           x509ExtFactory.buildBasicConstraintsValue( true, false, 0L ),
                           null );
        }

        @Override
        public X509Profile createForSignature( String name, Integer validityDays, String comment )
        {
            return create( name, validityDays, comment,
                           x509ExtFactory.buildKeyUsagesValue( true, EnumSet.of( KeyUsage.nonRepudiation, KeyUsage.digitalSignature ) ),
                           x509ExtFactory.buildExtendedKeyUsagesValue( false, EnumSet.of( ExtendedKeyUsage.emailProtection ) ),
                           x509ExtFactory.buildNetscapeCertTypesValue( false, EnumSet.of( NetscapeCertType.objectSigning, NetscapeCertType.smime ) ),
                           x509ExtFactory.buildBasicConstraintsValue( true, false, 0L ),
                           null );
        }

    }

}
