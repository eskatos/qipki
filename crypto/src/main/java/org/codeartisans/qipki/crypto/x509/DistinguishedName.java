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
package org.codeartisans.qipki.crypto.x509;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x509.X509Name;

import org.codeartisans.java.toolbox.StringUtils;

import sun.security.pkcs.PKCS9Attribute;
import sun.security.x509.AVA;
import sun.security.x509.RDN;
import sun.security.x509.X500Name;

/**
 * Handle distinguished names string representation according to RFC1779 and RFC2253 plus the attributes keywords listed in RFC3383.
 *
 * RFC1779 - http://www.ietf.org/rfc/rfc1779.txt
 *      1995 - A String Representation of Distinguished Names
 *
 * RFC2253 - http://www.ietf.org/rfc/rfc2253.txt
 *      1997 - Lightweight Directory Access Protocol (v3): UTF-8 String Representation of Distinguished Names
 *
 * RFC3383 - http://www.ietf.org/rfc/rfc3383.txt
 *      2002 -  Internet Assigned Numbers Authority (IANA) Considerations for the Lightweight Directory Access Protocol (LDAP)
 *      Especially the section B.3 in Appendix B titled : Object Identifier Descriptors
 * 
 */
public final class DistinguishedName
{

    /**
     * Distinguished name string representation format.
     */
    @SuppressWarnings( "PublicInnerClass" )
    public enum Format
    {

        RFC1779,
        RFC2253,
        RFC2253_CANONICAL,
        RFC1779_and_RFC3383,
        RFC2253_and_RFC3383
    }

    private final X500Principal x500Principal;
    private boolean removeEmptyRDNs;

    public DistinguishedName( X509Name x509Name )
    {
        this( x509Name.toString() );
    }

    public DistinguishedName( X500Principal x500Principal )
    {
        this.x500Principal = x500Principal;
    }

    public DistinguishedName( final String stringRepresentation )
    {
        this( new X500Principal( stringRepresentation, ADDITIONAL_KEYWORDS ) );
    }

    public void setRemoveEmptyRDNs( boolean removeEmptyRDNs )
    {
        this.removeEmptyRDNs = removeEmptyRDNs;
    }

    /**
     * @return          String representation according to RFC2253 format and RFC3383 keywords.
     */
    @Override
    public String toString()
    {
        return toString( Format.RFC2253_and_RFC3383 );
    }

    /**
     * @param format    Distinguished name string representation format.
     * @return          String representation according to the given format.
     */
    public String toString( Format format )
    {
        X500Principal ppalToUse = x500Principal;
        if ( removeEmptyRDNs ) {
            try {
                X500Name x500Name = X500Name.asX500Name( x500Principal );
                List<RDN> rdns = new ArrayList<RDN>();
                for ( RDN eachRDN : x500Name.rdns() ) {
                    if ( !isEmpty( eachRDN ) ) {
                        rdns.add( eachRDN );
                    }
                }
                X500Name afterX500Name = new X500Name( rdns.toArray( new RDN[ rdns.size() ] ) );
                ppalToUse = new X500Principal( afterX500Name.getRFC2253Name( ADDITIONAL_KEYWORDS ), ADDITIONAL_KEYWORDS );
            } catch ( IOException ignored ) {
                // Should not happen, we already passed this data into java APIs
            }
        }
        switch ( format ) {
            case RFC1779:
                return ppalToUse.getName( X500Principal.RFC1779 );
            case RFC2253:
                return ppalToUse.getName( X500Principal.RFC2253 );
            case RFC1779_and_RFC3383:
                return ppalToUse.getName( X500Principal.RFC1779, RFC3383_OID_MAPPINGS );
            case RFC2253_and_RFC3383:
                return ppalToUse.getName( X500Principal.RFC2253, RFC3383_OID_MAPPINGS );
            case RFC2253_CANONICAL:
                return ppalToUse.getName( X500Principal.CANONICAL );
            default:
                throw new IllegalArgumentException( "Given DN format is not supported: " + format.name() );
        }
    }

    public X500Principal toX500Principal()
    {
        return new X500Principal( toString( DistinguishedName.Format.RFC2253_and_RFC3383 ), ADDITIONAL_KEYWORDS );
    }

    private boolean isEmpty( RDN rdn )
    {
        if ( rdn.size() > 0 ) {
            for ( AVA eachAVA : rdn.avas() ) {
                if ( !StringUtils.isEmpty( eachAVA.getValueString() ) ) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * RFC2253 Chars that needs to be escaped.
     */
    private static final String RFC2253_ESCAPED_CHARS = ",=\"+<>#;\\";

    public static String escapeRDNData( String data )
    {
        if ( data == null ) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for ( char eachChar : data.toCharArray() ) {
            if ( RFC2253_ESCAPED_CHARS.indexOf( eachChar ) != -1 ) {
                sb.append( '\\' );
            }
            sb.append( eachChar );
        }
        return sb.toString();
    }

    /**
     * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4531319
     */
    public static final LinkedHashMap<String, String> ADDITIONAL_KEYWORDS = new LinkedHashMap<String, String>();
    /**
     * RFC3383 Attributes keywords
     */
    public static final LinkedHashMap<String, String> RFC3383_OID_MAPPINGS = new LinkedHashMap<String, String>();

    static {
        // WARNING !
        // Last inserted mappings have precedence when going from X500Principal to String
        // There are several keywords for the same OID
        // We must state on keywords priority because of the Java impl and that's not done here, only
        // the most common keywords are repeated at the end of the insertions so that we get usable DNs for now.
        RFC3383_OID_MAPPINGS.put( "2.5.4.1", "aliasedEntryName" ); // [X.501]
        RFC3383_OID_MAPPINGS.put( "2.5.4.1", "aliasedObjectName" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "1.3.6.1.4.1.1466.101.120.6", "altServer" ); // [RFC2252]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.26", "aRecord" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.37", "associatedDomain" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "1.3.6.1.4.1.453.7.2.8", "associatedInternetGateway" ); // [RFC2164]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.38", "associatedName" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "1.3.6.1.4.1.453.7.2.6", "associatedORAddress" ); // [RFC2164]
        RFC3383_OID_MAPPINGS.put( "1.3.6.1.4.1.453.7.2.3", "associatedX400Gateway" ); // [RFC2164]
        RFC3383_OID_MAPPINGS.put( "2.5.21.5", "attributeTypes" ); // [RFC2252]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.55", "audio" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.4.38", "authorityRevocationList" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.48", "buildingName" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.4.15", "businessCategory" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.6", "C" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.37", "cACertificate" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "1.2.840.113556.1.4.481", "calCalAdrURI" ); // [RFC2739]
        RFC3383_OID_MAPPINGS.put( "1.2.840.113556.1.4.478", "calCalURI" ); // [RFC2739]
        RFC3383_OID_MAPPINGS.put( "1.2.840.113556.1.4.480", "calCAPURI" ); // [RFC2739]
        RFC3383_OID_MAPPINGS.put( "1.2.840.113556.1.4.479", "calFBURL" ); // [RFC2739]
        RFC3383_OID_MAPPINGS.put( "1.2.840.113556.1.4.485", "calOtherCalAdrURIs" ); // [RFC2739]
        RFC3383_OID_MAPPINGS.put( "1.2.840.113556.1.4.482", "calOtherCalURIs" ); // [RFC2739]
        RFC3383_OID_MAPPINGS.put( "1.2.840.113556.1.4.484", "calOtherCAPURIs" ); // [RFC2739]
        RFC3383_OID_MAPPINGS.put( "1.2.840.113556.1.4.483", "calOtherFBURLs" ); // [RFC2739]
        RFC3383_OID_MAPPINGS.put( "2.5.4.39", "certificateRevocationList" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.3", "CN" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.31", "cNAMERecord" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.43", "co" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.4.3", "commonName" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.6", "countryName" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.18.1", "createTimestamp" ); // [RFC2252]
        RFC3383_OID_MAPPINGS.put( "2.5.18.3", "creatorsName" ); // [RFC2252]
        RFC3383_OID_MAPPINGS.put( "2.5.4.40", "crossCertificatePair" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.25", "DC" ); // [RFC2247]
        RFC3383_OID_MAPPINGS.put( "2.5.4.53", "deltaRevocationList" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.13", "description" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.27", "destinationIndicator" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.49", "distinguishedName" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "1.3.6.1.4.1.453.7.2.3", "distinguishedNameTableKey" ); // [RFC2293]
        RFC3383_OID_MAPPINGS.put( "2.5.21.2", "dITContentRules" ); // [RFC2252]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.54", "dITRedirect" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.21.1", "dITStructureRules" ); // [RFC2252]
        RFC3383_OID_MAPPINGS.put( "2.5.4.54", "dmdName" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.46", "dnQualifier" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.14", "documentAuthor" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.11", "documentIdentifier" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.15", "documentLocation" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.56", "documentPublisher" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.12", "documentTitle" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.13", "documentVersion" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.25", "domainComponent" ); // [RFC2247]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.5", "drink" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.49", "dSAQuality" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "1.3.6.1.4.1.1466.101.119.4", "dynamicSubtrees" ); // [RFC2589]
        RFC3383_OID_MAPPINGS.put( "2.5.4.47", "enhancedSearchGuide" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "1.3.6.1.4.1.1466.101.119.3", "entryTtl" ); // [RFC2589]
        RFC3383_OID_MAPPINGS.put( "2.5.4.23", "facsimileTelephoneNumber" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.5", "favouriteDrink" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.43", "friendlyCountryName" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.4.44", "generationQualifier" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.42", "givenName" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.42", "GN" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.20", "homePhone" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.39", "homePostalAddress" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.20", "homeTelephone" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.9", "host" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.4.51", "houseIdentifier" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.4", "info" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.4.43", "initials" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.25", "internationaliSDNNumber" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.46", "janetMailbox" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.60", "jpegPhoto" ); // [RFC1488]
        RFC3383_OID_MAPPINGS.put( "2.5.4.2", "knowledgeInformation" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.7", "L" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "1.3.6.1.4.1.250.1.57", "labeledURI" ); // [RFC2079]
        RFC3383_OID_MAPPINGS.put( "1.3.6.1.4.1.250.3.15", "labeledURIObject" ); // [RFC2079]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.24", "lastModifiedBy" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.23", "lastModifiedTime" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "1.3.6.1.4.1.1466.101.120.16", "ldapSyntaxes" ); // [RFC2252]
        RFC3383_OID_MAPPINGS.put( "2.5.4.7", "localityName" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.3", "mail" ); // [RFC2798]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.47", "mailPreferenceOption " ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.10", "manager" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.21.4", "matchingRules" ); // [RFC2252]
        RFC3383_OID_MAPPINGS.put( "2.5.21.8", "matchingRuleUse" ); // [RFC2252]
        RFC3383_OID_MAPPINGS.put( "1.3.6.1.4.1.453.7.2.9", "mcgamTables" ); // [RFC2164]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.27", "mDRecord" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.4.31", "member" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.41", "mobile" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.41", "mobileTelephoneNumber" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.18.4", "modifiersName" ); // [RFC2252]
        RFC3383_OID_MAPPINGS.put( "2.5.18.2", "modifyTimestamp" ); // [RFC2252]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.28", "mXRecord" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.4.41", "name" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.21.7", "nameForms" ); // [RFC2252]
        RFC3383_OID_MAPPINGS.put( "1.3.6.1.4.1.1466.101.120.5", "namingContexts" ); // [RFC2252]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.29", "nSRecord" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.4.10", "O" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.0", "objectClass" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.21.6", "objectClasses" ); // [RFC2252]
        RFC3383_OID_MAPPINGS.put( "1.3.6.1.4.1.453.7.2.7", "oRAddressComponentType" ); // [RFC2164]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.45", "organizationalStatus" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.4.11", "organizationalUnitName" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.10", "organizationName" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.22", "otherMailbox" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.4.11", "OU" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.32", "owner" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.42", "pager" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.42", "pagerTelephoneNumber" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.53", "personalSignature" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.40", "personalTitle" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.7", "photo" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.4.19", "physicalDeliveryOfficeName" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.16", "postalAddress" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.17", "postalCode" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.18", "postOfficeBox" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.28", "preferredDeliveryMethod" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.29", "presentationAddress" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.48", "protocolInformation" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.16.840.1.113730.3.1.34", "ref" ); // [RFC3296]
        RFC3383_OID_MAPPINGS.put( "2.5.4.26", "registeredAddress" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.3", "RFC822Mailbox" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.4.33", "roleOccupant" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.6", "roomNumber" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.4.14", "searchGuide" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.21", "secretary" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.4.34", "seeAlso" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.5", "serialNumber" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.50", "singleLevelQuality" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.4.4", "SN" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.30", "sOARecord" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.4.8", "ST" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.8", "stateOrProvinceName" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.9", "street" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.9", "streetAddress" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.18.10", "subschemaSubentry" ); // [RFC2252]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.52", "subtreeMaximumQuality" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.51", "subtreeMinimumQuality" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.4.52", "supportedAlgorithms" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.30", "supportedApplicationContext" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "1.3.6.1.4.1.1466.101.120.13", "supportedControl" ); // [RFC2252]
        RFC3383_OID_MAPPINGS.put( "1.3.6.1.4.1.1466.101.120.7", "supportedExtension" ); // [RFC2252]
        RFC3383_OID_MAPPINGS.put( "1.3.6.1.4.1.1466.101.120.15", "supportedLDAPVersion" ); // [RFC2252]
        RFC3383_OID_MAPPINGS.put( "1.3.6.1.4.1.1466.101.120.14", "supportedSASLMechanisms" ); // [RFC2252]
        RFC3383_OID_MAPPINGS.put( "2.5.4.4", "surname" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.20", "telephoneNumber" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.22", "teletexTerminalIdentifier" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.21", "telexNumber" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.2", "textEncodedORAddress" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "1.3.6.1.4.1.453.7.2.1", "textTableKey" ); // [RFC2293]
        RFC3383_OID_MAPPINGS.put( "1.3.6.1.4.1.453.7.2.2", "textTableValue" ); // [RFC2293]
        RFC3383_OID_MAPPINGS.put( "2.5.4.12", "title" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.44", "uniqueIdentifier" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "2.5.4.50", "uniqueMember" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.36", "userCertificate" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.8", "userClass" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.1", "userId" ); // [RFC1274]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.1", "uid" ); // [RFC2253]
        RFC3383_OID_MAPPINGS.put( "2.5.4.35", "userPassword" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.24", "x121Address" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.45", "x500UniqueIdentifier" ); // [RFC2256]

        // Putting theses at the end of the LinkedHashMap so they are prefered
        RFC3383_OID_MAPPINGS.put( "2.5.4.6", "C" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.3", "CN" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "0.9.2342.19200300.100.1.25", "DC" ); // [RFC2247]
        RFC3383_OID_MAPPINGS.put( "2.5.4.42", "GN" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.7", "L" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.10", "O" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.11", "OU" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.4", "SN" ); // [RFC2256]
        RFC3383_OID_MAPPINGS.put( "2.5.4.8", "ST" ); // [RFC2256]

        for ( Map.Entry<String, String> eachRfc3383Attr : RFC3383_OID_MAPPINGS.entrySet() ) {
            ADDITIONAL_KEYWORDS.put( eachRfc3383Attr.getValue().toUpperCase(), eachRfc3383Attr.getKey() );
        }
        ADDITIONAL_KEYWORDS.put( "E", PKCS9Attribute.EMAIL_ADDRESS_OID.toString() );
    }

}
