package com.platform.insight.filters;

import com.platform.insight.model.PlatformTemplate;

import java.util.Map;

public interface PlatformFilter {
     void filter(Map<String, Object> actual, PlatformTemplate template);
}
