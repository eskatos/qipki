package org.codeartisans.qipki.commons.values.rest;

import org.codeartisans.qipki.crypto.x509.RevocationReason;
import org.qi4j.api.property.Property;
import org.qi4j.api.value.ValueComposite;

public interface RevocationValue
        extends RestValue, ValueComposite
{

    Property<String> x509Uri();

    Property<RevocationReason> reason();

}
