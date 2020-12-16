package com.platform.insight.action;


import com.platform.insight.model.ApiModel;
import com.platform.insight.service.ApiService;
import com.platform.insight.utils.ApiCacheUtils;
import com.platform.insight.utils.EnvUtils;
import com.platform.insight.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("insight/tools")
public class InsightApiController {

    @Autowired
    @Qualifier(ApiService.ID)
    ApiService apiService;

    @RequestMapping(value = "query")
    @ResponseBody
    public String query(String type){
        if(!EnvUtils.isDebugger()){
            return "";
        }

        if(StringUtils.isEmpty(type)){
            return ResultUtils.getResult(false,"类型不能为空！");
        }
        List<ApiModel> apiModels = apiService.queryByType(type);

        return  ResultUtils.getResult(true,"查询成功",apiModels);
    }


    @RequestMapping(value = "queryService")
    @ResponseBody
    public String queryService(String service){
        if(!EnvUtils.isDebugger()){
            return "";
        }

        if(StringUtils.isEmpty(service)){
            return ResultUtils.getResult(false,"service不能为空！");
        }
        ApiModel apiModels = apiService.queryByService(service);

        return  ResultUtils.getResult(true,"查询成功",apiModels);
    }

    @RequestMapping(value = "delete",method= RequestMethod.POST)
    @ResponseBody
    public String delete(String service){

        if(!EnvUtils.isDebugger()){
            return "";
        }
        if(StringUtils.isEmpty(service)){
            return ResultUtils.getResult(false,"service不能为空！");
        }
        ApiModel apiModel = apiService.queryByService(service);
        if(apiModel == null){
            ResultUtils.getResult(false,service+" 已经删除");
        }
        apiService.deleteApi(service);
        ApiCacheUtils.clear();

        return  ResultUtils.getResult(true,"删除成功");
    }


    @RequestMapping(value = "save",method= RequestMethod.POST)
    @ResponseBody
    public String save(ApiModel apiModel){

        if(!EnvUtils.isDebugger()){
            return "";
        }
        if(StringUtils.isEmpty(apiModel.getService())){
            return ResultUtils.getResult(true,"service 不能为空！");
        }
        ApiModel model = apiService.queryByService(apiModel.getService());
        if(model!=null){
            return ResultUtils.getResult(true,apiModel.getService()+" 已经存在");
        }
        boolean b = apiService.saveApi(apiModel);
        if(b==false){
            return ResultUtils.getResult(false,"保存失败！");
        }
        ApiCacheUtils.clear();
        return ResultUtils.getResult(true,"保存成功！");
    }


    @RequestMapping(value = "update",method= RequestMethod.POST)
    @ResponseBody
    public String update(ApiModel apiModel){

        if(!EnvUtils.isDebugger()){
            return "";
        }
        if(StringUtils.isEmpty(apiModel.getService())){
            return ResultUtils.getResult(true,"service 不能为空！");
        }
        ApiModel model = apiService.queryByService(apiModel.getService());
        if(model==null){
            return ResultUtils.getResult(true,apiModel.getService()+" 不存在");
        }
        boolean b = apiService.updateApi(apiModel);
        if(b==false){
            return ResultUtils.getResult(false,"保存失败！");
        }
        ApiCacheUtils.clear();
        return ResultUtils.getResult(true,"保存成功！");
    }

}
