package com.platform.insight.model;

import com.alibaba.fastjson.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class TemplateItemArr extends JSONArray {
    public TemplateItemObj getTemplateItemObj(int index) {
        return TemplateItemObj.parseTemplateItemObj(this.getJSONObject(index).toJSONString());
    }

    /**
     *  获取 field_1 对应模板数据
     * @return
     */
    public Map<String, TemplateItemObj> getTemplateItemObjDict() {
        Map<String, TemplateItemObj> dict = new HashMap<>();
        for (int i = 0; i < this.size(); i++) {
            TemplateItemObj templateItemObj = this.getTemplateItemObj(i);
            String field_1 = templateItemObj.getField_1();
            dict.put(field_1, templateItemObj);
        }
        return dict;
    }

    public static TemplateItemArr parseTemplateItemArr(String json){
        return TemplateItemArr.parseObject(json,TemplateItemArr.class);
    }
}
