package com.sql_api.insight.service_platform_update;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface ApiService {
    //更新新增字段
    JSONObject updateFields(Map<String,Object> actual, JSONObject template);

    JSONObject updateFilters(Map<String,Object> actual,JSONObject template);

    Map<String,Object> updateRecord(JSONObject template);

    Map<String,Object> checkFilters(JSONObject template);
}
