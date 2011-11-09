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
package org.qipki.crypto.mac;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public interface MAC
{

    byte[] mac( InputStream data, MACParameters params );

    String hexMac( InputStream data, MACParameters params );

    String base64Mac( InputStream data, MACParameters params );

    byte[] mac( byte[] data, MACParameters params );

    String hexMac( byte[] data, MACParameters params );

    String base64Mac( byte[] data, MACParameters params );

    byte[] mac( String data, MACParameters params );

    String hexMac( String data, MACParameters params );

    String base64Mac( String data, MACParameters params );

    byte[] mac( String data, String encoding, MACParameters params )
            throws UnsupportedEncodingException;

    String hexMac( String data, String encoding, MACParameters params )
            throws UnsupportedEncodingException;

    String base64Mac( String data, String encoding, MACParameters params )
            throws UnsupportedEncodingException;

}
