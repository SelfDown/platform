package com.platform.insight.utils;

import org.springframework.util.StringUtils;

public class EncryptContext {
    private String salt;
    private String password;

    public EncryptContext(String salt, String password) {
        this.salt = salt;
        this.password = password;
    }

    public String getEncryptPassword() {
        return this.getEncryptPassword(this.salt, this.password);
    }

    public String getEncryptPassword(String salt, String password) {
        IEncryptStrategy en1 = new ConcreteEncrypt(salt, password);
        IEncryptStrategy en2 = new CustomEncryptStrategy(en1);
        IEncryptStrategy en3 = new MD5EncryptStrategy(en2);
        IEncryptStrategy en4 = new ShaEncryptStrategy(en3);
        byte[] bs = en4.execute();
        return StringJoinUtils.byteArrayToHexString(bs);
    }

    public String getSalt() {
        return this.salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean checkPassword(String password) {
        return StringUtils.isEmpty(password) ? false : password.equals(this.getEncryptPassword());
    }
}