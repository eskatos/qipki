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
package org.qipki.crypto.digest;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public interface Digester
{

    DigestParametersBuilder newParamsBuilder();

    byte[] generateSalt( int length );

    byte[] digest( InputStream data, DigestParameters params );

    String hexDigest( InputStream data, DigestParameters params );

    String base64Digest( InputStream data, DigestParameters params );

    byte[] digest( byte[] data, DigestParameters params );

    String hexDigest( byte[] data, DigestParameters params );

    String base64Digest( byte[] data, DigestParameters params );

    byte[] digest( File data, DigestParameters params );

    String hexDigest( File data, DigestParameters params );

    String base64Digest( File data, DigestParameters params );

    byte[] digest( String data, DigestParameters params );

    String hexDigest( String data, DigestParameters params );

    String base64Digest( String data, DigestParameters params );

    byte[] digest( String data, String encoding, DigestParameters params )
        throws UnsupportedEncodingException;

    String hexDigest( String data, String encoding, DigestParameters params )
        throws UnsupportedEncodingException;

    String base64Digest( String data, String encoding, DigestParameters params )
        throws UnsupportedEncodingException;

}
