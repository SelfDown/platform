package com.sql_api.insight.utils;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class MD5Utils {

    private static MessageDigest messagedigest = null;

    public MD5Utils() {
    }

    public static String getFileMD5String(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            Throwable var2 = null;

            String var3;
            try {
                var3 = getStreamMD5String(fileInputStream);
            } catch (Throwable var13) {
                var2 = var13;
                throw var13;
            } finally {
                if (fileInputStream != null) {
                    if (var2 != null) {
                        try {
                            fileInputStream.close();
                        } catch (Throwable var12) {
                            var2.addSuppressed(var12);
                        }
                    } else {
                        fileInputStream.close();
                    }
                }

            }

            return var3;
        } catch (Exception var15) {
            var15.printStackTrace();
            return null;
        }
    }

    public static String getStreamMD5String(InputStream input) {
        try {
            byte[] buffer = new byte[8192];

            int length;
            while((length = input.read(buffer)) != -1) {
                messagedigest.update(buffer, 0, length);
            }

            return new String(Hex.encodeHex(messagedigest.digest()));
        } catch (IOException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static String getMD5String(String s) {
        return DigestUtils.md5Hex(s);
    }

    public static String getMD5String(byte[] bytes) {
        return DigestUtils.md5Hex(bytes);
    }

    public static boolean checkPassword(String password, String md5PwdStr) {
        String s = getMD5String(password);
        return s.equals(md5PwdStr);
    }

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var1) {

        }

    }
}
