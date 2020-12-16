package com.platform.insight.utils;

import java.util.HashMap;
import java.util.Map;

public class ApiCacheUtils {
    //服务结果数据缓存
    public static Map<String,Object> cacheServiceMap = new HashMap<>();
    //模板数据缓存
    public static Map<String,Object> cacheApiTemplateMap = new HashMap<>();

    /**
     * 将查询出来的结果缓存，用于页面配置
    * */
    public static Object getServiceResult(String service){
        return cacheServiceMap.get(service);
    }

    /**
     * 设置缓存
     * @param service
     * @param result
     */
    public static void setServiceMap(String service,Object result){
        cacheServiceMap.put(service,result);
    }

    /**
     * 将查询出来的api进行缓存
     * @param service
     * @return
     */
    public static Object getApiTemplateMap(String service){
        return cacheApiTemplateMap.get(service);
    }

    public static void setApiTemplateMap(String service,Object template){
        cacheApiTemplateMap.put(service,template);
    }

    //清空缓存
    public static void clear(){
        cacheServiceMap.clear();
        cacheApiTemplateMap.clear();
    }
}
