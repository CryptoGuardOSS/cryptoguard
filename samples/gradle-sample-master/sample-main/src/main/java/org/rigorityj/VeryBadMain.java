package org.rigorityj;

import javax.xml.bind.DatatypeConverter;
import java.security.KeyPair;

public class VeryBadMain {

    public static void main(String[] args) throws Exception {

        BadAssymCrypto badCrypto = new BadAssymCrypto();
        KeyPair kp = badCrypto.generateKeyPairExplicit(512);

        System.out.println(kp.getPrivate().getEncoded());

        BadDigestAndRandom badDigestAndRandom = new BadDigestAndRandom();

        System.out.println(DatatypeConverter.printHexBinary(badDigestAndRandom.getDigest("MD5", "Hello World!")));
        System.out.println(DatatypeConverter.printHexBinary(badDigestAndRandom.getDigest("SHA1", "Hello World!")));
        System.out.println(DatatypeConverter.printHexBinary(badDigestAndRandom.getDigest("SHA-512", "Hello World!")));

        String key = "my+secret+key+lol";
        String initVector = "RandomInitVector";
        BadSymCrypto crypto = new BadSymCrypto("AES/CBC/PKCS5PADDING");

        String decrypted = crypto.decrypt(key, initVector, "154asf15as4d5as4dasdfasf1sa5d");
        System.out.println(decrypted);

        String encrypted = PasswordUtils.encryptPassword("mypass,PBEWITHHMACSHA1ANDAES_128,my-sensitive-key,f77aLYLo,2000");
        System.out.println(encrypted);
        System.out.println(PasswordUtils.encryptPassword(encrypted));
    }
}
