package com.sql_api.insight.action;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sql_api.insight.model.ApiModel;
import com.sql_api.insight.service.ApiService;
import com.sql_api.insight.service.BaseService;
import com.sql_api.insight.service.Result;
import com.sql_api.insight.utils.*;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("insight/service")
public class ServiceController {


    @Autowired
    Environment env;


    @RequestMapping(value = "result", method = RequestMethod.POST)
    @ResponseBody
    private String result(@RequestBody Map<String, Object> template) {
        BaseService baseService = SpringUtils.getBean(BaseService.class);
        Map<String, Object> result = baseService.service_result(template,true);
        return JSON.toJSONString(result);
    }


    //文件上传的service的 type 必须以 service_file_upload 开头
    //@RequiresAuthentication
    @RequestMapping(value = "fileUpload", method = RequestMethod.POST)
    @ResponseBody
    public String upload(@RequestParam("file") List<MultipartFile> files, String service, String tag) {


        if (files.isEmpty()) {
            return ResultUtils.getResult(false, "文件为空");
        }


        ApiService apiService = SpringUtils.getBean(ApiService.class);
        //查询是否存在
        ApiModel apiModel = apiService.queryByService(service);
        if (apiModel == null) {
            return ResultUtils.getResult(false, "查询失败，service 不存在");
        }

        String template_final = apiModel.getTemplate();
        JSONObject final_template = JSON.parseObject(template_final);

        String login_require = final_template.getString("login_require");


        String currentUser = UserManager.getCurrentPkUser();
        if (currentUser == null) {
            throw new UnauthenticatedException();
        }


        Map<String, Object> result = null;

        String service_type = final_template.getString("service_type");//获取类型
        if (!service_type.startsWith("service_file_upload")) {
            return ResultUtils.getResult(false, "仅支持文件上传");
        }
        Result api = null;
        api = SpringUtils.getBean(service_type);
        if (api == null) {
            return ResultUtils.getResult(false, "接口类型不存在!");
        }
        api.setFileList(files);
        Map<String, Object> template = new HashMap<>();
        template.put("service", service);
        result = api.result(template);

        boolean status = true;
        if (result.containsKey("success")) {
            status = (boolean) result.get("success");
        }

        String msg = (String) result.get("msg");
        Object data = result.get("data");

        return ResultUtils.getResult(status, msg, data, tag);

    }

    //    @RequiresAuthentication
    @RequestMapping(value = "download", method = RequestMethod.GET)
    @ResponseBody
    public String download(String id, HttpServletResponse response) {
        Map<String, Object> fileSourcePath = FileInfoUtils.getFileSourcePath(id);
        if (fileSourcePath != null) {
            try {
                FileInfoUtils.download(fileSourcePath, response);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return "下载成功";
        } else {
            return "下载失败";
        }

    }
}
