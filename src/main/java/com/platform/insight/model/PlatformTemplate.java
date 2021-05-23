package com.platform.insight.model;

import com.alibaba.fastjson.JSONObject;
import com.platform.insight.service.log.PlatformLog;
import com.platform.insight.utils.Const;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlatformTemplate extends JSONObject {

    /**
     * 将json 对象转为平台模板
     *
     * @param json
     * @return
     */
    private PlatformLog log = null;


    public void setLog(PlatformLog log) {
        this.log = log;
    }

    public void log(Object data) {
        if (this.log != null) {
            this.log.log(this.getBelong(), data);
        }
    }

    private String getBelong() {
        return getString(Const.BELONG);
    }

    private static void setBelong(PlatformTemplate template, String belong) {
        template.put(Const.BELONG, belong);
    }

    public static PlatformTemplate parseTemplate(String json) {
        PlatformTemplate template = parseObject(json, PlatformTemplate.class);
        String belong = UUID.randomUUID().toString();
        setBelong(template, belong);
        return template;
    }

    public String getService() {
        return getString(Const.SERVICE);
    }

    public String getServiceType() {
        return getString(Const.SERVICE_TYPE);
    }

    /**
     * 将 JSON array 分组一层
     *
     * @param field
     * @return
     */
    public TemplateItemArr getTemplateArr(String field) {
        String s = this.getJSONArray(field).toJSONString();
        return TemplateItemArr.parseTemplateItemArr(s);
    }

    public TemplateItemArr getDefaultFields() {
        return getTemplateArr(Const.DEFAULT_FIELDS);
    }

    public TemplateItemArr getFilters() {
        return getTemplateArr(Const.FILTERS);
    }

    public TemplateItemArr getChecks() {
        return getTemplateArr(Const.CHECKS);
    }

    public TemplateItemArr getFields() {
        return getTemplateArr(Const.FIELDS);
    }


    public Map<String, Object> getDefaultFieldsDict() {
        TemplateItemArr fields = this.getDefaultFields();
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < fields.size(); i++) {
            TemplateItemObj templateItemObj = fields.getTemplateItemObj(i);
            String field_1 = templateItemObj.getField_1();
            Object field_5 = templateItemObj.getField_5();
            if (StringUtils.isEmpty(field_1)) {
                continue;
            }
            map.put(field_1, field_5);
        }
        return map;
    }

    public TemplateItemObj getPageTemplateItem() {
        Map<String, Object> fieldsItemDict = this.getFieldsItemDict();
        if (fieldsItemDict.containsKey(Const.PAGE)) {
            TemplateItemObj o = (TemplateItemObj) fieldsItemDict.get(Const.PAGE);
            return o;
        }
        return null;

    }

    public TemplateItemObj getSizeTemplateItem() {
        Map<String, Object> fieldsItemDict = this.getFieldsItemDict();
        if (fieldsItemDict.containsKey(Const.SIZE)) {
            TemplateItemObj o = (TemplateItemObj) fieldsItemDict.get(Const.SIZE);
            return o;
        }
        return null;

    }

    public Map<String, Object> getFieldsItemDict() {
        TemplateItemArr fields = this.getFields();
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < fields.size(); i++) {
            TemplateItemObj templateItemObj = fields.getTemplateItemObj(i);
            String field_1 = templateItemObj.getField_1();

            if (StringUtils.isEmpty(field_1)) {
                continue;
            }
            map.put(field_1, templateItemObj);
        }
        return map;

    }

    public void setDefaultFields(TemplateItemArr defaultFields) {
        this.put(Const.DEFAULT_FIELDS, defaultFields);
    }

    public String getDataSQL() {
        return this.getString(Const.DATA_SQL);
    }

    public boolean isHttpNotSupport(boolean isHttp) {
        String action_type = this.getString(Const.ACTION_TYPE);
        return isHttp && !StringUtils.isEmpty(action_type) && !Const.HTTP.equals(action_type);

    }

    public boolean isRequireLogin() {
        String login_require = this.getString(Const.LOGIN_REQUIRE);
        return Const.TRUE.equals(login_require);

    }


}













