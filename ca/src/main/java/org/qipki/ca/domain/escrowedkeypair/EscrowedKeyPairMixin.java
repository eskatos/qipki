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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.KeyPair;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.This;
import org.qi4j.library.uowfile.singular.UoWFileLocator;
import org.qipki.crypto.io.CryptIO;

public abstract class EscrowedKeyPairMixin
    implements EscrowedKeyPairBehavior, UoWFileLocator
{

    @This
    private EscrowedKeyPair me;
    @Service
    private EscrowedKeyPairFileService fileService;
    @Service
    private CryptIO cryptio;

    @Override
    public File locateAttachedFile()
    {
        return fileService.getEscrowedKeyPairFile( me );
    }

    @Override
    public KeyPair keyPair()
    {
        try
        {
            return cryptio.readKeyPairPEM( new FileReader( me.managedFile() ) );
        }
        catch( FileNotFoundException ex )
        {
            throw new IllegalStateException( ex.getMessage(), ex );
        }
    }

}
