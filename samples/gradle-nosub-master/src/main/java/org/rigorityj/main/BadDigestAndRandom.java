package org.rigorityj.main;


import java.security.MessageDigest;
import java.util.Random;

public class BadDigestAndRandom {

    public byte[] generateRandomBytes(long seed) {
        byte[] randomBytes = new byte[64];
        new Random(seed).nextBytes(randomBytes);
        return randomBytes;
    }

    public static byte[] getDigest(String algo, String msg) throws Exception {
        MessageDigest md = MessageDigest.getInstance(algo);
        return md.digest(msg.getBytes());
    }

}
