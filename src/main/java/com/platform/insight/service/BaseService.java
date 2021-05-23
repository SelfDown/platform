package com.platform.insight.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platform.insight.model.ApiModel;
import com.platform.insight.model.PlatformTemplate;
import com.platform.insight.service.log.PlatformLog;
import com.platform.insight.service.log.PlatformLogImp;
import com.platform.insight.utils.OperationUtils;
import com.platform.insight.utils.ResultUtils;
import com.platform.insight.utils.SpringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service(BaseService.ID)
public class BaseService {
    public static final String ID = "base_service";

    /**
     * 不知类型进行返回结果
     *
     * @param template
     * @return
     */
    public Map<String, Object> service_result(Map<String, Object> template, Boolean is_http) {

        Map<String, Object> result = null;
        String service = (String) template.get("service");
        if (StringUtils.isEmpty(service)) {
            return ResultUtils.getResultMap(false, "查询失败，service 不能为空");
        }


        ApiService apiService = SpringUtils.getBean(ApiService.class);
        //查询是否存在
        ApiModel apiModel = apiService.queryByService(service);
        if (apiModel == null) {
            return ResultUtils.getResultMap(false, "查询失败，service 不存在");
        }

        String template_final = apiModel.getTemplate();
        PlatformTemplate final_template = PlatformTemplate.parseTemplate(template_final);
        PlatformLog log = new PlatformLogImp();
        final_template.setLog(log);
        final_template.log(final_template.getService()+"数据初始化成功");
        String service_type = final_template.getServiceType();//获取类型
        Result api = null;
        try {
            api = SpringUtils.getBean(service_type);
        } catch (Exception e) {
            final_template.log("没有找数据模板对象");

        }

        if (api == null) {
            return ResultUtils.getResultMap(false, "类型不存在!");
        }

        final_template.log("请求服务：" + final_template.getService() + "存储模板数据");
        api.setFinalTemplate(final_template);

        //生命周期：预检查
        final_template.log("生命周期：check 进行检查数据");
        Map<String, Object> check_result = api.check(template, final_template, is_http);
        if (!ResultUtils.isSuccess(check_result)) {
            return check_result;
        }

        final_template.log("生命周期：prepare 进行整合数据");
        Map<String, Object> prepare = api.prepare(template);
        if (!ResultUtils.isSuccess(prepare)) {
            return prepare;
        }

        //生命周期：处理结果
        final_template.log("生命周期：result 进行执行结果数据");
        result = api.result(template);
        String msg = (String) result.get("msg");
        result.remove("msg");
        boolean status = true;
        if (result.containsKey("success")) {
            status = (boolean) result.get("success");
            result.remove("success");
        }
        //生命周期：结束执行
        final_template.log("生命周期：finish 执行结束");
        api.finish(final_template);

        //添加操作日志
        if (!service.contains("service_operation_log")) {
            OperationUtils.addOperationLog(apiModel.getBackup(), JSONObject.toJSONString(template), msg);
        }
        return ResultUtils.getResultMap(status, msg, result);

    }
}
