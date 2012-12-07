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
package org.qipki.ca.application.contexts.cryptostore;

import org.qi4j.api.query.Query;
import org.qipki.ca.domain.cryptostore.CryptoStore;
import org.qipki.ca.domain.cryptostore.CryptoStoreFactory;
import org.qipki.ca.domain.cryptostore.CryptoStoreRepository;
import org.qipki.core.dci.Context;
import org.qipki.crypto.storage.KeyStoreType;

public class CryptoStoreListContext
    extends Context
{

    public Query<CryptoStore> list( int start )
    {
        return context.role( CryptoStoreRepository.class ).findAllPaginated( start, 25 );
    }

    public CryptoStore createCryptoStore( String name, KeyStoreType storeType, char[] password )
    {
        return context.role( CryptoStoreFactory.class ).create( name, storeType, password );
    }

}
