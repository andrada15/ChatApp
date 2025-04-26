package ro.chatapp.endtoendencryption;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class RSAEncryptionUnitTest {

    private final Encryption encryption = new Encryption();
    @Test
    void generateKeyPairTest() throws Exception {
        encryption.generateKeyPair();

        assertNotNull(encryption.getPublicKey());
        assertNotNull(encryption.getPrivateKey());

    }

    @Test
    void encryptAndDecryptTest() throws Exception {
        encryption.generateKeyPair();

        String message = "Hello World";
        String publicKey = Base64.getEncoder().encodeToString(encryption.getPublicKey().getEncoded());
        String privateKey = Base64.getEncoder().encodeToString(encryption.getPrivateKey().getEncoded());

        String encryptedMessage = encryption.encrypt(message, publicKey);
        assertNotNull(encryptedMessage);
        assertNotEquals(message, encryptedMessage);

        String decryptedMessage = encryption.decrypt(encryptedMessage, privateKey);
        assertEquals(message, decryptedMessage);
    }

    @Test
    void invalidPublicKeyTest() {
        String invalidPublicKey = "invalidPublicKey";
        assertThrows(Exception.class, () -> encryption.encrypt("invalidPublicKey", invalidPublicKey));
    }


    @Test
    void invalidPrivateKeyTest() {
        String invalidPrivateKey = "invalidPrivateKey";
        assertThrows(Exception.class, () -> encryption.encrypt("invalidPrivateKey", invalidPrivateKey));
    }
}