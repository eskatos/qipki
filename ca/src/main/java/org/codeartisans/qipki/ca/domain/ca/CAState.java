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
package org.codeartisans.qipki.ca.domain.ca;

import org.codeartisans.qipki.ca.domain.ca.profileassignment.X509ProfileAssignment;
import org.codeartisans.qipki.ca.domain.crl.CRL;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStore;
import org.codeartisans.qipki.commons.fragments.Nameable;
import org.qi4j.api.common.UseDefaults;
import org.qi4j.api.entity.Aggregated;
import org.qi4j.api.entity.Identity;
import org.qi4j.api.entity.association.Association;
import org.qi4j.api.entity.association.ManyAssociation;

public interface CAState
        extends Nameable, Identity
{

    Association<CryptoStore> cryptoStore();

    @Aggregated
    @UseDefaults
    ManyAssociation<X509ProfileAssignment> allowedX509Profiles();

    @Aggregated
    Association<CRL> crl();

}
