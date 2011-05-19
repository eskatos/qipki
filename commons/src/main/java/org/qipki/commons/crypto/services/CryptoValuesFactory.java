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
package org.qipki.commons.crypto.services;

import java.util.Date;

import org.qipki.commons.crypto.values.KeyPairSpecValue;
import org.qipki.commons.crypto.values.ValidityIntervalValue;
import org.qipki.crypto.algorithms.AsymetricAlgorithm;

import org.joda.time.Interval;

import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.value.ValueBuilder;
import org.qi4j.api.value.ValueBuilderFactory;

@Mixins( CryptoValuesFactory.Mixin.class )
public interface CryptoValuesFactory
        extends ServiceComposite
{

    ValidityIntervalValue buildValidityInterval( Interval interval );

    ValidityIntervalValue buildValidityInterval( Date notBefore, Date notAfter );

    KeyPairSpecValue createKeySpec( AsymetricAlgorithm algorithm, Integer length );

    @SuppressWarnings( "PublicInnerClass" )
    abstract class Mixin
            implements CryptoValuesFactory
    {

        @Structure
        private ValueBuilderFactory vbf;

        @Override
        public ValidityIntervalValue buildValidityInterval( Interval interval )
        {
            return buildValidityInterval( interval.getStart().toDate(), interval.getEnd().toDate() );
        }

        @Override
        public ValidityIntervalValue buildValidityInterval( Date notBefore, Date notAfter )
        {
            ValueBuilder<ValidityIntervalValue> builder = vbf.newValueBuilder( ValidityIntervalValue.class );
            ValidityIntervalValue interval = builder.prototype();
            interval.notBefore().set( notBefore );
            interval.notAfter().set( notAfter );
            return builder.newInstance();
        }

        @Override
        public KeyPairSpecValue createKeySpec( AsymetricAlgorithm algorithm, Integer length )
        {
            ValueBuilder<KeyPairSpecValue> keySpecBuilder = vbf.newValueBuilder( KeyPairSpecValue.class );
            KeyPairSpecValue keySpec = keySpecBuilder.prototype();
            keySpec.algorithm().set( algorithm );
            keySpec.length().set( length );
            return keySpecBuilder.newInstance();
        }

    }

}
