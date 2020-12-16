package com.sql_api.insight.ssh.service_server_ssh;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sql_api.insight.model.ApiModel;
import com.sql_api.insight.service.BaseService;
import com.sql_api.insight.service.ResultBaseService;
import com.sql_api.insight.utils.EnvUtils;
import com.sql_api.insight.utils.SSHUtil;
import com.sql_api.insight.utils.SpringUtils;
import com.sql_api.insight.utils.StringTemplateUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(ServiceServerSSH.ID)
public class ServiceServerSSH extends ResultBaseService {
    public static final String ID = "service_server_ssh";
    public static final String SSH_SESSION_KEY = "ssh_session";

    @Override
    public Map<String, Object> result(Map<String, Object> actual) {

        JSONObject final_template = getFinalTemplate();

        //prepare 检查ssh 登录
        Map<String, Object> prepareResult = handlePrepare(final_template);
        if (!(boolean) prepareResult.get("success")) {
            return prepareResult;
        }
        //execute 执行
        Map<String, Object> result = handleExecute(final_template);
        return result;
    }

    public SSHUtil get_ssh() {
        JSONObject finalTemplate = this.getFinalTemplate();
        SSHUtil return_session = (SSHUtil) finalTemplate.get(SSH_SESSION_KEY);
        return return_session;
    }

    @Override
    public Map<String, Object> finish(JSONObject final_template) {
        close_session(final_template);
        return getResult(true, "关闭session成功");
    }

    private Map<String, Object> handleExecute(JSONObject final_template) {
        Map<String, Object> data = new HashMap<>();
        JSONArray execute = final_template.getJSONArray("execute");
        SSHUtil ssh = get_ssh();
        for (int i = 0; i < execute.size(); i++) {
            JSONObject execute_shell = execute.getJSONObject(i);
            String shell = execute_shell.getString("field_5");
            String rule = execute_shell.getString("field_3");
            String shell_result = ssh.execute_shell(shell);
            JSONObject rule_obj = JSONObject.parseObject(rule);
            Boolean result2web = rule_obj.getBoolean("result2web");
            if (result2web) {
                String expect = rule_obj.getString("expect");
                data.put(expect, shell_result);
            }

        }
        return getResult(true, "执行成功", data);

    }

    //如果有ssh_session 这个字符串表示，是ssh 登录信息
    private void save_session(JSONObject final_template, Map<String, Object> result) {
        if (!result.containsKey("data")) {
            return;
        }
        Map<String, Object> result_data = (Map<String, Object>) result.get("data");
        if (result_data.size() == 0) {
            System.out.println("登录信息返回异常");
        }
        if (result_data.containsKey(SSH_SESSION_KEY)) {
            if (EnvUtils.isDebugger()) {
                SSHUtil return_session = (SSHUtil) result_data.get(SSH_SESSION_KEY);
                System.out.println("保存登录信息" + return_session.getHost());
            }
            //保存登录信息到模板
            final_template.put(SSH_SESSION_KEY, result_data.get(SSH_SESSION_KEY));
        }
    }

    private void close_session(JSONObject final_template) {
        if (final_template.containsKey(SSH_SESSION_KEY)) {
            if (EnvUtils.isDebugger()) {
                SSHUtil return_session = (SSHUtil) final_template.get(SSH_SESSION_KEY);
                System.out.println("清除登录信息" + return_session.getHost());
            }
            //关闭登录信息
            SSHUtil ssh = final_template.getObject(SSH_SESSION_KEY, SSHUtil.class);
            ssh.close();
        }
    }


    private Map<String, Object> handlePrepare(JSONObject final_template) {
        Map<String, Object> default_fields = getDictFields(final_template, "default_fields");
        Map<String, Object> result = new HashMap<>();
        JSONArray prepare = final_template.getJSONArray("prepare");
        List<Map<String, Object>> service_list = new ArrayList<>();
        for (int i = 0; i < prepare.size(); i++) {
            JSONObject obj = prepare.getJSONObject(i);
            String field_3 = obj.getString("field_3");
            String expect = obj.getString("field_5");
            String service = StringTemplateUtils.render(field_3, default_fields);
            BaseService baseService = SpringUtils.getBean(BaseService.class);
            Map service_map = JSON.parseObject(service, Map.class);
            Map<String, Object> service_result = baseService.service_result(service_map, false);
            Map expect_map = JSON.parseObject(expect, Map.class);
            for (Object key : expect_map.keySet()) {
                Object expected = expect_map.get(key);
                Object actual = service_result.get(key);
                //如果返回结果不是，不符合配置，直接抛出错误
                if (!actual.equals(expected)) {
                    result.put("success", false);
                    Map<String, Object> request = new HashMap<>();
                    request.put("request", service_map);
                    request.put("result", service_result);
                    result.put("data", request);
                    if (service_result.containsKey("msg")) {
                        result.put("msg", service_result.get("msg"));
                    }
                    return result;
                } else {
                    //保存登录信息
                    save_session(final_template, service_result);
                }
            }
        }

        result.put("success", true);
        return result;
    }
}
