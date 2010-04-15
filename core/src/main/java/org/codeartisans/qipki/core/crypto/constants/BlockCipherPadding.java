package org.codeartisans.qipki.core.crypto.constants;

public interface BlockCipherPadding
{

    // Sun JCE
    String NoPadding = "NoPadding";
    String PKCS5Padding = "PKCS5Padding";
    String SSL3Padding = "SSL3Padding";
    String ISO10126Padding = "ISO10126Padding";
    // Bouncy Castle
    String PKCS7Padding = "PKCS7Padding";
    String ISO10126d2Padding = "ISO10126d2Padding";
    String ISO7816d4Padding = "ISO7816d4Padding";
    String X932Padding = "X932Padding";
    String ZeroBytePadding = "ZeroBytePadding";
    String TBCPadding = "TBCPadding";
}
