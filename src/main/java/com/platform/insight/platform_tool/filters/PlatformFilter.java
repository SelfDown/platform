package com.platform.insight.platform_tool.filters;

import com.platform.insight.model.PlatformTemplate;

import java.util.Map;

public interface PlatformFilter {
     Map<String, Object> filter(Map<String, Object> actual, PlatformTemplate template);
}
