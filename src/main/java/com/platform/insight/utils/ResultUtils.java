package com.platform.insight.utils;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

public class ResultUtils {
    private boolean status;
    private String msg;
    private Object data;

    public ResultUtils() {

    }

    public static boolean isSuccess(Map<String, Object> result) {
        if (result.containsKey(Const.SUCCESS) && (boolean) result.get(Const.SUCCESS)) {
            return true;
        }
        return false;

    }

    public ResultUtils(boolean status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ResultUtils(boolean status, String msg, Object data) {
        this(status, msg);
        this.data = data;
    }

    public static String getResult(boolean status, String msg, Object data) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Const.SUCCESS, status);
        map.put(Const.MSG, msg);
        map.put(Const.DATA, data);
        return JSON.toJSONString(map);
    }


    public static String getMsg(Map<String, Object> result) {
        if (result.containsKey(Const.MSG) ) {
            return (String) result.get(Const.MSG);
        }

        return null;
    }

    public static String getResult(boolean status, String msg, Object data, String tag) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Const.SUCCESS, status);
        map.put(Const.MSG, msg);
        map.put(Const.DATA, data);
        map.put(Const.TAG, tag);
        return JSON.toJSONString(map);
    }


    public static Map<String, Object> getResultMap(boolean status, String msg, Object data) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Const.SUCCESS, status);
        map.put(Const.MSG, msg);
        map.put(Const.DATA, data);
        return map;
    }

    public static Map<String, Object> getResultMap(boolean status, String msg) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Const.SUCCESS, status);
        map.put(Const.MSG, msg);
        return map;
    }

    public static String getResult(boolean status, String msg) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Const.SUCCESS, status);
        map.put(Const.MSG, msg);

        return JSON.toJSONString(map);
    }

    @Override
    public String toString() {
        return "{}";
    }
}
