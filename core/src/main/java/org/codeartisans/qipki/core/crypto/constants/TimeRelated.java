package org.codeartisans.qipki.core.crypto.constants;

import org.joda.time.Duration;

public interface TimeRelated
{

    /**
     * Used to prevent clock synchronization issues.
     */
    Duration CLOCK_SKEW_DURATION = Duration.standardMinutes( 10 );
}
