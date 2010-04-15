package org.codeartisans.qipki.ca.domain.crl;

import java.math.BigInteger;
import org.qi4j.api.entity.EntityComposite;
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
public interface CRLEntity
        extends EntityComposite
{

    Property<BigInteger> lastCRLNumber();

    Property<String> pem();

}
