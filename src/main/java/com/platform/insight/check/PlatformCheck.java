package com.platform.insight.check;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface PlatformCheck {
    Map<String, Object> check(Map<String, Object> actual, JSONObject final_template, boolean is_http);
}
