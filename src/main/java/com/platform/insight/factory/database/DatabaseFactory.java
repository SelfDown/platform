package com.platform.insight.factory.database;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.platform.insight.data_source.DatabaseConnections;
import com.platform.insight.service.ResultBaseService;
import com.platform.insight.utils.EnvUtils;
import com.platform.insight.utils.StringJoinUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


public abstract class DatabaseFactory implements DatabaseOperation {


    /**
     * 获取fields 字段，将数组转成字典，{field_1:field_5}
     * 优先请求对象
     *
     * @param actual         请求对象
     * @param final_template 模板对象
     */

    public void changeField(Map<String, Object> actual, JSONObject final_template) {
        JSONArray fields = final_template.getJSONArray("fields");
        Map<String, Object> field_map = new HashMap<>();
        for (int i = 0; i < fields.size(); i++) {
            JSONObject fieldsJSONObject = fields.getJSONObject(i);
            String field_1 = fieldsJSONObject.getString("field_1");

            if (actual.containsKey(field_1)) {
                String value = (String) actual.get(field_1);
                if ("true".equals(value)) {
                    value = "true";
                } else {
                    value = "false";
                }
                field_map.put(field_1, value);
            } else {
                field_map.put(field_1, fieldsJSONObject.getString("field_5"));
            }
        }

        final_template.put("fields", field_map);
    }

    /**
     * 将property 转成字典{field_1:field_5}
     *
     * @param final_template
     */
    public void changeProperty(JSONObject final_template) {
        JSONArray property = final_template.getJSONArray("property");
        Map<String, Object> propertyMap = new HashMap<>();
        for (int i = 0; i < property.size(); i++) {
            JSONObject jsonObject = property.getJSONObject(i);
            propertyMap.put(jsonObject.getString("field_1"), jsonObject.getString("field_5"));
        }
        final_template.put("property", propertyMap);

    }

    @Override
    public List<String> getAlterTableSQL(JSONObject final_template) {
        List<String> list = new ArrayList<>();
        JSONArray newFields = final_template.getJSONArray("newFields");
        String table_name = final_template.getString("table_name");

        for (int i = 0; i < newFields.size(); i++) {
            JSONObject field = newFields.getJSONObject(i);
            StringBuilder sb = new StringBuilder();
            String field_1 = field.getString("field_1");


            sb.append("alter table `" + table_name + "` add  `");

            sb.append(field_1);
            sb.append("`");
            String[] field_type = field.getString("field_3").split(",");
            if (field_type.length > 1) {
                sb.append(" " + field_type[0] + "(" + field_type[1] + ")");
            } else {
                sb.append(" " + field_type[0] + " ");
            }
            sb.append(" ");
            list.add(sb.toString());
        }
        JSONArray updateFields = final_template.getJSONArray("updateFields");
        for (int i = 0; i < updateFields.size(); i++) {
            JSONObject field = updateFields.getJSONObject(i);
            StringBuilder sb = new StringBuilder();
            String field_1 = field.getString("field_1");

            sb.append("alter table " + table_name + " modify ");

            sb.append(field_1);
            String[] field_type = field.getString("field_3").split(",");
            if (field_type.length > 1) {
                sb.append(" " + field_type[0] + "(" + field_type[1] + ")");
            } else {
                sb.append(" " + field_type[0] + " ");
            }

            sb.append(" ");
            list.add(sb.toString());
        }


        if (EnvUtils.isDebugger()) {
            System.out.println(list);
        }
        return list;
    }

    public Map<String, Object> getCreateTableSQL(JSONObject final_template) {
        StringBuilder sb = new StringBuilder();
        sb.append("create table ");
        String table = final_template.getString("table_name");

        sb.append(" " + table + " ( ");
        JSONArray fields = final_template.getJSONArray("fields");

        for (int i = 0; i < fields.size(); i++) {

            JSONObject object = fields.getJSONObject(i);
            String field_1 = object.getString("field_1");

            sb.append(" " + field_1 + " ");


            String field_3 = object.getString("field_3");
            String[] fields_arr = field_3.split(",");
            if (fields_arr.length <= 0) {
                return ResultBaseService.getResult(false, field_1 + " 类型字段不能为空");
            }
            if (fields_arr.length == 1) {
                sb.append(fields_arr[0]);
            } else {
                sb.append(" " + fields_arr[0] + " " + "(" + fields_arr[1] + ") ");
            }

            if ("primary_key".equals(object.getString("field_4"))) {
                sb.append(" primary key ");
            }

            if (i != (fields.size() - 1)) {
                sb.append(" , ");
            }
        }
        sb.append(" ) ");


        if (EnvUtils.isDebugger()) {
            System.out.println(sb.toString());
        }
        Map<String, Object> data = new HashMap<>();
        data.put("sql", sb.toString());
        data.put("success", true);
        return data;
    }

    private boolean has_join_sql(String join_sql, Set<String> set) {
        //转成小写，放到集合里面，判断是否重复
        String tmp = join_sql.replace("\n", "")
                .replace(" ", "")
                .replace("\t", "")
                .toLowerCase();
        if (set.contains(tmp)) {
            return true;
        }
        set.add(tmp);
        return false;
    }

    private void changeWhereSQlToDataToWhere(String name, String operation, Object value, List<Object> data_list, List<String> where_sql_list) {
        //根据操作修改字段条件
        String where = "";
        switch (operation) {
            case "=":
                where = name + " " + operation + " ? ";
                data_list.add(value);
                break;
            case "like":
                where = name + " " + operation + " ? ";
                data_list.add("%" + value + "%");
                break;
            case "not in":
            case "in":

                List<String> params = Arrays.asList(value.toString().split(",")).stream().map(item -> "?").collect(Collectors.toList());
                where = name + " " + operation + " (" + StringJoinUtils.join(params, ",") + ")";
                data_list.addAll(Arrays.asList(value.toString().split(",")));
                break;

//                        List<String> params_not_in = Arrays.asList(value.toString().split(",")).stream().map(item -> "?").collect(Collectors.toList());
//                        where = name + " " + operation + " (" + StringJoinUtils.join(params_not_in, ",") + ")";
//                        data.addAll(Arrays.asList(value.toString().split(",")));
//                        break;
            case "between_and":
                where = " ( " + name + " between ? and ? ) ";
                String[] between_and = value.toString().split(",");
                data_list.add(between_and[0]);
                if (between_and.length <= 1) {
                    data_list.add(between_and[0]);
                } else {
                    data_list.add(between_and[1]);
                }
                break;
            case "date_between_and":
                where = " ( " + name + " between ? and ? ) ";
                String[] date_between_and = value.toString().split(",");
                data_list.add(date_between_and[0] + " 00:00:00");
                if (date_between_and.length <= 1) {
                    data_list.add(date_between_and[0] + " 00:00:00");
                } else {
                    data_list.add(date_between_and[1] + " 23:59:59");
                }
                break;
            default:
                where = " " + name + " " + operation + " ? ";
                data_list.add(value);
                break;

        }
        where_sql_list.add(where);
    }

    @Override
    public Map<String, Object> getSQLObject(JSONObject final_template) {

        String table = final_template.getString("table");
        table = String.format("`%s`", table);
        //添加显示字段
        JSONArray search_fields = final_template.getJSONArray("search_fields");
        JSONArray show_fields = final_template.getJSONArray("show_fields");
        JSONArray order_fields = final_template.getJSONArray("order_fields");
        String count_id = final_template.getString("count_id");
        count_id = String.format("`%s`", count_id);
        if (show_fields.size() <= 0) {
            return null;
        }


        //主显显示字段
        List<String> show_fields_list = new ArrayList<>();

        //子查询显示字段
        List<String> sub_show_fields_list = new ArrayList<>();
//        List<String> join_fields_list = new ArrayList<>();

        //搜索 条件 join 集合
        Set<String> where_join_sql_set = new HashSet<>();
        List<String> where_join_sql_list = new ArrayList<>();

        //字段 join 集合
        Set<String> select_join_sql_set = new HashSet<>();
        List<String> select_join_sql_list = new ArrayList<>();

        //where 条件
        List<String> where_sql_list = new ArrayList<>();
        List<Object> data = new ArrayList<>();

        //处理select筛选字段
        for (int i = 0; i < show_fields.size(); i++) {
            JSONObject field = show_fields.getJSONObject(i);

            JSONObject field_4 = JSON.parseObject(field.getString("field_4"));
            String field_1 = field.getString("field_1");
            //作为子查询的关联字段,将显示字段替换为，关联字段。子查询
            if (field_4.containsKey("join_field") && field_4.containsKey("join_sql")) { //如果是包含join 的sql 主查询的字段替换为关联字段
                //获取主表的关联字段，查询出来，以便，关联的时候存在该字段
                String join_field = field_4.getString("join_field");
                if (!sub_show_fields_list.contains(join_field)) {//如果已经存在，就不添加
                    sub_show_fields_list.add(String.format("`%s`", join_field));
                }

                String join_sql = field_4.getString("join_sql");
                boolean has_sql = has_join_sql(join_sql, select_join_sql_set);
                //检查是否已经存在过，sql 将调整为子查询
                if (!has_sql) {
                    //子查询
                    select_join_sql_list.add(join_sql);
                }

            } else {

                String sub_show_field = "";
                if (field_4.containsKey("origin_field")) {//将数据库字段重新命名
                    String origin_field = field_4.getString("origin_field");
                    sub_show_field = String.format("`%s` as `%s`", origin_field, field_1);

                } else {
                    sub_show_field = String.format("`%s` ", field_1);
                }
                sub_show_fields_list.add(sub_show_field);


            }
            //字段显示,主查询
            String main_show_field = "";
            if (field_4.containsKey("origin_field")) {
                String origin_field = field_4.getString("origin_field");
                main_show_field = String.format("`%s` as `%s`", origin_field, field_1);
            } else {
                main_show_field = String.format("`%s`", field_1);
            }
            show_fields_list.add(main_show_field);

        }


        int page = 1;
        int size = 10;
        //处理where搜索条件
        for (int i = 0; i < search_fields.size(); i++) {
            JSONObject o = (JSONObject) search_fields.get(i);
            String name = o.getString("field_1");
            Object value = o.getString("field_5");
            if (StringUtils.isEmpty(value)) {
                continue;
            }

            if ("page".equals(name)) {//处理分页
                page = Integer.parseInt(value + "");
                if (page <= 0) {
                    page = 1;
                }
            } else if ("size".equals(name)) {
                size = Integer.parseInt(value + "");
            } else {

                JSONObject field_4 = JSON.parseObject(o.getString("field_4"));
                String operation = field_4.getString("operation");
                if (field_4.containsKey("origin_field")) {
                    name = field_4.getString("origin_field");
                }
                String where = "";
                String type = o.getString("field_3");
                //todo 处理类型
                if (!StringUtils.isEmpty(type) && type == "I") {
                    value = Integer.parseInt(value + "");
                }
                //处理其他条件
                changeWhereSQlToDataToWhere(name, operation, value, data, where_sql_list);

                if (field_4.containsKey("join_sql")) {
                    String join_sql = field_4.getString("join_sql");
                    boolean has_sql = has_join_sql(join_sql, where_join_sql_set);
                    //添加到数据sql
                    if (!has_sql) {
                        where_join_sql_list.add(join_sql);
                    }
                }


            }


        }
        List<String> orderBy = new ArrayList<>();

        //处理排序
        for (int i = 0; i < order_fields.size(); i++) {
            JSONObject order = order_fields.getJSONObject(i);
            String field = order.getString("field_1");
            String value = order.getString("field_5");
            if (!StringUtils.isEmpty(field) && !StringUtils.isEmpty(value)) {
                orderBy.add(String.format(" %s %s", field, value));
            }
        }


//        String countSQL = getCountSQL(count_id, table, where_join_sql_list, where_sql_list);
//        String selectSQL = getSelectSQL(show_fields_list,sub_show_fields_list, table, select_join_sql_list, where_join_sql_list, where_sql_list, orderBy, page, size);

        Map<String, Object> map = new HashMap<>();
        //统计数据字段
        map.put("count_id", count_id);
        //主表
        map.put("table", table);
        //主表查询关联字段
        map.put("where_join_sql_list", where_join_sql_list);
        //主表条件字段
        map.put("where_sql_list", where_sql_list);
        //外层显示字段
        map.put("show_fields_list", show_fields_list);
        //里层显示字段
        map.put("sub_show_fields_list", sub_show_fields_list);
        //外层关联sql
        map.put("select_join_sql_list", select_join_sql_list);
        //排序
        map.put("orderBy", orderBy);
        map.put("page", page);
        map.put("size", size);
        //数据
        map.put("data", data);

//        map.put("sql_data",data);
        if (EnvUtils.isDebugger()) {
            System.out.println(map);
        }
        return map;

    }

    @Override
    public String getCountSQL(Map<String, Object> sqlObj) {
        String count_id = (String) sqlObj.get("count_id");
        List<String> where_join_sql_list = (List<String>) sqlObj.get("where_join_sql_list");
        List<String> where_sql_list = (List<String>) sqlObj.get("where_sql_list");
        StringBuilder count_sb = new StringBuilder();
        String table = (String) sqlObj.get("table");
        count_sb.append("select count(" + count_id + ") as `count`");
        count_sb.append(" from " + table + " ");
        count_sb.append(" " + StringJoinUtils.join(where_join_sql_list, " "));
        if (where_sql_list.size() > 0) {
            count_sb.append(" where ");
            count_sb.append(StringJoinUtils.join(where_sql_list, " and "));
        }
        return count_sb.toString();
    }

    @Override
    public Map<String, Object> buildSQL(Map<String, Object> sqlObj) {
        String countSQL = getCountSQL(sqlObj);
        String selectSQL = getSelectSQL(sqlObj);
        List<Object> data = (List<Object>) sqlObj.get("data");
        Map<String, Object> build = new HashMap<>();
        build.put("countSQL", countSQL);
        build.put("selectSQL", selectSQL);
        build.put("data", data);
        if (EnvUtils.isDebugger()) {
            System.out.println(countSQL);
            System.out.println(selectSQL);
            System.out.println(data);
        }
        return build;
    }

    @Override
    public Map<String, Object> getSQLResult(Map<String, Object> buildSQl) {

        String selectSql = (String) buildSQl.get("selectSQL");
        String countSql = (String) buildSQl.get("countSQL");
        List<Object> data = (List<Object>) buildSQl.get("data");
        //        JdbcTemplate readTemplate = DatabaseConnections.getReadTemplate();
//        List<Map<String, Object>> maps = null;
//        long count = 0;
//        //执行语句
//        if (data.size() > 0) {
//            maps = readTemplate.queryForList(selectSql, data.toArray());
//            count = readTemplate.queryForObject(countSql, data.toArray(), Long.class);
//
//        } else {
//            maps = readTemplate.queryForList(selectSql);
//            count = readTemplate.queryForObject(countSql, Long.class);
//        }
        List<Map<String, Object>> maps = getListResult(selectSql, data);
        Long count = getLongResult(countSql, data);
        Map<String, Object> result = new HashMap<>();
        result.put("data", maps);
        result.put("count", count);
        result.put("success", true);
        return result;
    }

    public List<Map<String, Object>> getListResult(String sql, List<Object> data) {
        JdbcTemplate readTemplate = DatabaseConnections.getReadTemplate();
        List<Map<String, Object>> result = null;
        if (data.size() > 0) {
            result = readTemplate.queryForList(sql, data.toArray());
        } else {
            result = readTemplate.queryForList(sql);
        }

        return result;
    }

    public Long getLongResult(String sql, List<Object> data) {
        JdbcTemplate readTemplate = DatabaseConnections.getReadTemplate();
        Long result = null;

        if (data.size() > 0) {
            result = readTemplate.queryForObject(sql, data.toArray(), Long.class);
        } else {
            result = readTemplate.queryForObject(sql, Long.class);
        }
        return result;
    }

}
