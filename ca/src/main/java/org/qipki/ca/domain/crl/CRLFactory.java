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
package org.qipki.ca.domain.crl;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.X509CRL;
import org.qi4j.api.entity.EntityBuilder;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;
import org.qipki.core.QiPkiFailure;
import org.qipki.crypto.io.CryptIO;

@Mixins( CRLFactory.Mixin.class )
@SuppressWarnings( "PublicInnerClass" )
public interface CRLFactory
    extends ServiceComposite
{

    CRL create( X509CRL x509crl );

    abstract class Mixin
        implements CRLFactory
    {

        @Structure
        private UnitOfWorkFactory uowf;
        @Service
        private CryptIO cryptIO;

        @Override
        public CRL create( X509CRL x509crl )
        {
            EntityBuilder<CRL> crlBuilder = uowf.currentUnitOfWork().newEntityBuilder( CRL.class );
            CRL crl = crlBuilder.instance();

            crl.lastCRLNumber().set( BigInteger.ZERO );
            crl = crlBuilder.newInstance();

            FileWriter fileWriter = null;
            try
            {
                fileWriter = new FileWriter( crl.managedFile() );
                fileWriter.write( cryptIO.asPEM( x509crl ).toString() );
                fileWriter.flush();

                return crl;
            }
            catch( IOException ex )
            {
                throw new QiPkiFailure( "Unable to revoke X509", ex );
            }
            finally
            {
                try
                {
                    if( fileWriter != null )
                    {
                        fileWriter.close();
                    }
                }
                catch( IOException ex )
                {
                    throw new QiPkiFailure( "Unable to revoke X509", ex );
                }
            }
        }

    }

}
