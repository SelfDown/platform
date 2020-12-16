package com.platform.insight.utils;


//import org.apache.log4j.Logger;

public class CustomEncryptStrategy implements IEncryptStrategy {
//    private Logger log = Logger.getLogger(CustomEncryptStrategy.class);
    private IEncryptStrategy instance;

    public CustomEncryptStrategy(IEncryptStrategy instance) {
        this.instance = instance;
    }

    public byte[] execute() {
        byte[] bytes = this.instance.execute();

        int i;
        for(i = 0; i < bytes.length; ++i) {
            byte tmp = bytes[i];
            bytes[i] = bytes[bytes.length - 1 - i];
            bytes[bytes.length - 1 - i] = tmp;
            if (i >= bytes.length / 2) {
                break;
            }
        }

        try {
            for(i = 0; i < bytes.length; ++i) {
                String str = Byte.toString(bytes[i]);
                Integer num = Integer.valueOf(str);
                if (num >= 33 && num <= 126) {
                    num = i % 2 == 0 ? num + 4 : num + 5;
                    if (num > 126) {
                        num = num - 126 + 33;
                    }

                    bytes[i] = Byte.valueOf(num.toString());
                }
            }

            return bytes;
        } catch (Exception var5) {
//            this.log.error(var5.getMessage(), var5);
            return bytes;
        }
    }
}
