import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class very {
	public static void main(String[] args) throws Exception {
		Map<String, String> hm = new HashMap<String, String>();
        	hm.put("aaa", "afix");
        	hm.put("bbb", "bfix");
        	hm.put("ccc", "cfix");
        	hm.put("ddd", "dfix");

        	String key = hm.get("aaa");

        	byte[] keyBytes = key.getBytes();
        	SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		
		System.out.println("Hello World");
	}
}
