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
package org.qipki.crypto.cipher;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;

public interface SymetricCipher
{

    byte[] cipher( String data, Key key );

    byte[] decipher( String ciphered, Key key );

    byte[] cipher( String data, byte[] key );

    byte[] decipher( String ciphered, byte[] key );

    byte[] cipher( byte[] data, Key key );

    byte[] decipher( byte[] ciphered, Key key );

    byte[] cipher( byte[] data, byte[] key );

    byte[] decipher( byte[] ciphered, byte[] key );

    void cipher( InputStream in, OutputStream out, Key key );

    void decipher( InputStream in, OutputStream out, Key key );

    void cipher( InputStream in, OutputStream out, byte[] key );

    void decipher( InputStream in, OutputStream out, byte[] key );

}
