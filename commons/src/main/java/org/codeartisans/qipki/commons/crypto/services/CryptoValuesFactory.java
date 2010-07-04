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
package org.codeartisans.qipki.commons.crypto.services;

import java.util.Date;
import org.codeartisans.qipki.commons.crypto.values.KeyPairSpecValue;
import org.codeartisans.qipki.commons.crypto.values.ValidityIntervalValue;
import org.codeartisans.qipki.crypto.algorithms.AsymetricAlgorithm;
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
