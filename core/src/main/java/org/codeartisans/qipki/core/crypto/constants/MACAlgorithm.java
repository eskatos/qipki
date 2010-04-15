package org.codeartisans.qipki.core.crypto.constants;

public interface MACAlgorithm
{

    /**
     * The HMAC-MD5 keyed-hashing algorithm as defined in RFC 2104: "HMAC: Keyed-Hashing for Message Authentication" (February 1997).
     */
    String HmacMD5 = "HmacMD5";
    /**
     * The HMAC-SHA1 keyed-hashing algorithm as defined in RFC 2104: "HMAC: Keyed-Hashing for Message Authentication" (February 1997).
     */
    String HmacSHA1 = "HmacSHA1";
    /**
     * The HmacSHA256 algorithm as defined in RFC 2104 "HMAC: Keyed-Hashing for Message Authentication" (February 1997) with SHA-256 as the message digest algorithm.
     */
    String HmacSHA256 = "HmacSHA256";
    /**
     * The HmacSHA384 algorithm as defined in RFC 2104 "HMAC: Keyed-Hashing for Message Authentication" (February 1997) with SHA-384 as the message digest algorithm.
     */
    String HmacSHA384 = "HmacSHA384";
    /**
     * The HmacSHA512 algorithm as defined in RFC 2104 "HMAC: Keyed-Hashing for Message Authentication" (February 1997) with SHA-512 as the message digest algorithm.
     */
    String HmacSHA512 = "HmacSHA512";
}
