package com.platform.insight.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import com.platform.insight.data_source.DatabaseConnections;
import com.platform.insight.factory.database.DatabaseFactory;
import com.platform.insight.model.ApiModel;
import com.platform.insight.model.PlatformTemplate;
import com.platform.insight.model.TemplateItemArr;
import com.platform.insight.model.TemplateItemObj;
import com.platform.insight.platform_tool.check.BaseCheck;
import com.platform.insight.platform_tool.check.PlatformCheck;
import com.platform.insight.platform_tool.filters.BaseFilter;
import com.platform.insight.platform_tool.filters.PlatformFilter;
import com.platform.insight.service_platform_query.ServicePlatformQuery;
import com.platform.insight.utils.*;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

public abstract class ResultBaseService extends ServiceUtils implements Result {
    List<MultipartFile> files = null;
    private long count = 0;
    private String day = "";
    private PlatformTemplate final_template;

    protected ApiModel getApiModel(String service) {
        ApiService api = SpringUtils.getBean(com.platform.insight.service.ApiService.ID);
        return api.queryByService(service);
    }

    /**
     * 从数据库里面查询
     *
     * @param service 服务名称
     * @return
     */
    protected PlatformTemplate getFinalTemplate(String service) {
        ApiModel apiModel = getApiModel(service);
        String template_final = apiModel.getTemplate();
        PlatformTemplate final_template = PlatformTemplate.parseTemplate(template_final);
        return final_template;
    }

    /**
     * 从内存里面取
     *
     * @return
     */

    @Override
    public PlatformTemplate getFinalTemplate() {
        return final_template;
    }

    @Override
    public void setFinalTemplate(PlatformTemplate finalTemplate) {
        this.final_template = finalTemplate;
    }


    /**
     * 获取当前连接的，数据库工厂
     *
     * @return
     */
    protected Map<String, Object> getDatabaseFactory() {

        String className = DatabaseUtils.getDatabaseClassName();
        DatabaseFactory databaseFactory = null;
        try {
            databaseFactory = SpringUtils.getBean(className);
        } catch (Exception e) {

        }
        if (databaseFactory == null) {
            return getResult(false, "暂不支持数据库类型:" + className);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", databaseFactory);
        return result;
    }

    protected Map<String, Object> checkFields(Map<String, Object> actual, JSONArray fields, JSONObject templateALL) {
        Map<String, Object> data = new HashMap<>();
        //JSONArray fields = template.getJSONArray("fields");
        if (fields == null) {
            return getResult(true, "检查配置不存在");
        }
        for (int i = 0; i < fields.size(); i++) {
            JSONObject jsonObject = fields.getJSONObject(i);

            String field = jsonObject.getString("field_1");
            if (StringUtils.isEmpty(field)) {
                continue;
            }
            /**以 | 分割 ，第二个字段
             * 检查规则,第三列，限制条件第五列
             */
            Map<String, Object> map_rules = checkActualRules(actual, field, jsonObject, templateALL);
            if (map_rules != null) {
                return map_rules;
            }
            /**
             * 以 | 分割 ，第一个字段
             * 检查类型，第三列
             * */
            Map<String, Object> map = checkActualTypes(actual, field, jsonObject);
            if (map != null) {
                return map;
            }
            /**
             *  检查是否有sql注入
             */

        }
        data.put("success", true);
        return data;
    }

    /**
     * 数据规则检查.数据正常返回null，错误返回map 信息
     *
     * @param actual         实际参数
     * @param field          字段
     * @param field_template 字段模板
     * @return
     */
    private Map<String, Object> checkActualRules(Map<String, Object> actual, String field, JSONObject field_template, JSONObject template_all) {
        //以|分割第二个字段
        String rule = field_template.getString("field_3");
        if (StringUtils.isEmpty(rule)) {
            return getResult(false, field + " 规则字段 第3列不能为空");
        }
        String[] split = rule.split(",");
        if (split.length == 1) {
            return null;
        }
        rule = split[1];
        Object value = actual.get(field);
        //json 规则字段
        String limit = field_template.getString("field_4");
        switch (rule) {
            case "must"://不能为空
                //判断字段是否为空
                Map<String, Object> must = isEmptyField(actual, field);
                if (must != null) {
                    return must;
                }
                break;
            case "update_exists":
                //判断字段是否为空
                Map<String, Object> update_exists = isUpdateEmptyField(actual, field, field_template);

                //如果是空就不更新
                if (update_exists != null) {
                    return update_exists;
                }
                if (!isJSONObject(limit)) {
                    return getResult(false, field + "：是否存在字段定义有误，必须为JSON");
                }

                List<Map<String, Object>> update_existsResult = getRulesResult(actual, field_template, field, template_all);
                if (update_existsResult == null) {
                    return getResult(false, "只能用于记录更新，检查记录是否存在");
                }
                if (update_existsResult.size() <= 0) {
                    return getResult(false, "记录不存在");
                } else if (update_existsResult.size() > 1) {
                    return getResult(false, "记录查询出 :" + update_existsResult.size() + " 条。只能一条");
                } else {

                }


                break;
            case "exists"://必传
                //判断字段是否为空
                Map<String, Object> existsEmptyField = isEmptyField(actual, field);

                //如果是空就不更新
                if (existsEmptyField != null) {
                    return existsEmptyField;
                }
                if (!isJSONObject(limit)) {
                    return getResult(false, field + "：是否存在字段定义有误，必须为JSON");
                }

                List<Map<String, Object>> rulesResult = getRulesResult(actual, field_template, field, template_all);
                if (rulesResult == null) {
                    return getResult(false, "只能用于记录更新，检查记录是否存在");
                }
                if (rulesResult.size() <= 0) {
                    return getResult(false, "记录不存在");
                } else if (rulesResult.size() > 1) {
                    return getResult(false, "记录查询出 :" + rulesResult.size() + " 条。只能一条");
                } else {

                }

                break;
            case "unique"://必须唯一
                //判断字段是否为空
                Map<String, Object> emptyField = isEmptyField(actual, field);
                if (emptyField != null) {
                    return emptyField;
                }
                if (!isJSONObject(limit)) {
                    return getResult(false, field + " :是否存在字段定义有误，必须为JSON");
                }

                List<Map<String, Object>> uniqueResult = getRulesResult(actual, field_template, field, template_all);
                if (uniqueResult.size() > 0) {
                    return getResult(false, field_template.getString("field_2") + " ：字段已经存在 " + value);
                }

                break;

            case "update_unique"://除本条记录唯一
                //判断字段是否为空
                Map<String, Object> uniqueMap = isUpdateEmptyField(actual, field, field_template);

                if (uniqueMap != null) {
                    return uniqueMap;

                }

                if (!isJSONObject(limit)) {
                    return getResult(false, field + "：是否存在字段定义有误，必须为JSON");
                }

                //查询记录
                List<Map<String, Object>> uniqueMapResult = getRulesResult(actual, field_template, field, template_all);
                if (uniqueMapResult == null) {
                    return getResult(false, "只能用于记录更新，检查记录是否存在");
                }
                if (uniqueMapResult.size() > 1) {
                    return getResult(false, field_template.getString("field_2") + " ：字段已经存在 " + value);
                } else if (uniqueMapResult.size() == 1) {
                    //和之前查询的记录作比较，必须有records
                    Map<String, Object> map = uniqueMapResult.get(0);
                    List<Map<String, Object>> records = (List<Map<String, Object>>) template_all.get("records");
                    if (records == null) {
                        return getResult(false, "未保存记录");
                    }
                    for (int i = 0; i < records.size(); i++) {
                        if (!map.containsKey(field)) {
                            return getResult(false, field_template.getString("field_2") + " ：字段 服务未配置 " + field + " 字段");
                        }
                        if (!map.get(field).equals(records.get(i).get(field))) {
                            return getResult(false, field_template.getString("field_2") + " ：字段已经存在 " + value);
                        }
                    }
                }


                break;
        }
        return null;
    }


    /**
     * 检查实际传参字段类型是否正确
     *
     * @param actual         实际数据
     * @param field          字段名称
     * @param field_template 自动段模板
     * @return 检查错误对象的信息
     */
    private Map<String, Object> checkActualTypes(Map<String, Object> actual, String field, JSONObject field_template) {
        //以|分割第第一个字段
        String type = field_template.getString("field_3");
        if (StringUtils.isEmpty(type)) {
            return getResult(false, field + " 规则字段 第3列不能为空");
        }
        type = type.split(",")[0];
        switch (type) {
            case "S":
                break;
            case "I":
                Object o = actual.get(field);
                //判断字段是否为空
                Map<String, Object> emptyField = isEmptyField(actual, field);
                //如果有值就检查
                if (emptyField == null) {
                    try {
                        Integer.parseInt(o + "");
                    } catch (Exception ex) {
                        return getResult(false, field + "：必须是整数");

                    }

                }

                break;

            case "D":
                Object d = actual.get(field);
                //判断字段是否为空
                Map<String, Object> dEmptyField = isEmptyField(actual, field);
                //如果有值就检查
                if (dEmptyField == null) {
                    try {
                        Double.parseDouble(d + "");

                    } catch (Exception ex) {
                        return getResult(false, field + "：必须是浮点数");

                    }

                }
                break;
        }
        return null;
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


    /**
     * 第四列规则检查，json字符串
     *
     * @param actual
     * @param field_template
     * @param field
     * @param template_all
     * @return
     */
    private List<Map<String, Object>> getRulesResult(Map<String, Object> actual, JSONObject field_template, String field, JSONObject template_all) {
        String limit_exists = field_template.getString("field_4");
        Object value_exists = actual.get(field);

        JSONObject limit = JSON.parseObject(limit_exists);
        if (limit == null) {
            return new ArrayList<>();
        }
        JSONObject deleteFieldMode = limit.getJSONObject("delete_field_mode");
        JSONObject delete_service_model = limit.getJSONObject("delete_service_mode");

        //字段模式
        if (deleteFieldMode != null) {
            Iterator<String> iterator = deleteFieldMode.keySet().iterator();
            List<Object> objects = new ArrayList<>();
            String sql = "select * from " + template_all.getString("table_name") + " where 1=1 ";
            while (iterator.hasNext()) {//获取伪删除字段
                String key = iterator.next();
                String valueOf = deleteFieldMode.getString(key);
                sql += " and " + key + " = ?";
                switch (valueOf) {
                    case "$user_id$":
                        objects.add(UserManager.getCurrentPkUser());
                        break;
                    case "$now$":
                        objects.add(value_exists);
                        break;
                    default:
                        //如果是规则数据，从实际传参中取值
                        String name = isRulesPatternValue(valueOf);
                        if (isRulesPatternValue(valueOf) != null) {
                            Object actual_value = actual.get(name);
                            if (actual_value == null) {
                                List<Map<String, Object>> records = (List<Map<String, Object>>) template_all.get("records");
                                if (records == null) {
                                    return null;
                                }
                                Map<String, Object> map = records.get(0);
                                actual_value = map.get(name);
                            }
                            objects.add(actual_value);
                        } else {
                            objects.add(valueOf);
                        }

                        break;
                }

            }
            if (EnvUtils.isDebugger()) {
                System.out.println(sql);
                System.out.println(objects);
            }
            //检查数据库是否存在记录
            List<Map<String, Object>> maps = DatabaseConnections.getReadTemplate().queryForList(sql, objects.toArray());
            return maps;
        } else if (delete_service_model != null) {//service 模式
            JSONObject search_fields = delete_service_model.getJSONObject("search_fields");
            Set<Map.Entry<String, Object>> entries = search_fields.entrySet();
            Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
            //获取参数
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                String value = (String) next.getValue();
                switch (value) {//获取当前值
                    case "$user_id$":
                        next.setValue(UserManager.getCurrentPkUser());
                        break;
                    case "$now$":
                        next.setValue(value_exists);
                        break;
                    default:
                        //获取其他属性
                        //如果是规则数据，从实际传参中取值
                        String name = isRulesPatternValue(value);
                        if (name != null) {
                            Object actual_value = actual.get(name);
                            if (actual_value == null) {
                                List<Map<String, Object>> records = (List<Map<String, Object>>) template_all.get("records");
                                if (records == null) {
                                    return null;
                                }
                                Map<String, Object> map = records.get(0);
                                actual_value = map.get(name);
                            }
                            next.setValue(actual_value);
                        } else {
                            next.setValue(value);
                        }
                        break;
                }
            }
            Map actual_convert = delete_service_model;
            Result result = SpringUtils.getBean(ServicePlatformQuery.ID);
            Map result_service = result.result(actual_convert);
            return (List<Map<String, Object>>) result_service.get("data");

        }


        return new ArrayList<>();


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


    private void serviceLog(String msg) {
        PlatformTemplate finalTemplate = getFinalTemplate();

        finalTemplate.log("=======================" + msg + "========================");
    }

    private void serviceDetailLog(String name, String msg) {
        PlatformTemplate finalTemplate = getFinalTemplate();
        finalTemplate.log("【" + name + "】" + msg);
    }

    /**
     * 检查和替换参数
     *
     * @param actual        前台传的参数
     * @param finalTemplate 后台模板
     * @param is_http       是否是http,内部调用
     * @return
     */
    @Override
    public Map<String, Object> check(Map<String, Object> actual, PlatformTemplate finalTemplate, boolean is_http) {

        this.serviceLog("检查数据开始");
        TemplateItemArr checks = finalTemplate.getChecks();
        for (int i = 0; i < checks.size(); i++) {

            TemplateItemObj templateItemObj = checks.getTemplateItemObj(i);
            String check_rule_bean = (String) templateItemObj.getField_5();
//            finalTemplate.log("【" + check_rule_bean + "】");
            this.serviceDetailLog(check_rule_bean, "正在检查数据");
            PlatformCheck checkBean = BaseCheck.getCheckBean(check_rule_bean);
            if (checkBean == null) {

                String msg = "检查数据对象：【" + check_rule_bean + "】不存在,请核对配置";
                finalTemplate.log(msg);
                return ResultUtils.getResultMap(false, msg);
            }
            Map<String, Object> checkResult = checkBean.check(actual, finalTemplate, is_http);
            //如果失敗了，直接返回
            if (!ResultUtils.isSuccess(checkResult)) {

                return checkResult;
            }


        }

        this.serviceLog("检查数据结束");
        return ResultUtils.getResultMap(true, "检查成功");

    }

    @Override
    public Map<String, Object> prepare(Map<String, Object> actual) {
        this.serviceLog("整合数据开始");
        PlatformTemplate finalTemplate = getFinalTemplate();
        TemplateItemArr filters = finalTemplate.getFilters();
        for (int i = 0; i < filters.size(); i++) {
            TemplateItemObj templateItemObj = filters.getTemplateItemObj(i);
            String filter_rule_bean = (String) templateItemObj.getField_5();
            this.serviceDetailLog(filter_rule_bean, "正在整合数据");
            PlatformFilter filterBean = BaseFilter.getFilterBean(filter_rule_bean);
            if (filterBean == null) {
                String msg = "整合数据对象：" + filter_rule_bean + "不存在，请核对配置";
                finalTemplate.log(msg);
                return ResultUtils.getResultMap(false, msg);
            }
            Map<String, Object> filter_result = filterBean.filter(actual, finalTemplate);
            if (!ResultUtils.isSuccess(filter_result)){
                return filter_result;
            }

        }
        this.serviceLog("整合数据结束");

//        updateFields((Map<String, Object>) actual.get("fields"), getFinalTemplate());
        return getResult(true, "处理成功");
    }

    @Override
    public void setFileList(List<MultipartFile> files) {
        this.files = files;
    }

    @Override
    public List<MultipartFile> getFileList() {
        return this.files;
    }

    @Override
    public long getCount() {
        String date = DateTimeUtil.getDate();
        if (!date.equals(day)) {
            day = date;
            count = 0;
        } else {

        }
        count += 1;

        return count;
    }


    @Override
    public Map<String, Object> finish(JSONObject final_template) {
        return getResult(true, "执行周期：结束成功");
    }

    /**
     * 将property 转成字典{field_1:field_5}
     *
     * @param final_template
     */
    public Map<String, Object> getDictFields(JSONObject final_template, String field) {
        JSONArray property = final_template.getJSONArray(field);
        Map<String, Object> propertyMap = new HashMap<>();
        for (int i = 0; i < property.size(); i++) {
            JSONObject jsonObject = property.getJSONObject(i);
            propertyMap.put(jsonObject.getString("field_1"), jsonObject.getString("field_5"));
        }
        return propertyMap;

    }

    /**
     * @param actual
     * @param template
     * @return 更新字段后的模板字符串
     */

    public JSONObject updateFields(Map<String, Object> actual, JSONObject template) {
        //获取可更改字段
        JSONArray fields = template.getJSONArray("fields");
        if (fields == null) {
            fields = template.getJSONArray("search_fields");
        }
        //获取默认字段
        JSONArray default_fields = template.getJSONArray("default_fields");
        //将更在字段加入默认字段
        for (int i = 0; i < fields.size(); i++) {
            JSONObject jsonObject = fields.getJSONObject(i);
            String field = jsonObject.getString("field_1");

            if (actual != null && actual.containsKey(field)) {

                //如果类型是字符串，就可以为空，否则不会更新
                String type = (String) jsonObject.get("field_3");
                if (!"S".equals(type)) {
                    Map<String, Object> emptyField = isEmptyField(actual, field);
                    if (emptyField != null) {
                        continue;
                    }
                }
                //修改数据
                jsonObject.put("field_5", actual.get(field));
                JSONObject defaultField = getDefaultField(default_fields, field);

                //没有就添加新字段,否则就修改
                if (defaultField == null) {
                    default_fields.add(jsonObject);
                } else {
                    defaultField.put("field_5", actual.get(field));
                }

            }
        }

        for (int i = 0; i < default_fields.size(); i++) {
            JSONObject object = default_fields.getJSONObject(i);
            Object rulesValue = getRulesValue(object, actual);
            //填充数据
            if (rulesValue != null) {
                object.put("field_5", rulesValue);
            }
        }
        return template;
    }
}
