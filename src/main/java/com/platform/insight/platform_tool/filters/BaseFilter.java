package com.platform.insight.platform_tool.filters;


import com.platform.insight.model.PlatformTemplate;
import com.platform.insight.model.TemplateItemArr;
import com.platform.insight.model.TemplateItemObj;
import com.platform.insight.platform_tool.rule_value.ept.RuleException;
import com.platform.insight.platform_tool.rule_value.ept.RuleTypeException;
import com.platform.insight.utils.ResultUtils;
import com.platform.insight.utils.ServiceUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service(BaseFilter.ID)
public class BaseFilter extends ServiceUtils implements PlatformFilter {
    /**
     * 基础过滤器，将fields
     */
    public static final String ID = "base_filter";

    public static BaseFilter getFilterBean(String check) {
        return (BaseFilter) getBeanByName(check);
    }


    /**
     * 将actual 的数据合并到 fields,将default_fields 的数据合并到 fields
     * 注意顺序，最后是将default_field 合并到数据列。表示actual 传了某个字段，也会被default_field替换.如果default_field 也存在这个字段
     *
     * @param actual   请求数据
     * @param template 本地模板
     */
    @Override
    public Map<String, Object> filter(Map<String, Object> actual, PlatformTemplate template) {
        //获取可更改字段
        TemplateItemArr fields = template.getFields();
        TemplateItemArr default_fields = template.getDefaultFields();

        //将更在字段加入默认字段
        for (int i = 0; i < fields.size(); i++) {
            TemplateItemObj itemObj = fields.getTemplateItemObj(i);
            String field = itemObj.getField_1();
            if (!actual.containsKey(field)) {
                continue;
            }
            //如果类型是字符串，就可以为空，否则不会更新
            String type = itemObj.getField_3();
            if (isEmptyFieldValue(actual, field)) {
                continue;
            }
            itemObj.setField_5(actual.get(field));


        }
        default_fields.addAll(0, fields);

        TemplateItemArr final_fields = new TemplateItemArr();

        for (int i = 0; i < default_fields.size(); i++) {
            TemplateItemObj object = default_fields.getTemplateItemObj(i);
            Object rulesValue = null;
            try {
                rulesValue = object.getField_3RuleValue(template);
                if (rulesValue != null) {
                    object.setField_5(rulesValue);
                }
            } catch (RuleTypeException e) {
                /**
                 * 如果转换类型错误直接返回
                 */
                String message = object.getErrorRuleFormatInfo(e.getMessage());
                template.log(message);
                return ResultUtils.getResultMap(false, message);
            } catch (RuleException e) {
                /**
                 * 普通错误记录日志
                 */
                String message = object.getErrorRuleFormatInfo(e.getMessage());
                template.log(message);

            } catch (Exception e) {
                e.printStackTrace();
            }

            final_fields.add(object);

        }
        template.setDefaultFields(final_fields);
        return ResultUtils.getResultMap(true, "数据整合成功");

    }
}
