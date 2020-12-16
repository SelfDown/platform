package com.platform.insight.service_database_table;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.platform.insight.data_source.DatabaseConnections;
import com.platform.insight.factory.database.DatabaseFactory;
import com.platform.insight.service.ResultBaseService;
import com.platform.insight.utils.DatabaseUtils;
import com.platform.insight.utils.EnvUtils;

import com.platform.insight.utils.ServicePlatformCreateUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(ServiceDatabaseTable.ID)
public class ServiceDatabaseTable extends ResultBaseService implements ApiService {
    public static final String ID = "service_database_table";
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
        //是否存在表修改
        boolean hasTable = databaseFactory.checkTableExists(final_template);
        if(!hasTable){//不存在则新增
            Map<String, Object> createTableSQL = databaseFactory.getCreateTableSQL(final_template);
            if(!(boolean)createTableSQL.get("success")){
                return createTableSQL;
            }
            //执行创建表sql
            JdbcTemplate writeTemplate = DatabaseConnections.getWriteTemplate();
            String sql = (String) createTableSQL.get("sql");
            writeTemplate.execute(sql);
            //初始化数据
            String init_data_service = final_template.getString("init_data_service");
            String msg = "创表成功！";
            if(!StringUtils.isEmpty(init_data_service)){
                ServicePlatformCreateUtils.result(init_data_service,new HashMap<>());
                msg+=" 新增成功！";
            }
            return getResult(true,msg);
        }else{
            //获取表字段信息
            databaseFactory.getFieldsDescFromDataBase(final_template);
            //获取表字段不同的信息
            databaseFactory.getDifferentField(final_template);
           List<String> alterTableSQL = databaseFactory.getAlterTableSQL(final_template);

           if(alterTableSQL.size()>0){
               JdbcTemplate writeTemplate = DatabaseConnections.getWriteTemplate();
               writeTemplate.batchUpdate( alterTableSQL.toArray(new String[0]));

               return getResult(true,"更新表成功");
           }else{
               return getResult(true,"已经是最新的");
           }
        }

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

    private List<String> getAlterTableSQL(JSONObject final_template){
        List<String> list = new ArrayList<>();
        JSONArray newFields = final_template.getJSONArray("newFields");
        String table_name = final_template.getString("table_name");

        for(int i=0;i<newFields.size();i++){
            JSONObject field = newFields.getJSONObject(i);
            StringBuilder sb = new StringBuilder();
            String field_1 = field.getString("field_1");


            sb.append("alter table "+table_name+" add  ");

            sb.append(field_1);
            String[] field_type = field.getString("field_3").split(",");
            if(field_type.length>1){
                sb.append(" "+field_type[0]+"("+field_type[1]+")");
            }else{
                sb.append(" "+field_type[0]+" ");
            }
            sb.append(" ");
            list.add(sb.toString());
        }
        JSONArray updateFields = final_template.getJSONArray("updateFields");
        for(int i=0;i<updateFields.size();i++){
            JSONObject field = updateFields.getJSONObject(i);
            StringBuilder sb = new StringBuilder();
            String field_1 = field.getString("field_1");

            sb.append("alter table "+table_name+" modify ");

            sb.append(field_1);
            String[] field_type = field.getString("field_3").split(",");
            if(field_type.length>1){
                sb.append(" "+field_type[0]+"("+field_type[1]+")");
            }else{
                sb.append(" "+field_type[0]+" ");
            }

            sb.append(" ");
            list.add(sb.toString());
        }

        if(DatabaseUtils.isMySQL()){
            //修改引擎
            if(final_template.containsKey("engine_sql")){
                list.add(final_template.getString("engine_sql"));
            }
        }



        if(EnvUtils.isDebugger()){
            System.out.println(list);
        }
       return list;

    }
    private void getFieldsDescFromDataBase(JSONObject final_template){
        JdbcTemplate readTemplate = DatabaseConnections.getReadTemplate();
        String sql ="";
        String table_name = final_template.getString("table_name");

        if(DatabaseUtils.isMySQL()){

            sql = "desc "+table_name;
        }else if(DatabaseUtils.isOracle()){
            table_name = table_name.toUpperCase();
            sql = "select column_name,data_type,data_length from all_tab_columns where table_name='"+table_name+"'";
        }
        List<Map<String, Object>> fields = readTemplate.queryForList(sql);
        Map<String,Object> data = new HashMap<>();
        for(int i= 0;i<fields.size();i++){
            Map<String, Object> field = fields.get(i);
            if(DatabaseUtils.isMySQL()){
                data.put(field.get("Field")+"",field);
            }else if(DatabaseUtils.isOracle()){
                data.put(field.get("COLUMN_NAME")+"", field);
            }

        }
        final_template.put("fields_from_database",data);
    }

    private void getDifferentField(JSONObject final_template){
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

    }

    private Map<String,Object> getCreateTableSQL(JSONObject final_template ){
        StringBuilder sb = new StringBuilder();
        sb.append("create table ");
        String table = final_template.getString("table_name");

        sb.append(" "+table+" ( ");
        JSONArray fields = final_template.getJSONArray("fields");

        for(int i=0;i<fields.size();i++){

            JSONObject object = fields.getJSONObject(i);
            String field_1 = object.getString("field_1");

            sb.append(" "+field_1+" ");


            String field_3 = object.getString("field_3");
            String[] fields_arr = field_3.split(",");
            if(fields_arr.length<=0){
                return getResult(false,field_1+" 类型字段不能为空");
            }
            if(fields_arr.length==1){
                sb.append(fields_arr[0] );
            }else{
                sb.append(" "+fields_arr[0]+" " +"("+fields_arr[1]+") ");
            }

            if("primary_key".equals(object.getString("field_4"))){
                sb.append(" primary key ");
            }

            if(i!=(fields.size()-1)){
                sb.append(" , ");
            }
        }
        sb.append(" ) ");

        if(DatabaseUtils.isMySQL()){
            if(final_template.containsKey("engine")){
                String engine = final_template.getString("engine");
                sb.append(" engine="+engine);
            }
        }

        if(EnvUtils.isDebugger()){
            System.out.println(sb.toString());
        }
        Map<String,Object> data = new HashMap<>();
        data.put("sql",sb.toString());
        data.put("success",true);
        return data;
    }
    @Override
    public boolean checkTableExists(JSONObject final_template) {
        JdbcTemplate readTemplate = DatabaseConnections.getReadTemplate();
        String sql ="";
        String table_name = final_template.getString("table_name");
        //todo:获取数据库类型有误
        if(DatabaseUtils.isMySQL()){
             sql = "show tables like '" + table_name + "'; ";
        }else if(DatabaseUtils.isOracle()){
            table_name = table_name.toUpperCase();
            sql = "select table_name from user_tables where TABLE_NAME = '"+table_name+"' ";
        }
        if(EnvUtils.isDebugger()){
            System.out.println(sql);
        }
        List<String> table_List = readTemplate.queryForList(sql,String.class);
        if(table_List.size()<=0){
            return false;
        }else {
            return true;
        }

    }
}
