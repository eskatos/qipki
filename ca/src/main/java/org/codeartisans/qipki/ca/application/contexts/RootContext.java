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
package org.codeartisans.qipki.ca.application.contexts;

import org.codeartisans.qipki.ca.domain.ca.CA;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStoreFactory;
import org.codeartisans.qipki.ca.domain.fragments.PKCS10Signer;
import org.codeartisans.qipki.core.dci.Context;
import org.codeartisans.qipki.ca.domain.ca.CAFactory;
import org.codeartisans.qipki.ca.domain.ca.CARepository;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStore;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStoreRepository;
import org.qi4j.api.injection.scope.Service;

/**
 * TODO Use ServiceReference for lazy service instanciation ?
 */
public class RootContext
        extends Context
{

    @Service
    private CryptoStoreRepository ksRepos;
    @Service
    private CryptoStoreFactory ksFactory;
    @Service
    private CARepository caRepos;
    @Service
    private CAFactory caFactory;

    public CryptoStoreListContext ksListContext()
    {
        context.playRoles( ksRepos, CryptoStoreRepository.class );
        context.playRoles( ksFactory, CryptoStoreFactory.class );
        return subContext( CryptoStoreListContext.class );
    }

    public CryptoStoreContext ksContext( String identity )
    {
        CryptoStore ks = ksRepos.findByIdentity( identity );
        context.playRoles( ks, CryptoStore.class );
        return subContext( CryptoStoreContext.class );
    }

    public CAListContext caListContext()
    {
        context.playRoles( caRepos, CARepository.class );
        context.playRoles( caFactory, CAFactory.class );
        context.playRoles( ksRepos, CryptoStoreRepository.class );
        return subContext( CAListContext.class );
    }

    public CAContext caContext( String identity )
    {
        CA ca = caRepos.findByIdentity( identity );
        context.playRoles( ca, CA.class );
        context.playRoles( ca, PKCS10Signer.class );
        return subContext( CAContext.class );
    }

}
