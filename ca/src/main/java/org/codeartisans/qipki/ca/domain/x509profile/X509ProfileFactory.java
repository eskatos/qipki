/*
 * Copyright (c) 2010 Paul Merlin <paul@nosphere.org>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
