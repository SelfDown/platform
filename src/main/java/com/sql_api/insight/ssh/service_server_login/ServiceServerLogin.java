package com.sql_api.insight.ssh.service_server_login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.JSchException;
import com.sql_api.insight.model.ApiModel;
import com.sql_api.insight.service.ResultBaseService;
import com.sql_api.insight.utils.SSHUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service(ServiceServerLogin.ID)
public class ServiceServerLogin extends ResultBaseService {
    public static final String ID ="service_server_login";
    @Override
    public Map<String, Object> result(Map<String, Object> actual) {
        String service = (String) actual.get("service");
        ApiModel apiModel = getApiModel(service);
        String template_final = apiModel.getTemplate();
        JSONObject final_template = JSON.parseObject(template_final);
        if(actual.get("fields")==null){
            actual.put("fields",new HashMap<>());
        }
        Map<String, Object> map = checkFields((Map<String, Object>) actual.get("fields"), final_template.getJSONArray("fields"),final_template);


        //检查状态是否成功
        String msg = null;
        if(!(boolean)map.get("success")){
            return map;
        }


        updateFields((Map<String, Object>) actual.get("fields"), final_template);
        Map<String, Object> fields = getDictFields(final_template, "fields");
        Map<String, Object> default_fields = getDictFields(final_template, "default_fields");
        Map<String,Object> result = new HashMap<>();
        String action_type = final_template.getString("action_type");
        boolean success = true;
        try {
            long startTime = System.currentTimeMillis();
            SSHUtil ssh  = new SSHUtil(fields);
            String return_session = (String) default_fields.get("return_session");
            if("true".equals(return_session) && "service".equals(action_type)){
                result.put("ssh_session",ssh);
            }else{
                ssh.close();
            }

            long endTime = System.currentTimeMillis();
            msg= String.format("连接成功,消耗 %s ms",(endTime - startTime));
        } catch (JSchException e) {
            e.printStackTrace();
            msg = e.toString();
            success = false;
        }


        result.put("success",success);
        result.put("msg",msg);
        return result;
    }
}
