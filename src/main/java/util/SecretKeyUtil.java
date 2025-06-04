package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SecretKeyUtil {

    public static SecretKey getSecretKey(String algorithm) {
        SecretKey secretKey = null;
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
            secretKey = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return secretKey;
    }

    public static String keyToString(SecretKey secretKey) {
        /* Get key in encoding format */
        byte encoded[] = secretKey.getEncoded();

        String encodedKey = Base64.getEncoder().encodeToString(encoded);

        return encodedKey;
    }

    public static void saveSecretKey(SecretKey secretKey, String fileName) {
        byte[] keyBytes = secretKey.getEncoded();
        File keyFile = new File(fileName);
        FileOutputStream fOutStream = null;
        try {
            fOutStream = new FileOutputStream(keyFile);
            fOutStream.write(keyBytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fOutStream != null) {
                try {
                    fOutStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}