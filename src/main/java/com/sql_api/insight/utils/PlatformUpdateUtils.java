package com.sql_api.insight.utils;

import com.sql_api.insight.service.Result;
import com.sql_api.insight.service_platform_update.ServicePlatformUpdate;


import java.util.HashMap;
import java.util.Map;

public class PlatformUpdateUtils {

    public static Map<String,Object> result(String service, Map<String,Object> fields,Map<String,Object> filters){
        Result result_service = SpringUtils.getBean(ServicePlatformUpdate.ID);
        Map<String,Object> service_operation_add = new HashMap<>();
        service_operation_add.put("service",service);
        service_operation_add.put("fields",fields);
        service_operation_add.put("filters",filters);
        return result_service.result(service_operation_add);
    }
}
