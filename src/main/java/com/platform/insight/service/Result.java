package com.platform.insight.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface Result {

    /**
     * 生命周期：检查数据
     * @param actual 前台传的参数
     * @param final_template 后台模板
     * @param is_http 是否是http,内部调用
     * @return
     */
    Map<String,Object> check(Map<String,Object> actual,JSONObject final_template,boolean is_http);

    /**
     *  预处理，
     *  主要进行一些配置参数fields 和实际值actual 参数替换，还有默认配置default_field,添加
     * @return
     */
    Map<String,Object> prepare(Map<String,Object> actual);

    /**
     * 生命周期执行，根据类型进行返回结果
     * @param actual
     * @return
     */

    Map<String,Object> result(Map<String,Object> actual);
    /**
     * 生命周期结束
     * @param final_template,模板
     * @return
     */
    Map<String,Object> finish(JSONObject final_template);


    JSONObject getFinalTemplate();
    void setFinalTemplate(JSONObject finalTemplate);

    void setFileList(List<MultipartFile> files);
    List<MultipartFile> getFileList();

    long getCount();


}
