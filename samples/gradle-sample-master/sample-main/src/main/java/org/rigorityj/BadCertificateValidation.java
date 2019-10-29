package org.rigorityj;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class BadCertificateValidation {

    public SSLSocketFactory getSocketFactoryWithBadSelfsignedCertValidation() throws Exception {
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

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{ignoreValidationTM}, new SecureRandom());
        return sslContext.getSocketFactory();
    }

    public SSLSocketFactory getSocketFactoryWithTrustAllCertValidation() throws Exception {
        TrustManager ignoreValidationTM = new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // Do Nothing
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
                // Do nothing
            }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{ignoreValidationTM}, new SecureRandom());
        return sslContext.getSocketFactory();
    }
}
