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

/**
 * Hash and Block Cipher based Message Authentication Code.
 */
public interface MAC
{

    byte[] mac( InputStream data, HMACParameters params );

    String hexMac( InputStream data, HMACParameters params );

    String base64Mac( InputStream data, HMACParameters params );

    byte[] mac( byte[] data, HMACParameters params );

    String hexMac( byte[] data, HMACParameters params );

    String base64Mac( byte[] data, HMACParameters params );

    byte[] mac( String data, HMACParameters params );

    String hexMac( String data, HMACParameters params );

    String base64Mac( String data, HMACParameters params );

    byte[] mac( String data, String encoding, HMACParameters params )
        throws UnsupportedEncodingException;

    String hexMac( String data, String encoding, HMACParameters params )
        throws UnsupportedEncodingException;

    String base64Mac( String data, String encoding, HMACParameters params )
        throws UnsupportedEncodingException;

    byte[] mac( InputStream data, BCMACParameters params );

    String hexMac( InputStream data, BCMACParameters params );

    String base64Mac( InputStream data, BCMACParameters params );

    byte[] mac( byte[] data, BCMACParameters params );

    String hexMac( byte[] data, BCMACParameters params );

    String base64Mac( byte[] data, BCMACParameters params );

    byte[] mac( String data, BCMACParameters params );

    String hexMac( String data, BCMACParameters params );

    String base64Mac( String data, BCMACParameters params );

    byte[] mac( String data, String encoding, BCMACParameters params )
        throws UnsupportedEncodingException;

    String hexMac( String data, String encoding, BCMACParameters params )
        throws UnsupportedEncodingException;

    String base64Mac( String data, String encoding, BCMACParameters params )
        throws UnsupportedEncodingException;

}
