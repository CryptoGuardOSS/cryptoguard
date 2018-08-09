package tester;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

/**
 * Created by sazzad on 8/8/17.
 */
public class AESEncryptor extends AESEncryptorBase {

    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 128;
    private Cipher ecipher;
    private Cipher dcipher;
    private SecretKey secret;
    private byte[] salt = null;
    private char[] passPhrase = null;

    public AESEncryptor(String passPhrase) {

        super(passPhrase);

        try {
            this.passPhrase = passPhrase.toCharArray();
            salt = new byte[8];
            SecureRandom rnd = new SecureRandom();
            rnd.nextBytes(salt);

            SecretKey tmp = getKeyFromPassword(passPhrase);
            secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            String algorithm = "AES/CBC/PKCS5Padding";

            ecipher = Cipher.getInstance(algorithm);
            ecipher.init(Cipher.ENCRYPT_MODE, secret);

            dcipher = Cipher.getInstance(algorithm);
            byte[] iv = ecipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
            dcipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidParameterSpecException | InvalidKeyException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public SecretKey getKeyFromPassword(String passPhrase) {
        return getKeyFromPassword(passPhrase, salt);
    }

    public SecretKey getKeyFromPassword(String passPhrase, byte[] salt) {
        SecretKeyFactory factory;
        SecretKey key = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
            key = factory.generateSecret(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return key;
    }
}
