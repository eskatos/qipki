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
package org.codeartisans.qipki.crypto.algorithms;

/**
 * @see http://en.wikipedia.org/wiki/Signature_(cryptography)
 */
public enum SignatureAlgorithm
        implements Algorithm
{

    // DSA currently just supports SHA-1.
    SHA1withDSA( "SHA1withDSA" ),
    // ECDSA is supported with both the SHA-1 and SHA-2 family of digest algorithms.
    SHA1withECDSA( "SHA1withECDSA" ),
    SHA224withECDSA( "SHA224withECDSA" ),
    SHA256withECDSA( "SHA256withECDSA" ),
    SHA384withECDSA( "SHA384withECDSA" ),
    SHA512withECDSA( "SHA512withECDSA" ),
    // A variety of digests can be used to sign using the RSA algorithm
    MD2withRSA( "MD2withRSA" ),
    MD5withRSA( "MD5withRSA" ),
    SHA1withRSA( "SHA1withRSA" ),
    SHA224withRSA( "SHA224withRSA" ),
    SHA256withRSA( "SHA256withRSA" ),
    SHA384withRSA( "SHA384withRSA" ),
    SHA512withRSA( "SHA512withRSA" ),
    RIPEMD160withRSA( "RIPEMD160withRSA" ),
    RIPEMD128withRSA( "RIPEMD128withRSA" ),
    RIPEMD256withRSA( "RIPEMD256withRSA" );
    private String algo;

    private SignatureAlgorithm( String algo )
    {
        this.algo = algo;
    }

    @Override
    public String jcaString()
    {
        return algo;
    }

}
