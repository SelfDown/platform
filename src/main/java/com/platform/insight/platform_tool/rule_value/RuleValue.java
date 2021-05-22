package com.platform.insight.platform_tool.rule_value;

import com.platform.insight.model.TemplateItemObj;

public interface RuleValue {
    Object field_3Value(TemplateItemObj itemObj);
    Object fieldValue(String field,TemplateItemObj itemObj);
}
