package com.platform.insight.service_platform_query_simple;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.platform.insight.data_source.DatabaseConnections;
import com.platform.insight.factory.database.DatabaseFactory;
import com.platform.insight.service_platform_query.ServicePlatformQuery;
import com.platform.insight.utils.DatabaseUtils;
import com.platform.insight.utils.EnvUtils;
import com.platform.insight.utils.StringJoinUtils;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service(ServicePlatformQuerySimple.ID)
public class ServicePlatformQuerySimple  extends ServicePlatformQuery {
    public static final String ID = "service_platform_query_simple";

    @Override
    public Map<String, Object> result(Map<String, Object> actual) {

        //获取数据模板
        JSONObject final_template = getFinalTemplate((String) actual.get("service"));
        //获取数据库工厂
        Map<String, Object> databaseFactoryInfo = getDatabaseFactory();
        //检查是否支持，改数据库类型
        boolean success = (boolean) databaseFactoryInfo.get("success");
        if(!success){
            return databaseFactoryInfo;
        }
        //获取对应的数据库工厂
        DatabaseFactory databaseFactory = (DatabaseFactory) databaseFactoryInfo.get("data");

        Map<String,Object> result = new HashMap<>();


        //检查类型
        if(!actual.containsKey("search_fields")){
            actual.put("search_fields",new HashMap<>());
        }

        //
        Map<String, Object> map = checkFields((Map<String, Object>) actual.get("search_fields"), final_template.getJSONArray("search_fields"), final_template);


        if(!(boolean)map.get("success")){
            return map;
        }

        //todo 检查显示字段配置
        //todo 检查默认搜索字段
        //更新搜索条件，并且加上默认条件

        updateSearchFields((Map<String, Object>) actual.get("search_fields"), final_template);
        //获取sql 对象
        Map<String, Object> sqlObject = databaseFactory.getSQLObject(final_template);
        //获取最终 sql
        Map<String, Object> sqlBuilder = databaseFactory.buildSQL(sqlObject);
        //如果是预览sql
        String preview = final_template.getString("preview");
        if("true".equals(preview)){
            String msg = "查询成功";
            sqlBuilder.put("msg",msg);
            sqlBuilder.put("success",true);
            return sqlBuilder;


        }
        //查询结果
        result = databaseFactory.getSQLResult(sqlBuilder);

        if(canTree(final_template)){
            List<Map<String,Object>> data = (List<Map<String, Object>>) result.get("data");
            String[] toTrees = final_template.getString("toTree").split(",");
            String children = "children";
            if(toTrees.length>=3){
                children = toTrees[2];
            }
            List<Map<String, Object>> maps = toTree(data, toTrees[0], toTrees[1],children);
            result.put("data",maps);
        }

        result.put("fields", getFields(final_template)); //获取显示字段
        String msg = "查询成功";
        result.put("msg",msg);
        return result;
    }

    @Override
    public List<Map<String, Object>> getFields(JSONObject template) {
        JSONArray show_fields = template.getJSONArray("show_fields");
        List<Map<String,Object>> fields = new ArrayList<>();
        for(int i = 0;i<show_fields.size();i++){
            JSONObject jsonObject = show_fields.getJSONObject(i);
            Map<String,Object> item= new HashMap<>();
            item.put("field",jsonObject.getString("field_1"));
            item.put("desc",jsonObject.getString("field_2"));
            String field_3 = jsonObject.getString("field_3");
            if(StringUtils.isEmpty(field_3)){
                item.put("show",new JSONObject());
            }else{
                item.put("show",JSON.parseObject(field_3));
            }
            fields.add(item);
        }
        return fields;
    }

    @Override
    public Map<String, Object> queryResult(JSONObject template) {
        JdbcTemplate readTemplate = DatabaseConnections.getReadTemplate();
        String table = template.getString("table");
        //添加显示字段
        JSONArray search_fields = template.getJSONArray("search_fields");
        JSONArray show_fields = template.getJSONArray("show_fields");
        JSONArray order_fields = template.getJSONArray("order_fields");
        String count_id= template.getString("count_id");
        if(show_fields.size()<=0){
            return null;
        }
        //转换成sql

        Map<String, Object> sql_map = getSQL(table, search_fields, show_fields,order_fields,count_id);
        String data_sql = (String) sql_map.get("data_sql");
        String count_sql = (String) sql_map.get("count_sql");
        List<Object> sql_data = (List<Object>) sql_map.get("sql_data");
        List<Map<String, Object>> maps = null;
        long count = 0;
        //执行语句

        if(sql_data.size()>0){
            maps = readTemplate.queryForList(data_sql,sql_data.toArray());
            count = readTemplate.queryForObject(count_sql,sql_data.toArray(),Long.class);

        }else{
            maps =readTemplate.queryForList(data_sql);
            count = readTemplate.queryForObject(count_sql,Long.class);
        }

        Map<String,Object> data = new HashMap<>();
        String toTree = template.getString("toTree");
        data.put("data",maps);
        data.put("count",count);
        data.put("success",true);
        return data;
    }


    private Map<String,Object> getSQL(String table,JSONArray search_fields,JSONArray show_fields,JSONArray order_fields,String count_id){

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
        for(int i =0;i<show_fields.size();i++){
            JSONObject field = show_fields.getJSONObject(i);

            JSONObject field_4 = JSON.parseObject(field.getString("field_4"));
            String field_1 = field.getString("field_1");


            //作为子查询的关联字段,将显示字段替换为，关联字段。子查询
            if(field_4.containsKey("join_field") && field_4.containsKey("join_sql")){ //如果是包含join 的sql 主查询的字段替换为关联字段
                String join_field = field_4.getString("join_field");
                if(!sub_show_fields_list.contains(join_field)){
                    sub_show_fields_list.add(join_field);
                }

                String join_sql = field_4.getString("join_sql");
                String tmp = join_sql.replace("\n", "")
                        .replace(" ", "")
                        .replace("\t", "")
                        .toLowerCase();

                //检查是否已经存在过，sql 将调整为子查询
                if(!select_join_sql_set.contains(tmp)){
                    //子查询
                    select_join_sql_set.add(tmp);
                    select_join_sql_list.add(join_sql);
                }

            }else{


                if(field_4.containsKey("origin_field")){
                    String origin_field = field_4.getString("origin_field");
                    sub_show_fields_list.add(origin_field+" as "+field_1);
                }else{
                    sub_show_fields_list.add(field_1);
                }


            }
            //字段显示,主查询

            if(field_4.containsKey("origin_field") ){
                String origin_field = field_4.getString("origin_field");
                if(DatabaseUtils.isOracle()){
                    field_1="\""+field_1+"\"";
                }
                show_fields_list.add(origin_field+" as "+field_1);

            }else{
                if(DatabaseUtils.isOracle()){
                    field_1 +=" as  \""+field_1+"\"";
                }
                show_fields_list.add(field_1);
            }

        }


        int page = 1;
        int size = 10;
        //处理where搜索条件
        for(int i =0;i<search_fields.size();i++) {
            JSONObject o = (JSONObject) search_fields.get(i);
            String name = o.getString("field_1");
            Object value = o.getString("field_5");
            if (StringUtils.isEmpty(value)) {
                continue;
            }

            if ("page".equals(name)) {
                page = Integer.parseInt(value + "");
                if (page <= 0) {
                    page = 1;
                }
            } else if ("size".equals(name)) {
                size = Integer.parseInt(value + "");
            } else {

                JSONObject field_4 = JSON.parseObject(o.getString("field_4"));
                String operation = field_4.getString("operation");
                if(field_4.containsKey("origin_field")){
                    name = field_4.getString("origin_field");
                }
                String where = "";
                String type = o.getString("field_3");
                if (!StringUtils.isEmpty(type) && type == "I") {
                    value = Integer.parseInt(value + "");
                }
                //根据操作修改字段条件
                switch (operation) {
                    case "=":
                        where = name + " " + operation + " ? ";
                        data.add(value);
                        break;
                    case "like":
                        where = name + " " + operation + " ? ";
                        data.add("%" + value + "%");
                        break;
                    case "not in":
                    case "in":

                        List<String> params = Arrays.asList(value.toString().split(",")).stream().map(item -> "?").collect(Collectors.toList());
                        where =  name + " " + operation + " (" + StringJoinUtils.join(params, ",") + ")";
                        data.addAll(Arrays.asList(value.toString().split(",")));
                        break;

//                        List<String> params_not_in = Arrays.asList(value.toString().split(",")).stream().map(item -> "?").collect(Collectors.toList());
//                        where = name + " " + operation + " (" + StringJoinUtils.join(params_not_in, ",") + ")";
//                        data.addAll(Arrays.asList(value.toString().split(",")));
//                        break;
                    case "between_and":
                        where = " ( " + name + " between ? and ? ) ";
                        String[] between_and = value.toString().split(",");
                        data.add(between_and[0]);
                        if (between_and.length <= 1) {
                            data.add(between_and[0]);
                        } else {
                            data.add(between_and[1]);
                        }
                        break;
                    case "date_between_and":
                        where = " ( " + name + " between ? and ? ) ";
                        String[] date_between_and = value.toString().split(",");
                        data.add(date_between_and[0] + " 00:00:00");
                        if (date_between_and.length <= 1) {
                            data.add(date_between_and[0] + " 00:00:00");
                        } else {
                            data.add(date_between_and[1] + " 23:59:59");
                        }
                        break;
                    default:
                        where = " " + name + " " + operation + " ? ";
                        data.add(value);
                        break;

                }
                where_sql_list.add(where);

                if(field_4.containsKey("join_sql")){
                    String join_sql = field_4.getString("join_sql");
                    String tmp = join_sql.replace("\n", "")
                            .replace(" ", "")
                            .replace("\t", "")
                            .toLowerCase();

                    //添加到数据sql
                    if(!where_join_sql_set.contains(tmp)){
                        where_join_sql_set.add(tmp);
                        where_join_sql_list.add(join_sql);
                    }
                }



            }


        }
        List<String> orderBy = new ArrayList<>();

        //处理排序
        for(int i =0;i<order_fields.size();i++){
            JSONObject order = order_fields.getJSONObject(i);
            String field = order.getString("field_1");
            String value = order.getString("field_5");
            if(!StringUtils.isEmpty(field) && !StringUtils.isEmpty(value)){
                orderBy.add(" "+field+" "+value+" ");
            }
        }


        String countSQL = getCountSQL(count_id, table, where_join_sql_list, where_sql_list);
        String selectSQL = getSelectSQL(show_fields_list,sub_show_fields_list, table, select_join_sql_list, where_join_sql_list, where_sql_list, orderBy, page, size);

        Map<String,Object> map = new HashMap<>();
        map.put("data_sql",selectSQL);
        map.put("count_sql",countSQL);
        map.put("sql_data",data);
        if (EnvUtils.isDebugger()){
            System.out.println(selectSQL);
            System.out.println(countSQL);
            System.out.println(data);

        }
        return map;

    }


    private String getCountSQL(String count_id,String table,List<String> where_join_sql_list,List<String> where_sql_list){
        StringBuilder count_sb = new StringBuilder();
        count_sb.append("select count("+count_id+")");
        count_sb.append(" from "+table+" ");
        count_sb.append(" "+StringJoinUtils.join(where_join_sql_list," "));

        if(where_sql_list.size()>0){
            count_sb.append(" where ");
            count_sb.append(StringJoinUtils.join(where_sql_list," and "));
        }
        return count_sb.toString();
    }

    private String getMySQLSelectSQL(List<String> show_fields_list,
                                     List<String> sub_show_fields_list,
                                     String table,
                                     List<String> select_join_sql_list,
                                     List<String> where_join_sql_list,
                                     List<String> where_sql_list,

                                     List<String> orderBy,
                                     int page,
                                     int size){

        StringBuilder sb = new StringBuilder();
        sb.append("select "+StringJoinUtils.join(sub_show_fields_list,",")+" ");
        sb.append(" from "+table+" ");
        sb.append(" "+StringJoinUtils.join(where_join_sql_list," "));
        if(where_sql_list.size()>0){
            sb.append(" where ");
            sb.append(StringJoinUtils.join(where_sql_list," and "));
        }
        int start = (page-1)*size;
        if(orderBy.size()>0){
            sb.append(" order by ");
            sb.append(" "+StringJoinUtils.join(orderBy,", "));
        }


        sb.append(" limit "+start+" , "+size);


        if(select_join_sql_list.size()>0){
            StringBuilder mainSb = new StringBuilder();
            mainSb.append("select "+StringJoinUtils.join(show_fields_list,",")+" ");
            mainSb.append(" from ( "+sb.toString()+" ) as "+table );
            mainSb.append(" "+StringJoinUtils.join(select_join_sql_list," "));
            return mainSb.toString();
        }else {
            return sb.toString();
        }

    }


    private String getOracleSelectSQL(List<String> show_fields_list,
                                     List<String> sub_show_fields_list,
                                     String table,
                                     List<String> select_join_sql_list,
                                     List<String> where_join_sql_list,
                                     List<String> where_sql_list,

                                     List<String> orderBy,
                                     int page,
                                     int size){


//        sub_show_fields_list.add("ROWNUM as rn");
        StringBuilder sb = new StringBuilder();
        String select_fields = StringJoinUtils.join(sub_show_fields_list,",");
        sb.append("select "+select_fields+" ");
        sb.append(" from "+table+" ");
        sb.append(" "+StringJoinUtils.join(where_join_sql_list," "));
        int start = (page-1)*size;
        int end = page*size;

        if(where_sql_list.size()>0){
            sb.append(" where ");
            sb.append(StringJoinUtils.join(where_sql_list," and "));
        }



        if(orderBy.size()>0){
            sb.append(" order by ");
            sb.append(" "+StringJoinUtils.join(orderBy,", "));
        }

        StringBuilder tmp = new StringBuilder("select ROWNUM as rn ,"+select_fields);
        tmp.append(" from ( ");
        tmp.append(sb);
        tmp.append(" ) "+table+" ");
        tmp.append(" where ROWNUM <= "+end);

        sb = tmp;

        String main_field = StringJoinUtils.join(show_fields_list,",");
        StringBuilder finalStringBuild = null;
        if(select_join_sql_list.size()>0){

            StringBuilder secondSb = new StringBuilder();
            secondSb.append("select "+select_fields+" from ");
            secondSb.append(" ( "+sb.toString()+" ) "+table);
            secondSb.append(" where rn >"+start);
            StringBuilder mainSb = new StringBuilder();

            mainSb.append("select "+main_field+" ");
            mainSb.append(" from ( "+secondSb.toString()+" )  "+table );
            mainSb.append(" "+StringJoinUtils.join(select_join_sql_list," "));
            finalStringBuild =  mainSb;
        }else {
            StringBuilder secondSb = new StringBuilder();
            secondSb.append("select "+main_field+" from ");
            secondSb.append(" ( "+sb.toString()+" ) "+table);
            secondSb.append(" where rn >"+start);
            finalStringBuild =  secondSb;
        }


        return finalStringBuild.toString();

    }
    private String getSelectSQL( List<String> show_fields_list,
                                 List<String> sub_show_fields_list,
                                 String table,
                                 List<String> select_join_sql_list,
                                 List<String> where_join_sql_list,
                                 List<String> where_sql_list,

                                 List<String> orderBy,
                                 int page,
                                 int size
                                 ){


        if(DatabaseUtils.isMySQL()){
            return  getMySQLSelectSQL(show_fields_list,sub_show_fields_list, table, select_join_sql_list, where_join_sql_list, where_sql_list, orderBy, page, size);
        }else if(DatabaseUtils.isOracle()){
            return  getOracleSelectSQL(show_fields_list,sub_show_fields_list, table, select_join_sql_list, where_join_sql_list, where_sql_list, orderBy, page, size);
        }
        return null;
    }

}
