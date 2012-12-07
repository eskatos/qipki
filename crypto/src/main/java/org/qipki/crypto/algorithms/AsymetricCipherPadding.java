/*
 * Copyright (c) 2011, Paul Merlin. All Rights Reserved.
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
package org.qipki.crypto.algorithms;

public enum AsymetricCipherPadding
    implements Algorithm
{

    PKCS1( "PKCS1Padding" ),
    OAEP( "OAEPPadding" ),
    OAEP_MD5_MGF1( "OAEPWithMD5AndMGF1Padding" ),
    OAEP_SHA1_MGF1( "OAEPWithSHA-1AndMGF1Padding" ),
    OAEP_SHA256_MGF1( "OAEPWithSHA-256AndMGF1Padding" ),
    OAEP_SHA384_MGF1( "OAEPWithSHA-384AndMGF1Padding" ),
    OAEP_SHA512_MGF1( "OAEPWithSHA-512AndMGF1Padding" );
    private String jcaString;

    private AsymetricCipherPadding( String jcaString )
    {
        this.jcaString = jcaString;
    }

    @Override
    public String jcaString()
    {
        return jcaString;
    }

}
