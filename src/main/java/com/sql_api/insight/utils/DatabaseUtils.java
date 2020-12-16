package com.sql_api.insight.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sql_api.insight.model.ApiModel;
import com.sql_api.insight.service.ApiService;
import com.sql_api.insight.service_database_connection_config.ServiceDatabaseConnectionConfig;
import org.springframework.util.StringUtils;

import java.util.List;

public class DatabaseUtils {
    static String database_type = null;
    static String class_name = null;
    public static boolean isMySQL(){
       return getDatabaseType().equals("mysql");
//        return  true;
    }

    /**
     * 获取数据库类型，同时也是数据库工厂实例名称，
     * 例如 org.sqlite.JDBC ，工厂实例SQLiteDatabaseFactory.ID
     * @return
     */
    public static String getDatabaseType(){
       if(!StringUtils.isEmpty(database_type)){
           return database_type;
       }

        JSONObject model = getApiJsonModel();
        if(model == null){
            return null;
        }
        database_type = model.getString("database_type").toLowerCase();
        return database_type;
    }

    private static JSONObject getApiJsonModel(){
        ApiService bean = SpringUtils.getBean(ApiService.ID);
        List<ApiModel> apiModels = bean.queryByType(ServiceDatabaseConnectionConfig.ID.replace("service_",""));
        JSONObject model = null;
        for (ApiModel item :apiModels){
            String template = item.getTemplate();
            JSONObject templateObj = JSON.parseObject(template);
            String database_bean_disable = templateObj.getString("database_bean_disable");
            if("0".equals(database_bean_disable)){
                model = templateObj;
                break;
            }
        }

      return  model   ;

    }

    public static String getDatabaseClassName(){
        if(!StringUtils.isEmpty(class_name)){
            return class_name;
        }

        JSONObject model = getApiJsonModel();
        if(model == null){
            return null;
        }
        class_name = model.getString("class_name");
        return class_name;
    }
    public static boolean isOracle(){
        return getDatabaseType().equals("oracle");
//        return false;
    }
}
