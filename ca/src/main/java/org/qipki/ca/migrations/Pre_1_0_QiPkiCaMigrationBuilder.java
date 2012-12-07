/*
 * Copyright (c) 2011, Paul Merlin. All Rights Reserved.
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

import org.qi4j.migration.assembly.MigrationBuilder;
import org.qi4j.migration.assembly.VersionMigrationBuilder;

public class Pre_1_0_QiPkiCaMigrationBuilder
    extends MigrationBuilder
{

    public Pre_1_0_QiPkiCaMigrationBuilder()
    {
        super( "1.0-alpha1" );
        to_1_0();
    }

    private void to_1_0()
    {
        VersionMigrationBuilder to_1_0 = toVersion( "1.0-alpha8" );
        to_1_0_package_rename( to_1_0 );
    }

    private static void to_1_0_package_rename( VersionMigrationBuilder to_1_0 )
    {
        to_1_0.renamePackage( "org.codeartisans.qipki.domain.ca.profileassignment",
                              "org.qipki.domain.ca.profileassignment" ).
            withEntities( "X509ProfileAssignmentEntity" );

        to_1_0.renamePackage( "org.codeartisans.qipki.ca.domain.ca.root",
                              "org.qipki.ca.domain.ca.root" ).
            withEntities( "RootCAEntity" );

        to_1_0.renamePackage( "org.codeartisans.qipki.ca.domain.ca.sub",
                              "org.qipki.ca.domain.ca.sub" ).
            withEntities( "SubCAEntity" );

        to_1_0.renamePackage( "org.codeartisans.qipki.ca.domain.crl",
                              "org.qipki.ca.domain.crl" ).
            withEntities( "CRLEntity" );

        to_1_0.renamePackage( "org.codeartisans.qipki.ca.domain.cryptostore",
                              "org.qipki.ca.domain.cryptostore" ).
            withEntities( "CryptoStoreEntity" );

        to_1_0.renamePackage( "org.codeartisans.qipki.ca.domain.escrowedkeypair",
                              "org.qipki.ca.domain.escrowedkeypair" ).
            withEntities( "EscrowedKeyPairEntity" );

        to_1_0.renamePackage( "org.codeartisans.qipki.ca.domain.revocation",
                              "org.qipki.ca.domain.revocation" ).
            withEntities( "RevocationEntity" );

        to_1_0.renamePackage( "org.codeartisans.qipki.ca.domain.x509",
                              "org.qipki.ca.domain.x509" ).
            withEntities( "X509Entity" );

        to_1_0.renamePackage( "org.codeartisans.qipki.ca.domain.x509profile",
                              "org.qipki.ca.domain.x509profile" ).
            withEntities( "X509ProfileEntity" );
    }

}
