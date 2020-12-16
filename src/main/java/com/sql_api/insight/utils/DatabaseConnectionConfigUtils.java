package com.sql_api.insight.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sql_api.insight.model.ApiModel;
import com.sql_api.insight.service.ApiService;
import com.sql_api.insight.service.Result;
import com.sql_api.insight.service_database_connection_config.ServiceDatabaseConnectionConfig;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseConnectionConfigUtils {

    public static final String CONFIG_ENABLE = "0";
    public static final String CONFIG_DISABLE = "1";


    public static Map<String,Object> result(String bean_name,Map<String,Object> fields){
        String service = "";
        ApiService api = SpringUtils.getBean(com.sql_api.insight.service.ApiService.ID);
        //获取数据库配置列表
        List<ApiModel> database_connection_config = api.queryByType("database_connection_config");
        for(ApiModel item:database_connection_config){
            String database_service_name = item.getService();
            String template = item.getTemplate();
            JSONObject jsonObject = JSON.parseObject(template);
            String database_bean = jsonObject.getString("database_bean");
            String database_bean_disable = jsonObject.getString("database_bean_disable");
            if(bean_name.equals(database_bean) && CONFIG_ENABLE.equals(database_bean_disable)){
                service = database_service_name;
                break;
            }
        }
        if(StringUtils.isEmpty(service)){
            return null;
        }
        Result result_service = SpringUtils.getBean(ServiceDatabaseConnectionConfig.ID);
        Map<String,Object> service_data = new HashMap<>();
        service_data.put("service",service);
        service_data.put("fields",fields);
        return result_service.result(service_data);
    }
    public static JSONObject ConfigOne(Map<String,Object> result){
        return (JSONObject) result.get("data");

    }
}
