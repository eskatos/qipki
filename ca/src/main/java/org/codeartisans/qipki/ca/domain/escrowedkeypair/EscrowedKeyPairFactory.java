/*
 * Created on 20 sept. 2010
 *
 * Licenced under the Netheos Licence, Version 1.0 (the "Licence"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at :
 *
 * http://www.netheos.net/licences/LICENCE-1.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright (c) Netheos
 */
package org.codeartisans.qipki.ca.domain.escrowedkeypair;

import java.security.KeyPair;

import org.codeartisans.qipki.crypto.algorithms.AsymetricAlgorithm;
import org.codeartisans.qipki.crypto.asymetric.AsymetricGenerator;
import org.codeartisans.qipki.crypto.asymetric.AsymetricGeneratorParameters;
import org.codeartisans.qipki.crypto.io.CryptIO;

import org.qi4j.api.entity.EntityBuilder;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

@Mixins( EscrowedKeyPairFactory.Mixin.class )
public interface EscrowedKeyPairFactory
        extends ServiceComposite
{

    EscrowedKeyPair create( AsymetricAlgorithm algorithm, Integer length );

    @SuppressWarnings( "PublicInnerClass" )
    abstract class Mixin
            implements EscrowedKeyPairFactory
    {

        @Structure
        private UnitOfWorkFactory uowf;
        @Service
        private AsymetricGenerator asymGenerator;
        @Service
        private CryptIO cryptIO;

        @Override
        public EscrowedKeyPair create( AsymetricAlgorithm algorithm, Integer length )
        {
            KeyPair keyPair = asymGenerator.generateKeyPair( new AsymetricGeneratorParameters( algorithm, length ) );
            EntityBuilder<EscrowedKeyPair> builder = uowf.currentUnitOfWork().newEntityBuilder( EscrowedKeyPair.class );
            EscrowedKeyPair ekp = builder.instance();
            ekp.pem().set( cryptIO.asPEM( keyPair ).toString() );
            ekp.algorithm().set( algorithm );
            ekp.length().set( length );
            return builder.newInstance();
        }

    }

}
