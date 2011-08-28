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
package org.qipki.ca.domain.ca;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bouncycastle.asn1.misc.MiscObjectIdentifiers;
import org.bouncycastle.asn1.misc.NetscapeCertType;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.PKCS10CertificationRequest;

import org.joda.time.Duration;

import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.This;

import org.qipki.ca.application.WrongParametersBuilder;
import org.qipki.ca.domain.ca.profileassignment.X509ProfileAssignment;
import org.qipki.ca.domain.ca.root.RootCAMixin;
import org.qipki.ca.domain.ca.sub.SubCA;
import org.qipki.ca.domain.revocation.Revocation;
import org.qipki.ca.domain.revocation.RevocationFactory;
import org.qipki.ca.domain.x509.X509;
import org.qipki.ca.domain.x509profile.X509Profile;
import org.qipki.core.QiPkiFailure;
import org.qipki.crypto.io.CryptIO;
import org.qipki.crypto.x509.DistinguishedName;
import org.qipki.crypto.x509.RevocationReason;
import org.qipki.crypto.x509.X509Generator;
import org.qipki.crypto.x509.X509ExtensionsReader;
import org.qipki.crypto.x509.X509ExtensionHolder;
import org.qipki.crypto.x509.X509ExtensionsBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CAMixin
        implements CABehavior
{

    private static final Logger LOGGER = LoggerFactory.getLogger( RootCAMixin.class );
    private static final Set<String> ALLOWED_REQUESTED_EXTENSIONS = new HashSet<String>();

    static {
        ALLOWED_REQUESTED_EXTENSIONS.add( X509Extensions.SubjectAlternativeName.getId() );
    }

    @Service
    private X509Generator x509Generator;
    @Service
    private X509ExtensionsReader x509ExtReader;
    @Service
    private X509ExtensionsBuilder x509ExtBuilder;
    @Service
    private CryptIO cryptIO;
    @Service
    private RevocationFactory revocationFactory;
    @This
    private CA me;

    @Override
    public DistinguishedName distinguishedName()
    {
        return new DistinguishedName( certificate().getSubjectX500Principal() );
    }

    @Override
    public DistinguishedName issuerDistinguishedName()
    {
        return new DistinguishedName( certificate().getIssuerX500Principal() );
    }

    @Override
    public X509Certificate certificate()
    {
        return me.cryptoStore().get().getX509Certificate( me.identity().get() );
    }

    @Override
    public PrivateKey privateKey()
    {
        return me.cryptoStore().get().getPrivateKey( me.identity().get() );
    }

    @Override
    public X509Certificate sign( X509Profile x509profile, PKCS10CertificationRequest pkcs10 )
    {
        LOGGER.debug( "Handling a PKCS#10 Certificate Signing Request using X509Profile " + x509profile.name().get() );
        try {

            ensureX509ProfileIsAllowed( x509profile );

            List<X509ExtensionHolder> extensions = x509ExtReader.extractRequestedExtensions( pkcs10 );
            ensureNoIllegalRequestedExtensions( extensions );

            // Adding extensions commons to all profiles
            SubjectKeyIdentifier subjectKeyID = x509ExtBuilder.buildSubjectKeyIdentifier( pkcs10.getPublicKey() );
            extensions.add( new X509ExtensionHolder( X509Extensions.SubjectKeyIdentifier, false, subjectKeyID ) );
            AuthorityKeyIdentifier authKeyID = x509ExtBuilder.buildAuthorityKeyIdentifier( certificate().getPublicKey() );
            extensions.add( new X509ExtensionHolder( X509Extensions.AuthorityKeyIdentifier, false, authKeyID ) );

            // Applying X509Profile on issued X509Certificate
            if ( x509profile.basicConstraints().get().subjectIsCA().get() ) {
                BasicConstraints bc = x509ExtBuilder.buildCABasicConstraints( x509profile.basicConstraints().get().pathLengthConstraint().get() );
                extensions.add( new X509ExtensionHolder( X509Extensions.BasicConstraints, x509profile.basicConstraints().get().critical().get(), bc ) );
            } else {
                BasicConstraints bc = x509ExtBuilder.buildNonCABasicConstraints();
                extensions.add( new X509ExtensionHolder( X509Extensions.BasicConstraints, x509profile.basicConstraints().get().critical().get(), bc ) );
            }
            KeyUsage keyUsages = x509ExtBuilder.buildKeyUsages( x509profile.keyUsages().get().keyUsages().get() );
            extensions.add( new X509ExtensionHolder( X509Extensions.KeyUsage, x509profile.keyUsages().get().critical().get(), keyUsages ) );

            ExtendedKeyUsage extendedKeyUsage = x509ExtBuilder.buildExtendedKeyUsage( x509profile.extendedKeyUsages().get().extendedKeyUsages().get() );
            extensions.add( new X509ExtensionHolder( X509Extensions.ExtendedKeyUsage, x509profile.extendedKeyUsages().get().critical().get(), extendedKeyUsage ) );

            NetscapeCertType netscapeCertType = x509ExtBuilder.buildNetscapeCertTypes( x509profile.netscapeCertTypes().get().netscapeCertTypes().get() );
            extensions.add( new X509ExtensionHolder( MiscObjectIdentifiers.netscapeCertType, x509profile.netscapeCertTypes().get().critical().get(), netscapeCertType ) );

            String[] crlDistPoints = gatherCRLDistributionPoints();
            if ( crlDistPoints.length > 0 ) {
                CRLDistPoint crlDistPointsExt = x509ExtBuilder.buildCRLDistributionPoints( certificate().getSubjectX500Principal(), crlDistPoints );
                extensions.add( new X509ExtensionHolder( X509Extensions.CRLDistributionPoints, false, crlDistPointsExt ) );
            }

            DistinguishedName issuerDN = new DistinguishedName( certificate().getSubjectX500Principal() );
            DistinguishedName subjectDN = new DistinguishedName( pkcs10.getCertificationRequestInfo().getSubject() );
            X509Certificate certificate = x509Generator.generateX509Certificate( privateKey(),
                                                                                 issuerDN,
                                                                                 BigInteger.probablePrime( 120, new SecureRandom() ),
                                                                                 subjectDN,
                                                                                 pkcs10.getPublicKey(),
                                                                                 Duration.standardDays( x509profile.validityDays().get() ),
                                                                                 extensions );

            return certificate;

        } catch ( GeneralSecurityException ex ) {
            LOGGER.error( ex.getMessage(), ex );
            throw new QiPkiFailure( "Unable to enroll PKCS#10", ex );
        }
    }

    /**
     * Climb up the CA hierarchy to find CRL Distribution Points.
     * 
     * Stops at the first CA defining distribution points.
     */
    private String[] gatherCRLDistributionPoints()
    {
        List<String> distPoints = new ArrayList<String>();
        distPoints.addAll( me.crlDistPoints().get() );
        CA currentCa = me;
        while ( distPoints.isEmpty() && currentCa instanceof SubCA ) {
            currentCa = ( ( SubCA ) currentCa ).issuer().get();
            distPoints.addAll( currentCa.crlDistPoints().get() );
        }
        return distPoints.toArray( new String[ distPoints.size() ] );
    }

    private void ensureX509ProfileIsAllowed( X509Profile x509profile )
    {
        for ( X509ProfileAssignment eachAssignment : me.allowedX509Profiles() ) {
            if ( eachAssignment.x509Profile().get().equals( x509profile ) ) {
                return;
            }
        }
        throw new WrongParametersBuilder().illegals( "X509Profile " + x509profile.name().get() + " is not allowed on CA " + me.name().get() ).build();
    }

    private void ensureNoIllegalRequestedExtensions( List<X509ExtensionHolder> requestedExtensions )
    {
        for ( X509ExtensionHolder eachDerOid : requestedExtensions ) {
            if ( !ALLOWED_REQUESTED_EXTENSIONS.contains( eachDerOid.getDerOID().getId() ) ) {
                throw new WrongParametersBuilder().illegals( "Illegal requested extension in PKCS#10 request: " + eachDerOid.getDerOID().getId() ).build();
            }
        }
    }

    @Override
    public Revocation revoke( X509 x509, RevocationReason reason )
    {
        Revocation revocation = revocationFactory.create( x509, reason );
        File pemFile = me.crl().get().pemFile();
        me.crl().get().lastCRLNumber().set( me.crl().get().lastCRLNumber().get().add( BigInteger.ONE ) );
        X509CRL x509CRL;

        FileReader fileReader = null;
        try {
            fileReader = new FileReader( pemFile );
            x509CRL = cryptIO.readCRLPEM( fileReader );
        } catch ( IOException ex ) {
            throw new QiPkiFailure( "Unable to revoke X509", ex );
        } finally {
            try {
                if ( fileReader != null ) {
                    fileReader.close();
                }
            } catch ( IOException ex ) {
                throw new QiPkiFailure( "Unable to revoke X509", ex );
            }
        }

        x509CRL = x509Generator.updateX509CRL( certificate(), privateKey(),
                                               x509.x509Certificate(), reason,
                                               x509CRL, me.crl().get().lastCRLNumber().get() );

        FileWriter fileWriter = null;
        try {

            fileWriter = new FileWriter( pemFile );
            fileWriter.write( cryptIO.asPEM( x509CRL ).toString() );
            fileWriter.flush();

            return revocation;
        } catch ( IOException ex ) {
            throw new QiPkiFailure( "Unable to revoke X509", ex );
        } finally {
            try {
                if ( fileWriter != null ) {
                    fileWriter.close();
                }
            } catch ( IOException ex ) {
                throw new QiPkiFailure( "Unable to revoke X509", ex );
            }
        }
    }

}
