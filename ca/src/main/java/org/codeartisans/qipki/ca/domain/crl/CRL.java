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
package org.codeartisans.qipki.ca.domain.crl;

import java.math.BigInteger;
import org.codeartisans.qipki.ca.domain.fragments.HasPEM;
import org.qi4j.api.property.Property;

/**
 *
 * NETSCAPE WAY :
 *
 *      http://www.netscape.ca/browser/netscape8/help/en/ssl_help.html#next_update
 *
 *
 * MICROSOFT WAY :
 *
 * The Effective Date must not be earlier than the CA certificate became valid. This is because the CRL carries the CA certificate's signature.
 *
 *      Effective Date = max(Current Time - ClockSkewMinutes, NotBefore_date_from_the_CA_certificate)
 *
 * The Next CRL Publish for a base CRL is calculated as the sum of current time plus CRLPeriod:
 *
 *      Next CRL Publish = Current Time + (CRLPeriodUnits * CRLPeriod)
 *
 * The Next CRL Publish for a delta CRL is calculated as the sum of current time plus CRLDeltaPeriod:
 *
 *      Next CRL Publish = Current Time + (CRLDeltaPeriodUnits * CRLDeltaPeriod)
 *
 * The Next Update is for a base CRL is calculated with the following formula:
 *
 *      NextUpdate = min(Current Time + (CRLPeriodUnits * CRLPeriod) + OverlapPeriod + ClockSkewMinutes, NotAfter_date_from_the_CA_certificate)
 *
 * The Next Update is for a delta CRL is calculated with the following formula:
 *
 *      NextUpdate = min(Current Time + (CRLDeltaPeriodUnits * CRLDeltaPeriod) + OverlapPeriod + ClockSkewMinutes, NotAfter_date_from_the_CA_certificate)
 */
public interface CRL
        extends HasPEM
{

    Property<BigInteger> lastCRLNumber();

}
