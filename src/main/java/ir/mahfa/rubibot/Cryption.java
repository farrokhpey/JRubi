package ir.mahfa.rubibot;

public class Cryption {
    private final String auth;
    private final String privateKey;

    public Cryption(String auth, String privateKey) {
        this.auth = auth;
        this.privateKey = privateKey;
    }
}
