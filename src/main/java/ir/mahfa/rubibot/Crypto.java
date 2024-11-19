package ir.mahfa.rubibot;

class Crypto {
    String auth = "auth_value";

    public String encrypt(String data) {
        return "encrypted_" + data;
    }

    public String changeAuthType(String auth) {
        return "new_auth_" + auth;
    }

    public String makeSignFromData(String encryptedData) {
        return null;
    }

    public String decrypt(Object result) {
        return null;
    }
}