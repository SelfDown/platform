package com.platform.insight.model;

import com.alibaba.fastjson.JSONObject;
import com.platform.insight.platform_tool.rule_value.BaseRuleValue;
import com.platform.insight.platform_tool.rule_value.RuleValue;
import com.platform.insight.platform_tool.rule_value.ept.RuleException;
import com.platform.insight.platform_tool.rule_value.ept.RuleTypeException;
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



    public String getRuleName(){
        String field_3 = getField_3();
        if (!StringUtils.isEmpty(field_3)){
            return field_3.split(Const.SPLIT)[0];
        }else{
            return null;
        }
    }

    public Object getField_3RuleValue(PlatformTemplate template) throws RuleException, RuleTypeException {
        String ruleName = getRuleName();
        if (StringUtils.isEmpty(ruleName)) {
            return getField_5();

        }
        RuleValue ruleBean = BaseRuleValue.getRuleBean(ruleName);
        if(ruleBean==null){
            throw new RuleException("规则不存在");
        }
        return ruleBean.field_3Value(this,template);

    }

    public String getErrorRuleFormatInfo( String msg) {
        return "字段：【" + this.getField_1() + "】值：【" + this.getField_5() + "】 规则：【" + this.getRuleName() + "】 错误信息：" + msg;

    }
}
