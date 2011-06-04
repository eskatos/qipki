/*
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
package org.qipki.ca.migrations;

import org.qi4j.migration.assembly.MigrationBuilder;
import org.qi4j.migration.assembly.VersionMigrationBuilder;

public class QiPkiCaMigrationBuilder
        extends MigrationBuilder
{

    public QiPkiCaMigrationBuilder()
    {
        super( "1.0-alpha1" );
        to_1_0_alpha8();
    }

    private void to_1_0_alpha8()
    {
        VersionMigrationBuilder to_1_0_alpha8 = toVersion( "1.0-alpha8" );

        to_1_0_alpha8.renamePackage( "org.codeartisans.qipki.domain.ca.profileassignment",
                                     "org.qipki.domain.ca.profileassignment" ).
                withEntities( "X509ProfileAssignmentEntity" );

        to_1_0_alpha8.renamePackage( "org.codeartisans.qipki.ca.domain.ca.root",
                                     "org.qipki.ca.domain.ca.root" ).
                withEntities( "RootCAEntity" );

        to_1_0_alpha8.renamePackage( "org.codeartisans.qipki.ca.domain.ca.sub",
                                     "org.qipki.ca.domain.ca.sub" ).
                withEntities( "SubCAEntity" );

        to_1_0_alpha8.renamePackage( "org.codeartisans.qipki.ca.domain.crl",
                                     "org.qipki.ca.domain.crl" ).
                withEntities( "CRLEntity" );

        to_1_0_alpha8.renamePackage( "org.codeartisans.qipki.ca.domain.cryptostore",
                                     "org.qipki.ca.domain.cryptostore" ).
                withEntities( "CryptoStoreEntity" );

        to_1_0_alpha8.renamePackage( "org.codeartisans.qipki.ca.domain.escrowedkeypair",
                                     "org.qipki.ca.domain.escrowedkeypair" ).
                withEntities( "EscrowedKeyPairEntity" );

        to_1_0_alpha8.renamePackage( "org.codeartisans.qipki.ca.domain.revocation",
                                     "org.qipki.ca.domain.revocation" ).
                withEntities( "RevocationEntity" );

        to_1_0_alpha8.renamePackage( "org.codeartisans.qipki.ca.domain.x509",
                                     "org.qipki.ca.domain.x509" ).
                withEntities( "X509Entity" );

        to_1_0_alpha8.renamePackage( "org.codeartisans.qipki.ca.domain.x509profile",
                                     "org.qipki.ca.domain.x509profile" ).
                withEntities( "X509ProfileEntity" );
    }

}
