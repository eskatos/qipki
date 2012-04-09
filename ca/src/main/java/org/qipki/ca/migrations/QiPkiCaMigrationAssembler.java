/*
 * Copyright (c) 2012, Paul Merlin. All Rights Reserved.
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
package org.qipki.ca.migrations;

import org.qi4j.api.service.importer.NewObjectImporter;
import org.qi4j.bootstrap.Assembler;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.migration.MigrationConfiguration;
import org.qi4j.migration.MigrationEventLogger;
import org.qi4j.migration.MigrationService;
import org.qi4j.migration.assembly.MigrationBuilder;

public class QiPkiCaMigrationAssembler
        implements Assembler
{

    @Override
    @SuppressWarnings( "unchecked" )
    public void assemble( ModuleAssembly module )
            throws AssemblyException
    {
        module.objects( MigrationEventLogger.class );
        module.importedServices( MigrationEventLogger.class ).importedBy( NewObjectImporter.class );

        MigrationBuilder pre_1_0_migration = new Pre_1_0_QiPkiCaMigrationBuilder();

        module.services( MigrationService.class ).setMetaInfo( pre_1_0_migration );
        module.entities( MigrationConfiguration.class );
        module.forMixin( MigrationConfiguration.class ).declareDefaults().lastStartupVersion().set( "1.0-alpha1" );
    }

}
