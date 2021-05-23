package com.platform.insight.platform_tool.rule_value.imp;

import com.platform.insight.model.PlatformTemplate;
import com.platform.insight.model.TemplateItemObj;
import com.platform.insight.platform_tool.rule_value.BaseRuleValue;
import com.platform.insight.platform_tool.rule_value.ept.RuleException;
import com.platform.insight.platform_tool.rule_value.ept.RuleTypeException;
import com.platform.insight.utils.Const;
import org.springframework.stereotype.Service;

@Service(IRuleValue.ID)
public class IRuleValue extends BaseRuleValue {
    public static final String ID = "I" + Const.RULE_VALUE;

    @Override
    public Object fieldValue(String field, TemplateItemObj itemObj,PlatformTemplate template) throws RuleTypeException {
        try {
            return itemObj.getInteger(Const.FIELD_5);
        } catch (Exception e) {
            throw new RuleTypeException("转换类型 Int 类型失败");
        }

    }
}
