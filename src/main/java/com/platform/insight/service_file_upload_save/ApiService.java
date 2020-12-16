package com.platform.insight.service_file_upload_save;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface ApiService {
    Map<String,Object> checkAccept(JSONObject template);
}
