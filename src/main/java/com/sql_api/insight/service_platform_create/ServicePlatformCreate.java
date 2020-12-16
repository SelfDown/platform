package com.sql_api.insight.service_platform_create;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.sql_api.insight.message.MessageReceiver;
import com.sql_api.insight.model.ApiModel;
import com.sql_api.insight.service.ResultBaseService;

import com.sql_api.insight.data_source.DatabaseConnections;
import com.sql_api.insight.utils.EnvUtils;
import com.sql_api.insight.utils.SpringUtils;
import com.sql_api.insight.utils.StringJoinUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service(ServicePlatformCreate.ID)
public class ServicePlatformCreate extends ResultBaseService implements  ApiService {
    public static final String ID ="service_platform_create";
    @Override
    public Map<String, Object> result(Map<String, Object> actual) {

        String service = (String) actual.get("service");
        ApiModel apiModel = getApiModel(service);
        String template_final = apiModel.getTemplate();
        JSONObject final_template = JSON.parseObject(template_final);
        Map<String, Object> map = checkFields((Map<String, Object>) actual.get("fields"), final_template.getJSONArray("fields"),final_template);
        if(actual.get("fields")==null){
            actual.put("fields",new HashMap<>());
        }

        //检查状态是否成功
        String msg = null;
        if(!(boolean)map.get("success")){
            return map;
        }
        Map<String, Object> result_map = insertRecord(updateFields((Map<String, Object>) actual.get("fields"), final_template));
        result_map.put("msg" , "新增成功");
        Map<String,Object> show_fields = getShowFields(final_template);
        result_map.put("success",true);
        result_map.put("data",show_fields);
        return  result_map;
    }

    private Map<String,Object> getShowFields(JSONObject final_template){
        Map<String,Object> data = new HashMap<>();
        JSONArray show_fields = final_template.getJSONArray("show_fields");
        if(show_fields == null){
            return data;
        }
        for(int i=0;i<show_fields.size();i++){
            JSONObject field = show_fields.getJSONObject(i);
            String field_1 = field.getString("field_1");
            if(StringUtils.isEmpty(field_1)){
                continue;
            }
            String field_5 = field.getString("field_5");
            data.put(field_1,field_5);
        }

        JSONArray default_fields = final_template.getJSONArray("default_fields");
        for(int i=0;i<default_fields.size();i++){
            JSONObject field = default_fields.getJSONObject(i);
            String field_1 = field.getString("field_1");
            String field_5 = field.getString("field_5");
            if(data.containsKey(field_1)){
                data.put(field_1,field_5);
            }
        }

        return data;



    }

    /**
     *
     * @param actual
     * @param template
     * @return 更新字段后的模板字符串
     */
    @Override
    public JSONObject updateFields(Map<String, Object> actual, JSONObject template) {
        //获取可更改字段
        JSONArray fields = template.getJSONArray("fields");
        //获取默认字段
        JSONArray default_fields = template.getJSONArray("default_fields");
        //将更在字段加入默认字段
        for (int i=0;i<fields.size();i++){
            JSONObject jsonObject = fields.getJSONObject(i);
            String field = jsonObject.getString("field_1");
            if(actual.containsKey(field)){
                jsonObject.put("field_5",actual.get(field));
                JSONObject defaultField = getDefaultField(default_fields, field);
                //没有就添加新字段,否则就修改
                if(defaultField == null){
                    default_fields.add(jsonObject);
                }else{
                    defaultField.put("field_5",actual.get(field));
                }

            }
        }

        for (int i=0;i<default_fields.size();i++){
            JSONObject object = default_fields.getJSONObject(i);
            Object rulesValue = getRulesValue(object, actual);
            //填充数据
            if(rulesValue!= null){
                object.put("field_5",rulesValue);

                String primary_key = object.getString("field_4");
                if(!StringUtils.isEmpty(primary_key)){
                    template.put("primary_key",rulesValue);
                }
            }
        }
        return template;
    }






    @Override
    public Map<String, Object> insertRecord(JSONObject templateFinal) {
        Map<String, Object> insertSQL = getInsertSQL(templateFinal);
        JdbcTemplate writeTemplate = DatabaseConnections.getWriteTemplate();
        String asynchronous = templateFinal.getString("asynchronous");
        Object[] data = ((List<Object>) insertSQL.get("data")).toArray();
        String sql = (String) insertSQL.get("sql");
        Map<String,Object> result = new HashMap<>();
        if(!StringUtils.isEmpty(asynchronous) && asynchronous.equals("true")){
            MessageReceiver bean = SpringUtils.getBean(MessageReceiver.class);
            Map<String,Object> operation = new HashMap<>();
            operation.put("sql",sql);
            operation.put("data",data);
            bean.send(MessageReceiver.OPERATION_ADD,operation);
        }else{
            int count = writeTemplate.update(sql, data);
            result.put("count",count);
        }
        result.put("data","插入成功");
        result.put("primary_key",templateFinal.getString("primary_key"));
        return result;

    }

    private Map<String,Object> getInsertSQL(JSONObject template){
        List<String> fields = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        List<String> tmp = new ArrayList<>();
        StringBuilder sql = new StringBuilder("insert into "+template.getString("table_name")+" (");
        JSONArray fields_values = template.getJSONArray("default_fields");
        for(int i =0;i<fields_values.size();i++){
            JSONObject jsonObject = fields_values.getJSONObject(i);
            String field_1 = jsonObject.getString("field_1");
            if(StringUtils.isEmpty(field_1)){
                continue;
            }
            fields.add(field_1);
            tmp.add("?");
            values.add(jsonObject.getString("field_5"));
        }
        sql.append(StringJoinUtils.join(fields,","));
        sql.append(") values (");
        sql.append(StringJoinUtils.join(tmp,","));
        sql.append(")");
        Map<String,Object> data = new HashMap<>();
        data.put("sql",sql.toString());
        data.put("data",values);
        if(EnvUtils.isDebugger()){
            System.out.println(sql.toString());
            System.out.println(values);
        }
        return data;
    }
}
