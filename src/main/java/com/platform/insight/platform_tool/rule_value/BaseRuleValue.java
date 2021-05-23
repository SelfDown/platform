package com.platform.insight.platform_tool.rule_value;

import com.platform.insight.model.PlatformTemplate;
import com.platform.insight.model.TemplateItemObj;
import com.platform.insight.platform_tool.rule_value.ept.RuleException;
import com.platform.insight.platform_tool.rule_value.ept.RuleTypeException;
import com.platform.insight.platform_tool.rule_value.imp.StringRuleValue;
import com.platform.insight.utils.Const;
import com.platform.insight.utils.SpringUtils;

public abstract class BaseRuleValue implements RuleValue {
    @Override
    public Object field_3Value(TemplateItemObj itemObj, PlatformTemplate template) throws RuleException, RuleTypeException {
        return this.fieldValue(itemObj.getField_3(), itemObj,template);
    }

    public static String getRuleBeanName(String rule) {
        return rule + Const.RULE_VALUE;
    }

    public static RuleValue getRuleBean(String rule) {
        try {
            RuleValue ruleValue = SpringUtils.getBean(getRuleBeanName(rule));
            if (ruleValue != null) {
                return ruleValue;
            }
        } catch (Exception e) {

        }

//        /**
//         *  调试过程中使用
//         */
//        if (rule.equals("S")) {
//            return new StringRuleValue();
//        }
        return null;

    }


}
