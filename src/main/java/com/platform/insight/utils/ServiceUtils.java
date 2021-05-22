package com.platform.insight.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServiceUtils {

    /**
     * 第一列字段，第三列 规则，第五列。默认数据
     *
     * @param field_obj
     * @param actual
     * @return
     */
    protected Object getRulesValue(JSONObject field_obj, Map<String, Object> actual) {
        String field = field_obj.getString("field_1");
        String rule = field_obj.getString("field_3");
        //数据类型
        //todo:将这里rule 抽取成一个配置,每个配置对应一个方法
        switch (rule) {
            case "S":
                return field_obj.getString("field_5");

            case "I":
                //如果有值就转换
                if (isEmptyField(actual, field) == null) {
                    return field_obj.getInteger("field_5");
                }
                break;
            case "D":
                //如果有值就转换
                if (isEmptyField(actual, field) == null) {
                    return field_obj.getDoubleValue("field_4");
                }
                break;
            case "S2L":
                //将字符串，转字符串List
                if (isEmptyField(actual, field) == null) {
                    return field_obj.getString("field_5").split(",");
                }
                break;

            case "I2L":
                //将I int 类型，转Int List
            case "UUID":
                return UUID.randomUUID().toString();
            case "DATETIME":
                return DateTimeUtil.getTimestampStr();
            case "USER_ID":
                return UserManager.getCurrentPkUser();
            case "PASSWORD":
                //用户名
                String username = (String) actual.get("username");
                //初始密码
                String password = MD5Utils.getMD5String("123456");
                EncryptContext dc = new EncryptContext(MD5Utils.getMD5String(username + "admindms@888").toUpperCase(), password.toUpperCase());
                return dc.getEncryptPassword();
        }
        return null;
    }

    /**
     * @param default_fields
     * @param field
     * @return 返回找到字段的对象，否则null
     */
    protected JSONObject getDefaultField(JSONArray default_fields, String field) {
        for (int i = 0; i < default_fields.size(); i++) {
            JSONObject jsonObject = default_fields.getJSONObject(i);
            String field_1 = jsonObject.getString("field_1");
            if (field.equals(field_1)) {
                return jsonObject;
            }
        }
        return null;
    }

    public static Map<String, Object> getResult(boolean success, String msg) {
        return ResultUtils.getResultMap(success, msg);
    }

    public static Map<String, Object> getResult(boolean success, String msg, Map<String, Object> data) {
        return ResultUtils.getResultMap(success, msg, data);
    }

    public static boolean isSuccess(Map<String, Object> result) {
        return (boolean) result.getOrDefault(Const.SUCCESS, false);

    }

    /**
     * @param actual
     * @param field
     * @return 空的就返回对象，否则返回null
     */
    protected Map<String, Object> isEmptyField(Map<String, Object> actual, String field) {

        if (actual == null || !actual.containsKey(field) || StringUtils.isEmpty(actual.get(field) + "")) {
            return getResult(false, field + "：字段不能为空");
        }
        return null;
    }

    /**
     *  判断是否为空值，
     *  没有key。或者有key ，但是值是空
     * @param actual
     * @param field
     * @return
     */
    protected boolean isEmptyFieldValue(Map<String, Object> actual, String field){
        if (actual == null || !actual.containsKey(field) || StringUtils.isEmpty(actual.get(field) + "")) {
            return true;
        }
        return false;
    }

    /**
     * @param actual
     * @param field
     * @return 空的就返回对象，否则返回null
     */
    protected Map<String, Object> isUpdateEmptyField(Map<String, Object> actual, String field, JSONObject field_template) {

        if (StringUtils.isEmpty(actual.get(field) + "")) {
            String type = field_template.getString("field_3");
            if (!"S".equals(type)) {
                return getResult(false, field + "：字段不能为空");
            }

        }
        return null;
    }

    protected boolean isJSONObject(String content) {
        try {
            JSONObject object = JSON.parseObject(content);
            if (object == null) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }


    }

    /**
     * 判断是否是规则数据
     *
     * @param
     * @return 返回规则数据，否则返回空
     */
    protected String isRulesPatternValue(String value) {
        if (value.indexOf("$") == 0 && value.lastIndexOf("$") == (value.length() - 1)) {
            return value.substring(1, value.length() - 1);
        }
        return null;
    }


    public static Object getBeanByName(String name){
        try {
            return SpringUtils.getBean(name);
        }catch (Exception e){
            return null;
        }


    }


}
