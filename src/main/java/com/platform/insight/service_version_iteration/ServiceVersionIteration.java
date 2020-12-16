package com.platform.insight.service_version_iteration;

import com.platform.insight.service.ResultBaseService;
import com.platform.insight.utils.PlatformQuerySimpleUtils;
import com.platform.insight.utils.PlatformUpdateUtils;
import com.platform.insight.utils.ServicePlatformCreateUtils;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service(ServiceVersionIteration.ID)
public class ServiceVersionIteration extends ResultBaseService implements ApiService {
    public static final String ID = "service_version_iteration";

    @Override
    public Map<String, Object> result(Map<String, Object> actual) {
        Map<String,Object> fields = (Map<String, Object>) actual.get("fields");
        Map<String, Object> record = null;
        if(actual.containsKey("filters")){//根据记录ID
            Map<String, Object> result = PlatformQuerySimpleUtils.result("model_version_now_query_by_id", (Map<String, Object>) actual.get("filters"));

            record = PlatformQuerySimpleUtils.resultOne(result);
            if(record == null){
                return  getResult(false, "记录不存在");
            }
        }else{  //根据，资源ID和文件名查找记录
            Map<String, Object> result = PlatformQuerySimpleUtils.result("model_version_now_query_by_resources_tree_id_name", fields);
            if(!(boolean)result.get("success")){
                return  getResult(false, (String) result.get("msg"));
            }
            record = PlatformQuerySimpleUtils.resultOne(result);
        }
        if(record ==null){//保存
            Map<String, Object> model_version_now_create = ServicePlatformCreateUtils.result("model_version_now_create", fields);
            if(!(boolean)model_version_now_create.get("success")){
                return  getResult(false, (String) model_version_now_create.get("msg"));
            }
            //保存版本
            fields.put("belong_model_version_id",model_version_now_create.get("primary_key"));
            ServicePlatformCreateUtils.result("model_version_history_create", fields);
        }else{//修改

            Map<String, Object> model_version_now_update = PlatformUpdateUtils.result("model_version_now_update", fields, record);
            if(!(boolean)model_version_now_update.get("success")){
                return  getResult(false, (String) model_version_now_update.get("msg"));
            }
            //如果url 变了 保存版本
            if(fields.containsKey("url")&& !StringUtils.isEmpty(fields.get("url")+"")){
                if(!fields.get("url").equals(record.get("url"))){
                    fields.put("belong_model_version_id",record.get("id"));
                    ServicePlatformCreateUtils.result("model_version_history_create", fields);
                }
            }
        }

        String msg = "保存成功";
        return  getResult(true,msg);
    }

}
