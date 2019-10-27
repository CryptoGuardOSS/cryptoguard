package org.rigorityj;

import javax.net.SocketFactory;
import javax.net.ssl.*;
import java.io.OutputStream;
import java.net.URL;

public class BadHostnameVerification {

    public HttpsURLConnection connectWithBadHostnames() throws Exception {

        HostnameVerifier hostnameVerifier = new HostnameVerifier() {

            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        URL url = new URL("https://example.org/");
        HttpsURLConnection urlConnection =
                (HttpsURLConnection) url.openConnection();
        urlConnection.setHostnameVerifier(hostnameVerifier);

        return urlConnection;
    }

    public void writeToBadSocket() throws Exception {

        SocketFactory sf = SSLSocketFactory.getDefault();
        SSLSocket socket = (SSLSocket) sf.createSocket("gmail.com", 443);

        OutputStream out = socket.getOutputStream();
        out.write("Hello Bad guy! ;)".getBytes());

        socket.close();
    }


}
