package com.platform.insight.platform_tool.check;

import com.platform.insight.model.PlatformTemplate;

import java.util.Map;

public interface PlatformCheck {
    Map<String, Object> check(Map<String, Object> actual, PlatformTemplate final_template, boolean is_http);
}
