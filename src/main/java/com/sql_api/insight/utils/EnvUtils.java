package com.sql_api.insight.utils;


import org.springframework.core.env.Environment;

public class EnvUtils {
    public static String getProperty(String name){
        Environment bean = SpringUtils.getBean(Environment.class);
        return bean.getProperty(name);
    }
    public static boolean isDebugger(){
        String debugger = getProperty("debugger");
        if("true".equals(debugger)){
            return true;
        }
        return false;
    }
}
