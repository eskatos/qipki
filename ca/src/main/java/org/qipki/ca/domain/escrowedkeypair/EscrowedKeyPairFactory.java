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
package org.qipki.ca.domain.escrowedkeypair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyPair;

import org.qi4j.api.entity.EntityBuilder;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.mixin.Mixins;
import org.qi4j.api.service.ServiceComposite;
import org.qi4j.api.unitofwork.UnitOfWorkFactory;

import org.qipki.crypto.algorithms.AsymetricAlgorithm;
import org.qipki.crypto.asymetric.AsymetricGenerator;
import org.qipki.crypto.asymetric.AsymetricGeneratorParameters;
import org.qipki.crypto.io.CryptIO;

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
        private EscrowedKeyPairFileService fileService;
        @Service
        private AsymetricGenerator asymGenerator;
        @Service
        private CryptIO cryptIO;

        @Override
        public EscrowedKeyPair create( AsymetricAlgorithm algorithm, Integer length )
        {
            EntityBuilder<EscrowedKeyPair> builder = uowf.currentUnitOfWork().newEntityBuilder( EscrowedKeyPair.class );

            EscrowedKeyPair ekp = builder.instance();

            ekp.algorithm().set( algorithm );
            ekp.length().set( length );

            ekp = builder.newInstance();

            KeyPair keyPair = asymGenerator.generateKeyPair( new AsymetricGeneratorParameters( algorithm, length ) );
            writeToFile( cryptIO.asPEM( keyPair ), fileService.getEscrowedKeyPairFile( ekp ) );

            return ekp;
        }

        // TODO move this in qi4j-io
        private static void writeToFile( CharSequence chars, File dest )
        {
            OutputStream out = null;
            try {
                out = new FileOutputStream( dest );
                PrintWriter printWriter = new PrintWriter( new OutputStreamWriter( out, "UTF-8" ) );
                printWriter.println( chars );
                printWriter.flush();
                out.flush();
            } catch ( IOException e ) {
                throw new RuntimeException( e.getMessage(), e );
            } finally {
                try {
                    if ( out != null ) {
                        out.close();
                    }
                } catch ( Exception ignored ) {
                }
            }
        }

    }

}
