package com.platform.insight.model;

import com.alibaba.fastjson.JSONObject;
import com.platform.insight.utils.Const;

public class PlatformTemplate extends JSONObject {

    /**
     * 将json 对象转为平台模板
     *
     * @param json
     * @return
     */
    public static PlatformTemplate parseTemplate(String json) {
        PlatformTemplate template = parseObject(json, PlatformTemplate.class);
        return template;
    }

    public String getService() {
        return getString(Const.SERVICE);
    }
}


