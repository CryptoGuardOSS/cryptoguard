import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class very {
	public static void main(String[] args) throws Exception {
		
		createURL("TestObject.git");

		System.out.println("Hello World");
	}

	public static HttpURLConnection createURL(String tr) throws IOException {
        	URL url = new URL("http://publicobject.com/" + tr);
        	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        	connection.setRequestMethod("POST");
        	return connection;
    	}
}
