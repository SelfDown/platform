package com.platform.insight.platform_tool.check;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.platform.insight.data_source.DatabaseConnections;
import com.platform.insight.service.Result;
import com.platform.insight.service_platform_query.ServicePlatformQuery;
import com.platform.insight.utils.*;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service(BaseCheck.ID)
public class BaseCheck extends ServiceUtils implements PlatformCheck {
    public static final String ID = "base_check";
    public static PlatformCheck getCheckBean(String check){
        return (PlatformCheck) getBeanByName(check);
    }

    @Override
    public Map<String, Object> check(Map<String, Object> actual, JSONObject final_template, boolean is_http) {

        String login_require = final_template.getString(Const.LOGIN_REQUIRE);
        //是否需要登陆,为false 不要登陆
        if (!Const.FALSE.equals(login_require)) {
            String currentUser = UserManager.getCurrentPkUser();
            if (currentUser == null) {
                throw new UnauthenticatedException();
            }
        }

        String action_type = final_template.getString(Const.ACTION_TYPE);
        if (is_http && !StringUtils.isEmpty(action_type) && !Const.HTTP.equals(action_type)) {

            return getResult(false, "不支持 http 访问");
        }
        //默认检查fields 字段
        if (actual.get(Const.FIELDS) == null) {
            actual.put(Const.FIELDS, new HashMap<>());
        }
        //默认获取default_fields 字段
        JSONArray default_fields = final_template.getJSONArray(Const.DEFAULT_FIELDS);
        if (default_fields == null) {
            final_template.put(Const.DEFAULT_FIELDS, new JSONArray());
        }
        Map<String, Object> actual_fields = (Map<String, Object>) actual.get(Const.FIELDS);
        JSONArray template_fields = final_template.getJSONArray(Const.FIELDS);

        Map<String, Object> map = checkFields(actual_fields, template_fields, final_template);
        //检查状态是否成功
        String msg = null;
        if (!isSuccess(map)) {
            return map;
        }
        return getResult(true, "检查成功");
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

}
