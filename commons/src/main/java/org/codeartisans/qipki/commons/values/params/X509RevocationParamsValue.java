package org.codeartisans.qipki.commons.values.params;

import org.codeartisans.qipki.crypto.x509.RevocationReason;
import org.qi4j.api.property.Property;
import org.qi4j.api.value.ValueComposite;

public interface X509RevocationParamsValue
        extends ValueComposite
{

    Property<RevocationReason> reason();

}
