package tester;

public class PassEncryptor {

    private SymCrypto symCrypto;

    private String passKey = "pass.key";
    private static final String prefix = "{key}";

    public PassEncryptor() throws Exception {
        symCrypto = new SymCrypto(passKey, prefix.length());
    }

    byte[] encPass(String pass, String src) throws Exception {
        return symCrypto.encrypt(pass, "hardcoded");
    }
}
