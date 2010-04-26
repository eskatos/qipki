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

import org.codeartisans.qipki.ca.application.contexts.ca.CAContext;
import org.codeartisans.qipki.ca.application.contexts.ca.CAListContext;
import org.codeartisans.qipki.ca.application.contexts.cryptostore.CryptoStoreListContext;
import org.codeartisans.qipki.ca.application.contexts.cryptostore.CryptoStoreContext;
import org.codeartisans.qipki.ca.application.contexts.x509.X509Context;
import org.codeartisans.qipki.ca.application.contexts.x509.X509ListContext;
import org.codeartisans.qipki.ca.domain.ca.CA;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStoreFactory;
import org.codeartisans.qipki.core.dci.Context;
import org.codeartisans.qipki.ca.domain.ca.CAFactory;
import org.codeartisans.qipki.ca.domain.ca.CARepository;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStore;
import org.codeartisans.qipki.ca.domain.cryptostore.CryptoStoreRepository;
import org.codeartisans.qipki.ca.domain.x509.X509;
import org.codeartisans.qipki.ca.domain.x509.X509Factory;
import org.codeartisans.qipki.ca.domain.x509.X509Repository;
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
    @Service
    private X509Factory x509Factory;
    @Service
    private X509Repository x509Repository;

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
        context.playRoles( x509Repository, X509Repository.class );
        return subContext( CAContext.class );
    }

    public X509ListContext x509ListContext()
    {
        context.playRoles( x509Repository, X509Repository.class );
        context.playRoles( x509Factory, X509Factory.class );
        context.playRoles( caRepos, CARepository.class );
        return subContext( X509ListContext.class );
    }

    public X509Context x509Context( String identity )
    {
        X509 x509 = x509Repository.findByIdentity( identity );
        context.playRoles( x509, X509.class );
        return subContext( X509Context.class );
    }

}
