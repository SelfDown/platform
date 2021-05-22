package com.platform.insight.platform_tool.filters;


import com.platform.insight.model.PlatformTemplate;
import com.platform.insight.model.TemplateItemArr;
import com.platform.insight.model.TemplateItemObj;
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
    public void filter(Map<String, Object> actual, PlatformTemplate template) {
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
        //获取默认字段

//        Map<String, TemplateItemObj> fields_dict = fields.getTemplateItemObjDict();
////        List<TemplateItemObj> apList = new ArrayList();
////        for (int i =0;i<fields.size();i++){
////            // 获取默认值字段
////            TemplateItemObj templateItemObj = default_fields.getTemplateItemObj(i);
////            String field_1 = templateItemObj.getField_1();
////            /**
////             * 如果默认字段已经存在该字段，则跳过，以便保证设置默认值不能修改，比如delete_flag一直是0
////             */
////            if(fields_dict.containsKey(field_1)){
////                continue;
////            }else{
////                apList.add(templateItemObj);
////            }
////        }
//        //将fields 合并到default_fields 中
        default_fields.addAll(0, fields);

        for (int i = 0; i < default_fields.size(); i++) {
            TemplateItemObj object = default_fields.getTemplateItemObj(i);
            Object rulesValue = object.getField_3RuleValue();
            //填充数据
            if (rulesValue != null) {
                object.setField_5(rulesValue);
            }
        }
        template.setDefaultFields(default_fields);

    }
}
