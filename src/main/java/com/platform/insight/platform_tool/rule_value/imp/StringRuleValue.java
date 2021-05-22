package com.platform.insight.platform_tool.rule_value.imp;

import com.platform.insight.model.TemplateItemObj;
import com.platform.insight.platform_tool.rule_value.BaseRuleValue;
import com.platform.insight.utils.Const;
import org.springframework.stereotype.Service;

@Service(StringRuleValue.ID)
public class StringRuleValue extends BaseRuleValue {
    public static final String ID = "S" + Const.RULE_VALUE;

    @Override
    public Object fieldValue(String field, TemplateItemObj itemObj) {
        return itemObj.getString(Const.FIELD_5);
    }
}
