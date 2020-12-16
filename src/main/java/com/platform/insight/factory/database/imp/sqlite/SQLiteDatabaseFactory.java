package com.platform.insight.factory.database.imp.sqlite;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.platform.insight.data_source.DatabaseConnections;
import com.platform.insight.factory.database.DatabaseFactory;
import com.platform.insight.utils.StringJoinUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(SQLiteDatabaseFactory.ID)
public class SQLiteDatabaseFactory extends DatabaseFactory {
    public static final String ID = "org.sqlite.JDBC";
    @Override
    public void changeUrl(JSONObject final_template) {
        String url = "jdbc:sqlite:"+final_template.getString("database");
        final_template.put("url",url);
    }

    @Override
    public boolean checkTableExists(JSONObject final_template) {

        String sql ="";
        String table_name = final_template.getString("table_name");
        sql = String.format("SELECT count(*) from sqlite_master where type='table' and name='%s'",table_name);
        JdbcTemplate readTemplate = DatabaseConnections.getReadTemplate();
        long count = readTemplate.queryForObject(sql,Long.class);
        if(count <=0){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void getFieldsDescFromDataBase(JSONObject final_template) {
        JdbcTemplate readTemplate = DatabaseConnections.getReadTemplate();
        String table_name = final_template.getString("table_name");
        String sql = String.format(" PRAGMA table_info('%s') ",table_name);
        List<Map<String, Object>> fields = readTemplate.queryForList(sql);
        Map<String,Object> data = new HashMap<>();
        for(int i= 0;i<fields.size();i++){
            Map<String, Object> field = fields.get(i);
            data.put(field.get("name")+"",field);
        }
        final_template.put("fields_from_database",data);
    }

    @Override
    public void getDifferentField(JSONObject final_template) {
        JSONArray fields = final_template.getJSONArray("fields");
        Map<String,Object> fields_from_database = (Map<String, Object>) final_template.get("fields_from_database");
        List<JSONObject> newFields = new ArrayList<>();
        List<JSONObject> updateFields = new ArrayList<>();
        for(int i=0;i<fields.size();i++){
            JSONObject field = fields.getJSONObject(i);
            String field_name = field.getString("field_1");//配置字段名称
            if(fields_from_database.containsKey(field_name)){//如果存在，检查是否有更新,不存在直接新增，不删除原有
                Map<String,Object> field_from_database = (Map<String, Object>) fields_from_database.get(field_name);

                    String type = (String) field_from_database.get("type");
                    String[] data_type = type.split("\\(");//数据库示例：类型varchar (255)
                    String field_3 = field.getString("field_3");//配置示例：varchar,255
                    String[] field_3_type = field_3.split(",");
                    //如果字段类型不同
                    String config_type = field_3_type[0].trim();//配置类型
                    String database_type = data_type[0].trim();//数据库类型
                    if(!database_type.equals(config_type)){//如果类型不一致，则修改
                        updateFields.add(field);
                        continue;
                    }
                    //长度不同
                    if(field_3_type.length>1){//如果配置了长度
                        //数据库存在长度
                        if(data_type.length>1){//比较长度，数据库里也有长度
                            String data_type_length = data_type[1].split("\\)")[0];
                            if(!data_type_length.equals(field_3_type[1])){//长度不同则修改
                                updateFields.add(field);
                            }
                        }else{//没有长度
                            updateFields.add(field);
                        }
                    }



            }else{//不存在，就添加
                newFields.add(field);
            }
        }

        final_template.put("newFields",newFields);
        final_template.put("updateFields",updateFields);
    }

    @Override
    public String getSelectSQL(Map<String, Object> sqlObj) {

        List<String> show_fields_list = (List<String>) sqlObj.get("show_fields_list");
        List<String> sub_show_fields_list = (List<String>) sqlObj.get("sub_show_fields_list");
        String table = (String) sqlObj.get("table");
        List<String> select_join_sql_list = (List<String>) sqlObj.get("select_join_sql_list");
        List<String> where_join_sql_list = (List<String>) sqlObj.get("where_join_sql_list");
        List<String> where_sql_list = (List<String>) sqlObj.get("where_sql_list");

        List<String> orderBy = (List<String>) sqlObj.get("orderBy");
        int page = (int) sqlObj.get("page");
        int size = (int) sqlObj.get("size");


        StringBuilder sb = new StringBuilder();
        sb.append("select "+ StringJoinUtils.join(sub_show_fields_list,",")+" ");
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
}
