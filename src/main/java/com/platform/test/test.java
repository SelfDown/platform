package com.platform.test;

import com.alibaba.fastjson.JSONObject;
import com.platform.insight.model.PlatformTemplate;

public class test {
    public static void main(String[] args) {
        PlatformTemplate template = PlatformTemplate.parseTemplate("{'service':'zzhang'}");
        System.out.println(template);
        System.out.println(template.getService());
    }
}
