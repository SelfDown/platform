package com.platform.insight.model;

import com.alibaba.fastjson.JSONObject;
import com.platform.insight.platform_tool.rule_value.BaseRuleValue;
import com.platform.insight.platform_tool.rule_value.RuleValue;
import com.platform.insight.utils.Const;
import org.springframework.util.StringUtils;

public class TemplateItemObj extends JSONObject {
    public String getField_1() {
        return this.getString(Const.FIELD_1);
    }

    public String getField_2() {
        return this.getString(Const.FIELD_2);
    }

    public String getField_3() {
        return this.getString(Const.FIELD_3);
    }

    public Object getField_5() {
        return this.get(Const.FIELD_5);
    }
    public static TemplateItemObj parseTemplateItemObj(String json){
        return parseObject(json,TemplateItemObj.class);
    }

    /**
     * 设置取值字段
     *
     * @param value
     */

    public void setField_5(Object value) {
        this.put(Const.FIELD_5, value);
    }


    public Object getField_3RuleValue() {
        String field_3 = getField_3();
        if (StringUtils.isEmpty(field_3)) {

        }
        RuleValue ruleBean = BaseRuleValue.getRuleBean(field_3);
        try {
            return ruleBean.field_3Value(this);
        } catch (Exception e) {
            return null;
        }

    }
}
