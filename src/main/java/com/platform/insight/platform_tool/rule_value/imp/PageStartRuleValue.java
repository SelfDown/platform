package com.platform.insight.platform_tool.rule_value.imp;

import com.platform.insight.model.PlatformTemplate;
import com.platform.insight.model.TemplateItemObj;
import com.platform.insight.platform_tool.rule_value.BaseRuleValue;
import com.platform.insight.platform_tool.rule_value.ept.RuleException;
import com.platform.insight.platform_tool.rule_value.ept.RuleNotExistFieldException;
import com.platform.insight.platform_tool.rule_value.ept.RuleTypeException;
import com.platform.insight.platform_tool.rule_value.ept.RuleValueErrorException;
import com.platform.insight.utils.Const;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service(PageStartRuleValue.ID)
public class PageStartRuleValue extends BaseRuleValue {
    public static final String ID = "PAGE_START" + Const.RULE_VALUE;

    @Override
    public Object fieldValue(String field, TemplateItemObj itemObj, PlatformTemplate template) throws RuleTypeException, RuleNotExistFieldException {

        TemplateItemObj pageTemplateItem = template.getPageTemplateItem();
        if (pageTemplateItem == null) {
            throw new RuleNotExistFieldException(" page 字段没有配置");
        }
        TemplateItemObj sizeTemplateItem = template.getSizeTemplateItem();
        if (sizeTemplateItem == null) {
            throw new RuleNotExistFieldException(" size 字段没有配置");
        }
        int page = 0;
        try {
            page = pageTemplateItem.getInteger(Const.FIELD_5);
        } catch (Exception e) {
            throw new RuleTypeException(" page 类型不正确");
        }
        if (page <= 0) {
            throw new RuleValueErrorException("页号不能小于 0");
        }

        int size = 0;
        try {
            size = sizeTemplateItem.getInteger(Const.FIELD_5);
        } catch (Exception e) {
            throw new RuleTypeException(" size 类型不正确");
        }

        int start = (page - 1) * size;


        return start;

    }
}

