package com.platform.insight.utils;



import java.sql.Connection;
import java.sql.DriverManager;

public class SqliteUtils {
    static Connection conn = null;
     public static  Connection getConn(){

         if (conn !=null){
             return conn;
         }
         try {
             Class.forName("org.sqlite.JDBC");
             String path = EnvUtils.getProperty("api.database.path");
             conn = DriverManager.getConnection("jdbc:sqlite:"+path);
         } catch ( Exception e ) {
             System.err.println( e.getClass().getName() + ": " + e.getMessage() );

         }
         return conn;
     }
}
