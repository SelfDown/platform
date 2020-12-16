package com.platform.insight.utils;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShaEncryptStrategy implements IEncryptStrategy {

    private IEncryptStrategy instance;

    public ShaEncryptStrategy(IEncryptStrategy instance) {
        this.instance = instance;
    }

    public byte[] execute() {
        byte[] bytes = this.instance.execute();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            return md.digest(bytes);
        } catch (NoSuchAlgorithmException var3) {

            return bytes;
        }
    }
}
