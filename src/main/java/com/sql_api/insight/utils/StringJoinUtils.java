package com.sql_api.insight.utils;


import java.util.List;

public class StringJoinUtils {
    public static String join( Object[] o , String flag ){
        StringBuffer str_buff = new StringBuffer();

        for(int i=0 , len=o.length ; i<len ; i++){
            str_buff.append( String.valueOf( o[i] ) );
            if(i<len-1)str_buff.append( flag );
        }

        return str_buff.toString();
    }

    public static String byteArrayToHexString(byte[] bytearray) {
        String strDigest = "";

        for(int i = 0; i < bytearray.length; ++i) {
            strDigest = strDigest + byteToHexString(bytearray[i]);
        }

        return strDigest;
    }

    public static String byteToHexString(byte ib) {
        char[] Digit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] ob = new char[]{Digit[ib >>> 4 & 15], Digit[ib & 15]};
        String s = new String(ob);
        return s;
    }

    public static String join(List o , String flag ){
        StringBuffer str_buff = new StringBuffer();

        for(int i=0 , len=o.size() ; i<len ; i++){
            str_buff.append( String.valueOf( o.get(i) ) );
            if(i<len-1)str_buff.append( flag );
        }

        return str_buff.toString();
    }

}
