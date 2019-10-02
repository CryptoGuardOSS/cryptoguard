package org.rigorityj;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class BadAssymCrypto {

    public KeyPair generateKeyPairImplicit() throws NoSuchAlgorithmException {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        return keyPairGenerator.genKeyPair();
    }

    public KeyPair generateKeyPairExplicit(int size) throws NoSuchAlgorithmException {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(size, new SecureRandom());

        return keyPairGenerator.genKeyPair();
    }
}
