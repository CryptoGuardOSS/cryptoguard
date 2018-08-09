package tester;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by krishnokoli on 11/15/17.
 */
public class PBEUsage {

    private static final String MK_CIPHER = "AES";
    private static final int MK_KeySize = 256;
    private static final int SALT_SIZE = 8;
    private static final String PBE_ALGO = "PBEWithMD5AndTripleDES";
    private static final String MD_ALGO = "MD5";

    private byte[] encryptMasterKey(String password) throws Throwable {
        Key secretKey = generateMasterKey();
        PBEKeySpec pbeKeySpec = getPBEParameterSpec(password);
        byte[] masterKeyToDB = encryptKey(secretKey.getEncoded(), pbeKeySpec);
        return masterKeyToDB;
    }

    private byte[] encryptMasterKey(String password, byte[] secretKey) throws Throwable {
        PBEKeySpec pbeKeySpec = getPBEParameterSpec(password);
        byte[] masterKeyToDB = encryptKey(secretKey, pbeKeySpec);
        return masterKeyToDB;
    }

    private Key generateMasterKey() throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance(MK_CIPHER);
        kg.init(MK_KeySize);
        return kg.generateKey();
    }

    private PBEKeySpec getPBEParameterSpec(String password) throws Throwable {
        MessageDigest md = MessageDigest.getInstance(MD_ALGO);
        byte[] saltGen = md.digest(password.getBytes());
        byte[] salt = new byte[SALT_SIZE];
        System.arraycopy(saltGen, 0, salt, 0, SALT_SIZE);
        int iteration = password.toCharArray().length + 1;
        return new PBEKeySpec(password.toCharArray(), salt, iteration);
    }
    private byte[] encryptKey(byte[] data, PBEKeySpec keyspec) throws Throwable {
        SecretKey key = getPasswordKey(keyspec);
        if(keyspec.getSalt() != null) {
            PBEParameterSpec paramSpec = new PBEParameterSpec(keyspec.getSalt(), keyspec.getIterationCount());
            Cipher c = Cipher.getInstance(key.getAlgorithm());
            c.init(Cipher.ENCRYPT_MODE, key,paramSpec);
            return c.doFinal(data);
        }
        return null;
    }
    private SecretKey getPasswordKey(PBEKeySpec keyspec) throws Throwable {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBE_ALGO);
        return factory.generateSecret(keyspec);
    }
}
