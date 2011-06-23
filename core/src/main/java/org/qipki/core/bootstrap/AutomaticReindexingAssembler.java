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
package org.qipki.core.bootstrap;

import org.qipki.core.reindex.AutomaticReindexerService;

import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.index.reindexer.ReindexerConfiguration;
import org.qi4j.index.reindexer.ReindexerService;

public class AutomaticReindexingAssembler
        implements Assembler
{

    @Override
    @SuppressWarnings( "unchecked" )
    public void assemble( ModuleAssembly ma )
            throws AssemblyException
    {
        ma.addServices( ReindexerService.class ).visibleIn( Visibility.module );
        ma.addEntities( ReindexerConfiguration.class ).visibleIn( Visibility.module );
        ma.addServices( AutomaticReindexerService.class ).visibleIn( Visibility.module ).instantiateOnStartup();
    }

}
