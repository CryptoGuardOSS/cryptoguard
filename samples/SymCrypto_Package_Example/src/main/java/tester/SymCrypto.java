package tester;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class SymCrypto {

    private static String ALGO = "AES";
    private static String ALGO_SPEC = "AES/CBC/NoPadding";
    private static String defaultKey;
    private static Cipher cipher;
    private static KeyGenerator keyGenerator;

    public static byte[] encrypt(String txt, String key) throws Exception {
        if (key == null) {
            key = new String(defaultKey);
        } else if (key.isEmpty()) {
            key = getKey();
        }

        byte[] keyBytes = DatatypeConverter.parseBase64Binary(key);
        byte[] txtBytes = txt.getBytes();
        SecretKeySpec keySpc = new SecretKeySpec(keyBytes, ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, keySpc);
        return cipher.doFinal(txtBytes);
    }

    private static String getKey() {
        return getKeyInternal();
    }

    private static String getKeyInternal(){
        return "xyzzzzzzzzzzzzzzzzzzzzzz";
    }

    public static String getKey(String src) {

        String val = System.getProperty(src);

        if (val == null) {
            val = new String("defalultval");
        }

        return val;
    }

    public static void main(String[] args) {
	String key = "testKey";	
	try {
		cipher = Cipher.getInstance(ALGO_SPEC);
		keyGenerator = KeyGenerator.getInstance(ALGO);
		String keyStr = getKey(key);

		if (keyStr == null) {
		    byte[] keyBytes = new byte[12];
		    keyBytes[0] = 0;
		    keyBytes[1] = 1;

		    defaultKey = new String(keyBytes);

		} else {
		    defaultKey = keyStr;
		}

		byte[] encryption = encrypt("Hello World",keyStr);
	} catch (Exception e) {
		System.out.println("Issue happend");
	}

	System.out.println("Hello World");

    }
}
