package com.sql_api.insight.utils;

import com.sql_api.insight.service.Result;
import com.sql_api.insight.service_platform_create.ServicePlatformCreate;

import java.util.HashMap;
import java.util.Map;

public class ServicePlatformCreateUtils {

    public static Map<String,Object> result(String service, Map<String,Object> fields){
        Result result_service = SpringUtils.getBean(ServicePlatformCreate.ID);
        Map<String,Object> service_operation_add = new HashMap<>();
        service_operation_add.put("service",service);
        service_operation_add.put("fields",fields);
        return result_service.result(service_operation_add);
    }
}
