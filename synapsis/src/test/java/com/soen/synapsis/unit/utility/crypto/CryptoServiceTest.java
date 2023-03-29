package com.soen.synapsis.unit.utility.crypto;

import com.soen.synapsis.utility.crypto.CryptoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

class CryptoServiceTest {

    private CryptoService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CryptoService();
    }

    @Test
    public void encryptedStringCanBeDecrypted() {
        String originalText = "123456abcdefg";

        SecretKey secretKey = underTest.generateSymmetricKey();

        String encryptedText = underTest.encrypt(originalText, secretKey);
        String decryptedText = underTest.decrypt(encryptedText, secretKey);

        assertEquals(originalText, decryptedText);
    }

    @Test
    public void encryptedStringCanBeDecryptedFromByteArray() {
        String originalText = "123456abcdefg";

        byte[] secretKey = underTest.generateSymmetricKey().getEncoded();

        String encryptedText = underTest.encrypt(originalText, secretKey);
        String decryptedText = underTest.decrypt(encryptedText, secretKey);

        assertEquals(originalText, decryptedText);
    }
}