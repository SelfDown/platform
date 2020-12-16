package com.platform.insight.service_platform_query;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface ApiService {
    // 获取显示字段
    Map<String, Object> queryResult(JSONObject template);
    //模板更新显示字段
    JSONObject updateSearchFields(Map<String,Object> actual,JSONObject template);
    // 获取显示字段
    List<Map<String,Object>> getFields(JSONObject template);

    List<Map<String,Object>> toTree(List<Map<String,Object>> list,String parent_field,String id_field,String children);
}
