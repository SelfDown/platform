package com.platform.insight.utils;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5EncryptStrategy implements IEncryptStrategy {

    private IEncryptStrategy instance;

    public MD5EncryptStrategy(IEncryptStrategy instance) {
        this.instance = instance;
    }

    public byte[] execute() {
        byte[] bytes = this.instance.execute();


        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            return md.digest(bytes);
        } catch (NoSuchAlgorithmException var3) {

            return bytes;
        }
    }
}
