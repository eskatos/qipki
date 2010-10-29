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
package org.codeartisans.qipki.crypto.x509;

public enum X509GeneralName
{

    otherName( org.bouncycastle.asn1.x509.GeneralName.otherName ),
    rfc822Name( org.bouncycastle.asn1.x509.GeneralName.rfc822Name ),
    dNSName( org.bouncycastle.asn1.x509.GeneralName.dNSName ),
    x400Address( org.bouncycastle.asn1.x509.GeneralName.x400Address ),
    directoryName( org.bouncycastle.asn1.x509.GeneralName.directoryName ),
    ediPartyName( org.bouncycastle.asn1.x509.GeneralName.ediPartyName ),
    uniformResourceIdentifier( org.bouncycastle.asn1.x509.GeneralName.uniformResourceIdentifier ),
    iPAddress( org.bouncycastle.asn1.x509.GeneralName.iPAddress ),
    registeredID( org.bouncycastle.asn1.x509.GeneralName.registeredID ),
    unknownGeneralName( -1 );
    private int tag;

    private X509GeneralName( int tag )
    {
        this.tag = tag;
    }

    public int tag()
    {
        return tag;
    }

}
