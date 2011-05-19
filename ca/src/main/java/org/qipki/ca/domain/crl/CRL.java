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
package org.qipki.ca.domain.crl;

import java.math.BigInteger;

import org.qipki.ca.domain.fragments.HasPEM;
import org.qi4j.api.entity.Queryable;

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

    @Queryable( false )
    Property<BigInteger> lastCRLNumber();

}
