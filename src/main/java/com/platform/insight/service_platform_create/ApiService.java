package com.platform.insight.service_platform_create;



import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface ApiService {
    //更新新增字段
    JSONObject updateFields(Map<String,Object> actual, JSONObject template);

    //插入记录
    Map<String,Object> insertRecord(JSONObject template);
}
