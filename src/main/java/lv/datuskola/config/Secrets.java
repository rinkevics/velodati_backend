package lv.datuskola.config;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

public class Secrets {

    public static String SALT = "yrvEWaLe";

    public static Properties readAndDecrypt(String password) throws IOException, GeneralSecurityException {
        var key = getSecretKey(password);
        var decryptedProperties = getProperties(key);
        return decryptedProperties;
    }

    private static SecretKeySpec getSecretKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = SALT.getBytes();
        int iterationCount = 40000;
        int keyLength = 256;
        return createSecretKey(password.toCharArray(),
                salt, iterationCount, keyLength);
    }

    private static Properties getProperties(SecretKeySpec key) throws IOException, GeneralSecurityException {
        var decryptedProperties = new Properties();
        var prop = new Properties();
        InputStream is = new FileInputStream(new File("C:\\work\\secrets.properties"));
        prop.load(is);

        for(var propertyName : prop.keySet()) {
            String encrypted = (String) prop.get(propertyName);
            String decrypted = decrypt(encrypted, key);
            decryptedProperties.put(propertyName, decrypted);
        }
        return decryptedProperties;
    }

    private static SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }

    private static String decrypt(String string, SecretKeySpec key) throws GeneralSecurityException {
        String iv = string.split(":")[0];
        String property = string.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
        return new String(pbeCipher.doFinal(base64Decode(property)), StandardCharsets.UTF_8);
    }

    private static byte[] base64Decode(String property) {
        return Base64.getDecoder().decode(property);
    }
}