package com.platform.insight.utils;


public class ConcreteEncrypt implements IEncryptStrategy {
    private String username;
    private String password;

    public ConcreteEncrypt(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public byte[] execute() {
        if (this.username == null) {
            this.username = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }

        byte[] uBytes = this.username.getBytes();
        byte[] pBytes = this.password.getBytes();
        byte[] bytes = new byte[uBytes.length + pBytes.length];
        int i;
        if (uBytes.length >= pBytes.length) {
            for(i = 0; i < pBytes.length; ++i) {
                bytes[i * 2] = pBytes[i];
                bytes[i * 2 + 1] = uBytes[i];
            }

            if (uBytes.length - pBytes.length > 0) {
                for(i = 0; i < uBytes.length - pBytes.length; ++i) {
                    bytes[pBytes.length * 2 + i] = uBytes[pBytes.length + i];
                }
            }
        } else {
            for(i = 0; i < uBytes.length; ++i) {
                bytes[i * 2] = uBytes[i];
                bytes[i * 2 + 1] = pBytes[i];
            }

            if (pBytes.length - uBytes.length > 0) {
                for(i = 0; i < pBytes.length - uBytes.length; ++i) {
                    bytes[uBytes.length * 2 + i] = pBytes[uBytes.length + i];
                }
            }
        }

        return bytes;
    }

    public static byte[] intToByteArray1(int i) {
        byte[] result = new byte[]{(byte)(i >> 24 & 255), (byte)(i >> 16 & 255), (byte)(i >> 8 & 255), (byte)(i & 255)};
        return result;
    }


}
