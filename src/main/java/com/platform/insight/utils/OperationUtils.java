package com.platform.insight.utils;

import com.platform.insight.service.Result;
import com.platform.insight.service_platform_create.ServicePlatformCreate;


import java.util.HashMap;
import java.util.Map;

public class OperationUtils {

    public static void addOperationLog(String operation_name,String params,String msg){
        Result result_service = SpringUtils.getBean(ServicePlatformCreate.ID);
        Map<String,Object> service_operation_add = new HashMap<>();
        service_operation_add.put("service","service_operation_add");
        Map<String,Object> fields = new HashMap<>();
        fields.put("operation_name",operation_name);
        fields.put("params",params);
        fields.put("msg",msg);
        service_operation_add.put("fields",fields);
        try{
            Map<String, Object> result = result_service.result(service_operation_add);
            if(EnvUtils.isDebugger()){
                System.out.println("日志添加成功");
            }
        }catch(Exception ex){

        }
    }
}
