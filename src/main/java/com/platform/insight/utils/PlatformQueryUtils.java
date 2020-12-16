package com.platform.insight.utils;//package com.shareworx.insight.utils;
//
//import com.shareworx.insight.service.Result;
//import com.shareworx.insight.service_platform_query.ServicePlatformQuery;
//import com.shareworx.platform.util.SpringUtils;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class PlatformQueryUtils {
//    public static Map<String,Object> result(String service,Map<String,Object> search_fields){
//        Result result_service = SpringUtils.getBean(ServicePlatformQuery.ID);
//        Map<String,Object> service_operation_add = new HashMap<>();
//        service_operation_add.put("service",service);
//        service_operation_add.put("search_fields",search_fields);
//        return result_service.result(service_operation_add);
//    }
//
//    public static Map<String,Object> resultOne(Map<String,Object> result){
//        if(!(boolean)result.get("success")){
//            return null;
//        }
//
//        long count = (long) result.get("count");
//        if(count <=0){
//            return null;
//        }
//        //查询第一条记录
//
//        List<Map<String,Object>> data_list = (List<Map<String, Object>>) result.get("data");
//        return data_list.get(0);
//    }
//}
