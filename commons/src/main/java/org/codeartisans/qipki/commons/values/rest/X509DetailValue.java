/*
 * Copyright (c) 2010 Paul Merlin <paul@nosphere.org>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.codeartisans.qipki.commons.values.rest;

import org.codeartisans.qipki.commons.values.crypto.x509.KeysExtensionsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.ConstraintsExtensionsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.PoliciesExtensionsValue;
import org.codeartisans.qipki.commons.values.crypto.x509.NamesExtensionsValue;
import org.qi4j.api.common.Optional;
import org.qi4j.api.property.Property;

public interface X509DetailValue
        extends X509Value
{

    Property<Integer> certificateVersion();

    Property<String> signatureAlgorithm();

    Property<String> publicKeyAlgorithm();

    Property<Integer> publicKeySize();

    Property<String> md5Fingerprint();

    Property<String> sha1Fingerprint();

    Property<String> sha256Fingerprint();

    @Optional
    Property<String> netscapeCertComment();

    @Optional
    Property<KeysExtensionsValue> keysExtensions();

    @Optional
    Property<PoliciesExtensionsValue> policiesExtensions();

    @Optional
    Property<NamesExtensionsValue> namesExtensions();

    @Optional
    Property<ConstraintsExtensionsValue> constraintsExtensions();

}
