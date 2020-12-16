package com.platform.insight.factory.database.imp.mysql;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.platform.insight.data_source.DatabaseConnections;
import com.platform.insight.factory.database.DatabaseFactory;
import com.platform.insight.utils.DatabaseUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(MysqlDatabaseFactory.ID)
public class MysqlDatabaseFactory extends DatabaseFactory {
    public static final String ID = "com.mysql.jdbc.Driver";

    @Override
    public void changeUrl(JSONObject final_template) {
        String url = "jdbc:mysql://"+final_template.getString("host")+":3306/"+final_template.getString("database")+"?useUnicode=true&characterEncoding=UTF8";
        final_template.put("url",url);
    }

    @Override
    public boolean checkTableExists(JSONObject final_template) {
        JdbcTemplate readTemplate = DatabaseConnections.getReadTemplate();
        String sql ="";
        String table_name = final_template.getString("table_name");
        sql = "show tables like '" + table_name + "'; ";
        List<String> table_List = readTemplate.queryForList(sql,String.class);
        if(table_List.size()<=0){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void getFieldsDescFromDataBase(JSONObject final_template) {
        JdbcTemplate readTemplate = DatabaseConnections.getReadTemplate();
        String sql ="";
        String table_name = final_template.getString("table_name");
        sql = "desc "+table_name;
        List<Map<String, Object>> fields = readTemplate.queryForList(sql);
        Map<String,Object> data = new HashMap<>();
        for(int i= 0;i<fields.size();i++){
            Map<String, Object> field = fields.get(i);
            data.put(field.get("Field")+"",field);
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
            String field_name = field.getString("field_1");

            if(DatabaseUtils.isOracle()){
                field_name = field_name.toUpperCase();
            }
            if(fields_from_database.containsKey(field_name)){//如果存在，检查是否有更新
                Map<String,Object> field_from_database = (Map<String, Object>) fields_from_database.get(field_name);
                if(DatabaseUtils.isMySQL()){
                    String type = (String) field_from_database.get("Type");
                    String[] data_type = type.split("\\(");
                    String field_3 = field.getString("field_3");
                    String[] field_3_type = field_3.split(",");
                    //如果字段名称不同
                    if(!data_type[0].equals(field_3_type[0])){
                        updateFields.add(field);
                        continue;
                    }
                    //长度不同
                    if(field_3_type.length>1){
                        //数据库存在长度
                        if(data_type.length>1){//比较长度
                            String data_type_length = data_type[1].split("\\)")[0];
                            if(!data_type_length.equals(field_3_type[1])){//长度不同则修改
                                updateFields.add(field);
                            }
                        }else{//todo 数据库里没有长度， 是否需要修改？
                            //updateFields.add(field);
                        }
                    }
                }else if(DatabaseUtils.isOracle()){
                    String data_type = (String) field_from_database.get("DATA_TYPE");


                    String field_3 = field.getString("field_3");
                    String[] field_3_type = field_3.split(",");
                    String config_data_type = field_3_type[0].toUpperCase();
                    //如果字段名称不同
                    if(!data_type.equals(config_data_type)){


                        if(data_type.equals("NUMBER") && config_data_type.toUpperCase().equals("INT")){ //number int 同义

                            //不做处理。不检查字段长度是变化
                            continue;
                        }else if(data_type.equals("VARCHAR2") && config_data_type.toUpperCase().equals("VARCHAR")){//varchar varchar2 同义
                            //不做处理。继续检查字段长度是变化
                        }else{
                            updateFields.add(field);
                            continue;
                        }

                    }
                    //配置了长度，并且长度不同
                    if(field_3_type.length>1){

                        //比较长度
                        String data_type_length =  field_from_database.get("DATA_LENGTH").toString();
                        if(!data_type_length.equals(field_3_type[1])){//长度不同则修改
                            updateFields.add(field);
                        }

                    }
                }

            }else{//不存在，就添加
                newFields.add(field);
            }
        }

        final_template.put("newFields",newFields);
        final_template.put("updateFields",updateFields);

        getDifferentEngine(final_template);
    }

    private void getDifferentEngine(JSONObject final_template){
        if(DatabaseUtils.isOracle()){
            return;
        }
        JdbcTemplate readTemplate = DatabaseConnections.getReadTemplate();
        String sql = "show table status where name = '" + final_template.getString("table_name") + "';  ";
        List<Map<String,Object>> list = readTemplate.queryForList(sql);
        Map<String, Object> map = list.get(0);
        String engine = (String) map.get("Engine");
        engine = engine.toLowerCase();
        String final_templateString = final_template.getString("engine");
        if(!StringUtils.isEmpty(final_templateString) && !engine.equals(final_templateString)){
            String engine_sql = "alter table operation_log ENGINE = '"+final_templateString+"';";
            final_template.put("engine_sql",engine_sql);

        }


    }

    @Override
    public Map<String,Object> getCreateTableSQL(JSONObject final_template){
        Map<String, Object> createTableSQL = super.getCreateTableSQL(final_template);
        boolean success = (boolean) createTableSQL.get("success");
        if(!success){
            return  createTableSQL;
        }
        String sql = (String) createTableSQL.get("sql");
        StringBuilder sb = new StringBuilder(sql);
        if(final_template.containsKey("engine")){
            String engine = final_template.getString("engine");
            sb.append(" engine="+engine);
        }
        createTableSQL.put("sql",sb.toString());
        return createTableSQL;
    }
    @Override
    public List<String> getAlterTableSQL(JSONObject final_template){
        List<String> alterTableSQL = super.getAlterTableSQL(final_template);
        //如果修改了引擎
        if(final_template.containsKey("engine_sql")){
            alterTableSQL.add(final_template.getString("engine_sql"));
        }
        return alterTableSQL;
    }

    @Override
    public String getSelectSQL(Map<String, Object> sqlObj) {

//        StringBuilder sb = new StringBuilder();
//        sb.append("select "+ StringJoinUtils.join(sub_show_fields_list,",")+" ");
//        sb.append(" from "+table+" ");
//        sb.append(" "+StringJoinUtils.join(where_join_sql_list," "));
//        if(where_sql_list.size()>0){
//            sb.append(" where ");
//            sb.append(StringJoinUtils.join(where_sql_list," and "));
//        }
//        int start = (page-1)*size;
//        if(orderBy.size()>0){
//            sb.append(" order by ");
//            sb.append(" "+StringJoinUtils.join(orderBy,", "));
//        }
//
//
//        sb.append(" limit "+start+" , "+size);
//
//
//        if(select_join_sql_list.size()>0){
//            StringBuilder mainSb = new StringBuilder();
//            mainSb.append("select "+StringJoinUtils.join(show_fields_list,",")+" ");
//            mainSb.append(" from ( "+sb.toString()+" ) as "+table );
//            mainSb.append(" "+StringJoinUtils.join(select_join_sql_list," "));
//            return mainSb.toString();
//        }else {
//            return sb.toString();
//        }
        return null;

    }


}
