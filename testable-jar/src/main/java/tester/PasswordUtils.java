package tester;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.Map;

/**
 * Created by krishnokoli on 11/27/17.
 */
public class PasswordUtils {

    private final String CRYPT_ALGO;
    private String password;
    private final char[] ENCRYPT_KEY;
    private final byte[] SALT;
    private final int ITERATION_COUNT;
    private final char[] encryptKey;
    private final byte[] salt;
    private static final String LEN_SEPARATOR_STR = ":";

    public static final String DEFAULT_CRYPT_ALGO = "PBEWithMD5AndDES";
    public static final String DEFAULT_ENCRYPT_KEY = "tzL1AKl5uc4NKYaoQ4P3WLGIBFPXWPWdu1fRm9004jtQiV";
    public static final String DEFAULT_SALT = "f77aLYLo";
    public static final int DEFAULT_ITERATION_COUNT = 17;

    public static String encryptPassword(String aPassword) throws IOException {
        return new PasswordUtils(aPassword).encrypt();
    }

    private String encrypt() throws IOException {
        String ret = null;
        String strToEncrypt = null;
        if (password == null) {
            strToEncrypt = "";
        } else {
            strToEncrypt = password.length() + LEN_SEPARATOR_STR + password;
        }
        try {
            Cipher engine = Cipher.getInstance(CRYPT_ALGO);
            PBEKeySpec keySpec = new PBEKeySpec(encryptKey);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(CRYPT_ALGO);
            SecretKey key = skf.generateSecret(keySpec);
            engine.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(salt, ITERATION_COUNT));
            byte[] encryptedStr = engine.doFinal(strToEncrypt.getBytes());
            ret = new String(DatatypeConverter.printBase64Binary(encryptedStr));
        } catch (Throwable t) {
            throw new IOException("Unable to encrypt password due to error", t);
        }
        return ret;
    }

    PasswordUtils(String aPassword) {
        String[] crypt_algo_array = null;
        int count = 0;
        if (aPassword != null && aPassword.contains(",")) {

            count = aPassword.split(",").length;

            crypt_algo_array = aPassword.split(",");
        }
        if (crypt_algo_array != null && crypt_algo_array.length > 1) {
            CRYPT_ALGO = crypt_algo_array[0];
            ENCRYPT_KEY = crypt_algo_array[1].toCharArray();
            SALT = crypt_algo_array[2].getBytes();
            ITERATION_COUNT = Integer.parseInt(crypt_algo_array[3]);
            password = crypt_algo_array[4];
            if (count > 4) {
                for (int i = 5; i <= count; i++) {
                    password = password + "," + crypt_algo_array[i];
                }
            }
        } else {
            CRYPT_ALGO = DEFAULT_CRYPT_ALGO;
            ENCRYPT_KEY = DEFAULT_ENCRYPT_KEY.toCharArray();
            SALT = DEFAULT_SALT.getBytes();
            ITERATION_COUNT = DEFAULT_ITERATION_COUNT;
            password = aPassword;
        }
        Map<String, String> env = System.getenv();
        String encryptKeyStr = env.get("ENCRYPT_KEY");
        if (encryptKeyStr == null) {
            encryptKey = ENCRYPT_KEY;
        } else {
            encryptKey = encryptKeyStr.toCharArray();
        }
        String saltStr = env.get("ENCRYPT_SALT");
        if (saltStr == null) {
            salt = SALT;
        } else {
            salt = saltStr.getBytes();
        }
    }

    public static String decryptPassword(String aPassword) throws IOException {
        return new PasswordUtils(aPassword)
                .decrypt();
    }

    private String decrypt() throws IOException {
        String ret = null;
        try {
            byte[] decodedPassword = DatatypeConverter.parseBase64Binary(password);
            Cipher engine = Cipher.getInstance(CRYPT_ALGO);
            PBEKeySpec keySpec = new PBEKeySpec(encryptKey);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(CRYPT_ALGO);
            SecretKey key = skf.generateSecret(keySpec);
            engine.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(salt, ITERATION_COUNT));
            String decrypted = new String(engine.doFinal(decodedPassword));
            int foundAt = decrypted.indexOf(LEN_SEPARATOR_STR);
            if (foundAt > -1) {
                if (decrypted.length() > foundAt) {
                    ret = decrypted.substring(foundAt + 1);
                } else {
                    ret = "";
                }
            } else {
                ret = null;
            }
        } catch (Throwable t) {
            throw new IOException("Unable to decrypt password due to error", t);
        }
        return ret;
    }
}
