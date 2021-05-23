package com.platform.insight.platform_tool.rule_value;

import com.platform.insight.model.PlatformTemplate;
import com.platform.insight.model.TemplateItemObj;
import com.platform.insight.platform_tool.rule_value.ept.RuleException;
import com.platform.insight.platform_tool.rule_value.ept.RuleNotExistFieldException;
import com.platform.insight.platform_tool.rule_value.ept.RuleTypeException;

public interface RuleValue {
    Object field_3Value(TemplateItemObj itemObj, PlatformTemplate template) throws RuleException, RuleTypeException,RuleNotExistFieldException;
    Object fieldValue(String field,TemplateItemObj itemObj, PlatformTemplate template) throws RuleException, RuleTypeException, RuleNotExistFieldException;
}
