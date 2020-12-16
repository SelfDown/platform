package com.platform.insight.service;



import com.platform.insight.model.ApiModel;

import java.util.List;

public interface ApiService {
    String ID = "ApiService";

    boolean saveApi(ApiModel api);

    /**
     * 根据服务找模板
     * @param service
     * @return
     */
    ApiModel queryByService(String service);

    /**
     * 根据类型找模板列表
     * @param type
     * @return
     */
    List<ApiModel> queryByType(String type);
    //删除api
    boolean deleteApi(String service);
    //更新api
    boolean updateApi(ApiModel api);
    /*
    Map<String, Object> queryResult(JSONObject template);
    // 获取显示字段
    List<Map<String,Object>> getFields(JSONObject template);

    //模板更新显示字段
    JSONObject updateSearchFields(Map<String,Object> actual,JSONObject template);

    //检查新增字段
    Map<String,Object> checkFields(Map<String, Object> actual, JSONArray fields,JSONObject template);
    //更新新增字段
    JSONObject updateFields(Map<String,Object> actual,JSONObject template);
    //插入记录
    Map<String,Object> insertRecord(JSONObject template);


    Map<String,Object> updateRecord(JSONObject template);


    JSONObject updateFilters(Map<String,Object> actual,JSONObject template);
    //检查更新字段
//    Map<String,Object> checkUpdateFields(Map<String, Object> actual,Map<String, Object> filters,JSONObject template);
    */
}
