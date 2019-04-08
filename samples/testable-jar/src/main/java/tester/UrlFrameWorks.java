package tester;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlFrameWorks {

    private static String apiBaseUrl = "http://abc.com";

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(apiBaseUrl);

    public HttpURLConnection createURL(String tr) throws IOException {

        URL url = new URL("http://publicobject.com/helloworld.txt");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        return connection;
    }

    private Request createUrlUsingOkHttp1(String tr) {

        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build();

        return request;

    }

    private void createUrlUsingOkHttp2() {

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://ajax.googleapis.com/ajax/services/search/images").newBuilder();
        urlBuilder.addQueryParameter("v", "1.0");
        urlBuilder.addQueryParameter("q", "android");
        urlBuilder.addQueryParameter("rsz", "8");
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();
    }

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    public static void changeApiBaseUrl(String newApiBaseUrl) {
        apiBaseUrl = newApiBaseUrl;

        builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(apiBaseUrl);
    }
}
