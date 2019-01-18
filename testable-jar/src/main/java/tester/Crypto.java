package tester;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.SocketFactory;
import javax.net.ssl.*;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Random;

/**
 * Created by krishnokoli on 7/2/17.
 */
public class Crypto {

    private String AES_ECB = "AES/ECB/PKCS5PADDING";
    public Cipher cipher;

    public void Crypto(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec1 = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            cipher = Cipher.getInstance(AES_ECB);

            long currentTime = System.nanoTime();
            String currentTimeStr = String.valueOf(currentTime);

            SecretKeySpec secretKeySpec = new SecretKeySpec(currentTimeStr.getBytes(), "AES");

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);

            cipher.init(Cipher.DECRYPT_MODE, skeySpec1, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string: "
                    + DatatypeConverter.printBase64Binary(encrypted));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String decrypt(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(DatatypeConverter.parseBase64Binary(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public TrustManager getTrustManager() {

        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                HostnameVerifier defaultHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();

                return defaultHostnameVerifier.verify("*.amap.com", sslSession);
            }
        };

        HostnameVerifier hostnameVerifier1 = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };

        System.out.println(hostnameVerifier);

        TrustManager ignoreValidationTM = new X509TrustManager() {

            private X509TrustManager trustManager;

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                trustManager.checkClientTrusted(chain, authType);
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
                if (x509CertificateArr == null || x509CertificateArr.length != 1) {
                    this.trustManager.checkServerTrusted(x509CertificateArr, str);
                } else {
                    x509CertificateArr[0].checkValidity();
                }
            }
        };

        return ignoreValidationTM;
    }

    public KeyPair generateKeyPair() throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException {

        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.setKeyEntry(null, null, "mypass".toCharArray(), null);

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024, new SecureRandom());

        return keyPairGenerator.genKeyPair();

    }

    public KeyPair generateKeyPairDefaultKeySize() throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException {

        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.setKeyEntry(null, null, "mypass".toCharArray(), null);

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        return keyPairGenerator.genKeyPair();

    }

    public void geCustomSocketFactory() throws IOException {

        SocketFactory sf = SSLSocketFactory.getDefault();
        SSLSocket socket = (SSLSocket) sf.createSocket("gmail.com", 443);
        HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
        SSLSession s = socket.getSession();


        // Verify that the certicate hostname is for mail.google.com
        // This is due to lack of SNI support in the current SSLSocket.
        if (!hv.verify("mail.google.com", s)) {
            throw new SSLHandshakeException("Expected mail.google.com, not found " + s.getPeerPrincipal());
        }

        socket.close();
    }

    public byte[] randomNumberGeneration(long seed) {

        byte[] randomBytes = new byte[64];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(seed);
        secureRandom.nextBytes(randomBytes);

        return randomBytes;
    }

    public void main(String[] args) throws NoSuchAlgorithmException {
        String key = "Bar12345Bar12345"; // 128 bit key
        String initVector = "RandomInitVector"; // 16 bytes IV

        MessageDigest md = MessageDigest.getInstance("SHA1");
        MessageDigest md2 = MessageDigest.getInstance("SHA");

        TrustManager trustManager = getTrustManager();
        System.out.println(trustManager);

        System.out.println(md.toString() + md2);

//        decrypt(key, initVector, "abcd");

        randomNumberGeneration(1000L);
//
        while (args.length > 0) {
            if (args[0].equals("g")) {
                decrypt(key, initVector, "abcd");
            }
        }
    }
}
