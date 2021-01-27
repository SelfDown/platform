package com.platform.insight.filters;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.platform.insight.model.PlatformTemplate;
import com.platform.insight.utils.Const;
import com.platform.insight.utils.ServiceUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service(BaseFilter.ID)
public class BaseFilter extends ServiceUtils implements PlatformFilter {
    /**
     * 基础过滤器，将fields
     */
    public static final String ID = "base_filter";

    /**
     * 将actual 的数据合并到 search_fields(或者fields),将default_fields 的数据合并到 fields
     * 注意顺序，最后是将default_field 合并到数据列。表示actual 传了某个字段，也会被default_field替换.如果default_field 也存在这个字段
     * @param actual   请求数据
     * @param template 本地模板
     */
    @Override
    public void filter(Map<String, Object> actual, PlatformTemplate template) {
        //获取可更改字段
        JSONArray fields = template.getJSONArray(Const.FIELDS);
//        if (fields == null) {
//            fields = template.getJSONArray(Const.SEARCH_FIELD);
//        }
        //获取默认字段
        JSONArray default_fields = template.getJSONArray(Const.DEFAULT_FIELD);
        //将更在字段加入默认字段
        for (int i = 0; i < fields.size(); i++) {
            JSONObject jsonObject = fields.getJSONObject(i);
            String field = jsonObject.getString(Const.FIELD_1);

            if (actual != null && actual.containsKey(field)) {

                //如果类型是字符串，就可以为空，否则不会更新
                String type = (String) jsonObject.get(Const.FIELD_3);
                if (!"S".equals(type)) {
                    Map<String, Object> emptyField = isEmptyField(actual, field);
                    if (emptyField != null) {
                        continue;
                    }
                }
                //修改数据
                jsonObject.put("field_5", actual.get(field));
                JSONObject defaultField = getDefaultField(default_fields, field);

                //没有就添加新字段,否则就修改
                if (defaultField == null) {
                    default_fields.add(jsonObject);
                } else {
                    defaultField.put("field_5", actual.get(field));
                }

            }
        }

        for (int i = 0; i < default_fields.size(); i++) {
            JSONObject object = default_fields.getJSONObject(i);
            Object rulesValue = getRulesValue(object, actual);
            //填充数据
            if (rulesValue != null) {
                object.put("field_5", rulesValue);
            }
        }

    }
}
