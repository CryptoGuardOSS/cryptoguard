package tester;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class SymCrypto {

    private String ALGO = "AES";
    private String ALGO_SPEC = "AES/CBC/NoPadding";
    private String defaultKey;
    private Cipher cipher;
    private KeyGenerator keyGenerator;

    public SymCrypto(String key, int size) throws Exception {
        cipher = Cipher.getInstance(ALGO_SPEC);
        keyGenerator = KeyGenerator.getInstance(ALGO);
        String keyStr = getKey(key);

        if (keyStr == null) {
            byte[] keyBytes = new byte[size];
            keyBytes[0] = 0;
            keyBytes[1] = 1;

            defaultKey = new String(keyBytes);

        } else {
            defaultKey = keyStr;
        }

    }

    public byte[] encrypt(String txt, String key) throws Exception {
        if (key == null) {
            key = defaultKey;
        } else if (key.isEmpty()) {
            key = new String(keyGenerator.generateKey().getEncoded());
        }

        byte[] keyBytes = DatatypeConverter.parseBase64Binary(key);
        byte[] txtBytes = txt.getBytes();
        SecretKeySpec keySpc = new SecretKeySpec(keyBytes, ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, keySpc);
        return cipher.doFinal(txtBytes);
    }

    public String getKey(String src) {
        return System.getProperty(src);
    }
}
