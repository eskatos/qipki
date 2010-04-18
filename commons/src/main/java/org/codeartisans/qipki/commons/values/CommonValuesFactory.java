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
package org.codeartisans.qipki.commons.values;

import java.util.Date;
import org.joda.time.Interval;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.value.ValueBuilder;
import org.qi4j.api.value.ValueBuilderFactory;

@Mixins( CommonValuesFactory.Mixin.class )
public interface CommonValuesFactory
        extends ServiceComposite
{

    ValidityPeriod buildValidityPeriod( Interval interval );

    ValidityPeriod buildValidityPeriod( Date notBefore, Date notAfter );

    abstract class Mixin
            implements CommonValuesFactory
    {

        @Structure
        private ValueBuilderFactory vbf;

        @Override
        public ValidityPeriod buildValidityPeriod( Interval interval )
        {
            return buildValidityPeriod( interval.getStart().toDate(), interval.getEnd().toDate() );
        }

        @Override
        public ValidityPeriod buildValidityPeriod( Date notBefore, Date notAfter )
        {
            ValueBuilder<ValidityPeriod> builder = vbf.newValueBuilder( ValidityPeriod.class );
            ValidityPeriod period = builder.prototype();
            period.notBefore().set( notBefore );
            period.notAfter().set( notAfter );
            return builder.newInstance();
        }

    }

}
