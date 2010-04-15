package org.codeartisans.qipki.core.crypto.constants;

public interface SignatureAlgorithm
{

    // DSA currently just supports SHA-1.
    String SHA1withDSA = "SHA1withDSA";
    // ECDSA is support with both the SHA-1 and SHA-2 family of digest algorithms.
    String SHA1withECDSA = "SHA1withECDSA";
    String SHA224withECDSA = "SHA224withECDSA";
    String SHA256withECDSA = "SHA256withECDSA";
    String SHA384withECDSA = "SHA384withECDSA";
    String SHA512withECDSA = "SHA512withECDSA";
    // A variety of digests can be used to sign using the RSA algorithm
    String MD2withRSA = "MD2withRSA";
    String MD5withRSA = "MD5withRSA";
    String SHA1withRSA = "SHA1withRSA";
    String SHA224withRSA = "SHA224withRSA";
    String SHA256withRSA = "SHA256withRSA";
    String SHA384withRSA = "SHA384withRSA";
    String SHA512withRSA = "SHA512withRSA";
    String RIPEMD160withRSA = "RIPEMD160withRSA";
    String RIPEMD128withRSA = "RIPEMD128withRSA";
    String RIPEMD256withRSA = "RIPEMD256withRSA";
}
