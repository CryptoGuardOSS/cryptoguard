package tester;

public class LiveVarsClass {
    private static String xxx1 = VeryBusyClass.xxx;
    private static final AESEncryptor aes = new AESEncryptor(xxx1);
}
