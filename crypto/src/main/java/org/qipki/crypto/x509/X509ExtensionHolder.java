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
package org.qipki.crypto.x509;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DEREncodable;

public final class X509ExtensionHolder
{

    private final ASN1ObjectIdentifier asn1OID;
    private final boolean critical;
    private final DEREncodable value;

    public X509ExtensionHolder( ASN1ObjectIdentifier asn1OID, boolean critical, DEREncodable value )
    {
        this.asn1OID = asn1OID;
        this.critical = critical;
        this.value = value;
    }

    public ASN1ObjectIdentifier getASN1OID()
    {
        return asn1OID;
    }

    public boolean isCritical()
    {
        return critical;
    }

    public DEREncodable getValue()
    {
        return value;
    }

}
