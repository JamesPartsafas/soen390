package com.soen.synapsis.utility.crypto;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Handles all logic relating to cryptography and key pair generation
 */
@Service
public class CryptoService {

    private final int KEY_LENGTH = 128;
    private final String ALGORITHM = "AES";

    /**
     * Generates a new symmetric key for use
     * @return A key to encrypt and decrypt content
     */
    public SecretKey generateSymmetricKey() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
            generator.init(KEY_LENGTH);

            return generator.generateKey();
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Encrypt plain text using a specified key
     * @param plainText The plain text to encrypt
     * @param secretKey The key that will do the encrypting
     * @return The encrypted version of plainText
     */
    public String encrypt(String plainText, byte[] secretKey) {
        SecretKey originalKey = new SecretKeySpec(secretKey, 0, secretKey.length, ALGORITHM);

        return encrypt(plainText, originalKey);
    }

    /**
     * Encrypt plain text using a specified key
     * @param plainText The plain text to encrypt
     * @param secretKey The key that will do the encrypting
     * @return The encrypted version of plainText
     */
    public String encrypt(String plainText, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

            return Base64.getEncoder().encodeToString(encryptedBytes);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Decrypt text using a specified key
     * @param encryptedText The text to decrypt
     * @param secretKey The key that will do the decrypting
     * @return The decrypted version of encryptedText
     */
    public String decrypt(String encryptedText, byte[] secretKey) {
        SecretKey originalKey = new SecretKeySpec(secretKey, 0, secretKey.length, ALGORITHM);

        return decrypt(encryptedText, originalKey);
    }

    /**
     * Decrypt text using a specified key
     * @param encryptedText The text to decrypt
     * @param secretKey The key that will do the decrypting
     * @return The decrypted version of encryptedText
     */
    public String decrypt(String encryptedText, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(encryptedText));

            return new String(plainText);
        }
        catch (Exception e) {
            return null;
        }
    }
}
