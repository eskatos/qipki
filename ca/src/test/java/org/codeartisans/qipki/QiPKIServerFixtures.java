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
package org.codeartisans.qipki;

import org.codeartisans.qipki.server.domain.ca.CA;
import org.codeartisans.qipki.server.domain.ca.CAFactory;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.Activatable;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWork;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

@Mixins( QiPKIServerFixtures.Mixin.class )
public interface QiPKIServerFixtures
        extends Activatable, ServiceComposite
{

    public static final String ROOT_CA_NAME = "root";
    public static final String USERS_CA_NAME = "users";
    public static final String SERVICES_CA_NAME = "services";

    abstract class Mixin
            implements QiPKIServerFixtures
    {

        @Structure
        private UnitOfWorkFactory uowf;
        @Service
        private CAFactory caFactory;

        @Override
        public void activate()
                throws Exception
        {
            UnitOfWork uow = uowf.newUnitOfWork();
            CA root = caFactory.create( ROOT_CA_NAME );
            CA users = caFactory.create( USERS_CA_NAME );
            CA services = caFactory.create( SERVICES_CA_NAME );
            uow.complete();
        }

        @Override
        public void passivate()
                throws Exception
        {
        }

    }

}
