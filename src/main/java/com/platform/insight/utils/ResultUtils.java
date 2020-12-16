package com.platform.insight.utils;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

public class ResultUtils {
    private boolean status;
    private String msg;
    private Object data;
    public  ResultUtils(){

    }

    public ResultUtils(boolean status,String msg){
        this.status = status;
        this.msg  = msg;
    }
    public ResultUtils(boolean status,String msg,Object data){
        this(status,msg);
        this.data = data;
    }
    public static String getResult(boolean status,String msg,Object data){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",status);
        map.put("msg",msg);
        map.put("data",data);
        return JSON.toJSONString(map);
    }

    public static String getResult(boolean status,String msg,Object data,String tag){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",status);
        map.put("msg",msg);
        map.put("data",data);
        map.put("tag",tag);
        return JSON.toJSONString(map);
    }


    public static Map<String,Object> getResultMap(boolean status,String msg,Object data){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",status);
        map.put("msg",msg);
        map.put("data",data);
        return map;
    }

    public static Map<String,Object> getResultMap(boolean status,String msg){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",status);
        map.put("msg",msg);
       return map;
    }
    public static String getResult(boolean status,String msg){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",status);
        map.put("msg",msg);

        return JSON.toJSONString(map);
    }
    @Override
    public String toString() {
        return "{}";
    }
}
