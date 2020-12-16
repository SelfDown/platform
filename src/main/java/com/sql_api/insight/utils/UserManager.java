package com.sql_api.insight.utils;




import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;


import java.util.Map;

public class UserManager {
    public static String getCurrentPkUser(){
        Map<String, Object> userInfo = getUserInfo();
        if(userInfo != null){
            return (String) getUserInfo().get("pk_user");
        }else{
            return null;
        }

    }


    public static Map<String,Object> getUserInfo(){
        //
        Subject subject = SecurityUtils.getSubject();
        Map<String,Object> principal = (Map<String, Object>) subject.getPrincipal();
        if(principal != null){
            principal.remove("password");
        }

        return principal;


    }
}
