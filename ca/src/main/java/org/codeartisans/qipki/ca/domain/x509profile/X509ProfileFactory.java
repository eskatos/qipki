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

import org.codeartisans.qipki.commons.states.X509ProfileState;
import org.codeartisans.qipki.commons.values.crypto.x509.BasicConstraintsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.ExtendedKeyUsagesValue;
import org.codeartisans.qipki.commons.values.crypto.x509.KeyUsagesValue;
import org.codeartisans.qipki.commons.values.crypto.x509.NameConstraintsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.NetscapeCertTypesValue;
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

    X509Profile create( X509ProfileState params );

    abstract class Mixin
            implements X509ProfileFactory
    {

        @Structure
        private UnitOfWorkFactory uowf;
        @Structure
        private ValueBuilderFactory vbf;

        @Override
        public X509Profile create( X509ProfileState params )
        {
            EntityBuilder<X509Profile> builder = uowf.currentUnitOfWork().newEntityBuilder( X509Profile.class );
            X509Profile profile = builder.instance();
            profile.name().set( params.name().get() );
            profile.netscapeCertComment().set( params.netscapeCertComment().get() );
            if ( params.keyUsages().get() != null ) {
                profile.keyUsages().set( vbf.newValueBuilder( KeyUsagesValue.class ).
                        withPrototype( params.keyUsages().get() ).newInstance() );
            }
            if ( params.extendedKeyUsages().get() != null ) {
                profile.extendedKeyUsages().set( vbf.newValueBuilder( ExtendedKeyUsagesValue.class ).
                        withPrototype( params.extendedKeyUsages().get() ).newInstance() );
            }
            if ( params.netscapeCertTypes().get() != null ) {
                profile.netscapeCertTypes().set( vbf.newValueBuilder( NetscapeCertTypesValue.class ).
                        withPrototype( params.netscapeCertTypes().get() ).newInstance() );
            }
            if ( params.basicConstraints().get() != null ) {
                profile.basicConstraints().set( vbf.newValueBuilder( BasicConstraintsValue.class ).
                        withPrototype( params.basicConstraints().get() ).newInstance() );
            }
            if ( params.nameConstraints().get() != null ) {
                profile.nameConstraints().set( vbf.newValueBuilder( NameConstraintsValue.class ).
                        withPrototype( params.nameConstraints().get() ).newInstance() );
            }
            return builder.newInstance();
        }

    }

}
