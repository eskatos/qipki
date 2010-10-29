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
package org.codeartisans.qipki.ca.domain.x509profile;

import org.codeartisans.qipki.commons.crypto.values.x509.BasicConstraintsValue;
import org.codeartisans.qipki.commons.crypto.values.x509.ExtendedKeyUsagesValue;
import org.codeartisans.qipki.commons.crypto.values.x509.KeyUsagesValue;
import org.codeartisans.qipki.commons.crypto.values.x509.NameConstraintsValue;
import org.codeartisans.qipki.commons.crypto.values.x509.NetscapeCertTypesValue;

import org.qi4j.api.common.Optional;
import org.qi4j.api.entity.EntityBuilder;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;
import org.qi4j.api.value.ValueBuilderFactory;

@Mixins( X509ProfileFactory.Mixin.class )
public interface X509ProfileFactory
        extends ServiceComposite
{

    X509Profile create( String name,
                        Integer validityDays,
                        @Optional String comment,
                        @Optional KeyUsagesValue keyUsages,
                        @Optional ExtendedKeyUsagesValue extendedKeyUsages,
                        @Optional NetscapeCertTypesValue netscapeCertTypes,
                        @Optional BasicConstraintsValue basicConstraints,
                        @Optional NameConstraintsValue nameConstraints );

    @SuppressWarnings( "PublicInnerClass" )
    abstract class Mixin
            implements X509ProfileFactory
    {

        @Structure
        private UnitOfWorkFactory uowf;
        @Structure
        private ValueBuilderFactory vbf;

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

    }

}
