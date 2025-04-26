package ro.chatapp.endtoendencryption;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class Encryption {

    private PublicKey publicKey;
    private PrivateKey privateKey;

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            this.publicKey = keyPair.getPublic();
            this.privateKey = keyPair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String content, String publicKey) {
        try {
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey.getBytes());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey1 = keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey1);

        return Base64.getEncoder().encodeToString(cipher.doFinal(content.getBytes("UTF-8")));
    }
    catch (Exception e) {
        e.printStackTrace();
        return null;}
    }

    public String decrypt(String content, String privateKey) {
        try{
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey.getBytes());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey1 = keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey1);
        return new String(cipher.doFinal(Base64.getDecoder().decode(content)), "UTF-8");
    }
    catch (Exception e) {
        e.printStackTrace();
        return null;}
    }


    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }
}
