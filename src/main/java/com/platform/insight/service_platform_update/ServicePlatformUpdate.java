package com.platform.insight.service_platform_update;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.platform.insight.model.ApiModel;
import com.platform.insight.service.ResultBaseService;
import com.platform.insight.data_source.DatabaseConnections;
import com.platform.insight.utils.EnvUtils;
import com.platform.insight.utils.ResultUtils;
import com.platform.insight.utils.StringJoinUtils;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(ServicePlatformUpdate.ID)
public  class ServicePlatformUpdate  extends ResultBaseService implements  ApiService {
    public static final String ID = "service_platform_update";
    @Override
    public Map<String, Object> result(Map<String, Object> actual) {
        if(!actual.containsKey("filters")){
            return ResultUtils.getResultMap(false,"filters 不能为空");
        }
        String service = (String) actual.get("service");
        ApiModel apiModel = getApiModel(service);
        String template_final = apiModel.getTemplate();
        JSONObject final_template = JSON.parseObject(template_final);
        //检查筛选条件字段 filters,如果有记录就保存起来
        String msg = null;
        Map<String, Object> filters_fields = checkFields((Map<String, Object>) actual.get("filters"), final_template.getJSONArray("filters"),final_template);
        if(!(boolean)filters_fields.get("success")){
            msg = (String) filters_fields.get("msg");
            return filters_fields;
        }

        //获取参数,并且添加filters_value 参数
        updateFilters((Map<String, Object>) actual.get("filters"), final_template);

        //检查是否存在
        Map<String, Object> map = checkFilters(final_template);
        if(map != null){
            return  map;
        }

        //检查更新字段 fields
        Map<String, Object> update_fields = checkFields((Map<String, Object>) actual.get("fields"), final_template.getJSONArray("fields"),final_template);
        if(!(boolean)update_fields.get("success")){
           return update_fields;
        }


        //更新数据字段
        updateFields((Map<String, Object>) actual.get("fields"), final_template);


        //更新表
        Map<String,Object> result = updateRecord(final_template);
        msg="更新成功！";
        result.put("msg",msg);
        result.put("success",true);
        return result;
    }



    /**
     * 更新过滤条件 field_1 取字段，field3 和field4。优先请求请求的
     * @param actual
     * @param template
     * @return
     */
    @Override
    public JSONObject updateFilters(Map<String, Object> actual, JSONObject template) {

        //获取filters字段
        JSONArray filters = template.getJSONArray("filters");
        JSONArray filters_values = new JSONArray();
        for(int i=0;i<filters.size();i++){
            JSONObject template_filter = filters.getJSONObject(i);
            String field = template_filter.getString("field_1");
            JSONObject object = new JSONObject();
            object.put("field_1",field);
            object.put("field_3",template_filter.getString("field_3"));
            object.put("field_5",template_filter.getString("field_5"));
            if(actual.containsKey(field)){
                object.put("field_5",actual.get(field));
            }else{
                Object rulesValue = getRulesValue(object, actual);
                if(rulesValue!=null){
                    object.put("field_5",rulesValue);
                }
            }

            filters_values.add(object);
        }

        JSONArray default_filters = template.getJSONArray("default_filters");
        if(default_filters != null){
            for(int i=0;i<default_filters.size();i++){
                JSONObject template_filter = default_filters.getJSONObject(i);
                String field = template_filter.getString("field_1");
                JSONObject object = new JSONObject();
                object.put("field_1",field);
                object.put("field_3",template_filter.getString("field_3"));
                object.put("field_5",template_filter.getString("field_5"));
                if(actual.containsKey(field)){
                    object.put("field_5",actual.get(field));
                }else{
                    Object rulesValue = getRulesValue(object, actual);
                    if(rulesValue!=null){
                        object.put("field_5",rulesValue);
                    }
                }

                filters_values.add(object);
            }
        }

        template.put("filters_values",filters_values);
        return template;
    }


    @Override
    public Map<String, Object> updateRecord(JSONObject template) {
        Map<String, Object> insertSQL = getUpdateSQL(template);
        JdbcTemplate writeTemplate = DatabaseConnections.getWriteTemplate();
        List<Object> data = (List<Object>) insertSQL.get("data");
        int count = writeTemplate.update((String) insertSQL.get("sql"), data.toArray());
        Map<String,Object> result = new HashMap<>();
        result.put("data","更新成功");
        result.put("count",count);
        return result;
    }

    @Override
    public Map<String, Object> checkFilters(JSONObject template) {

        StringBuilder sb = new StringBuilder("select * from ");
        sb.append(template.getString("table_name"));
        sb.append(" where 1=1 ");


        JSONArray filters = template.getJSONArray("filters_values");

        //拼接where 条件

        List<String> values = new ArrayList<>();
        for(int i =0;i<filters.size();i++){
            JSONObject jsonObject = filters.getJSONObject(i);
            sb.append(" and "+jsonObject.getString("field_1")+"= ? ");
            values.add(jsonObject.getString("field_5"));
        }
        if(EnvUtils.isDebugger()){
            System.out.println(sb.toString());
            System.out.println(values);
        }
        List<Map<String, Object>> maps = DatabaseConnections.getReadTemplate().queryForList(sb.toString(), values.toArray());
        if(maps.size()==0){
            return ResultUtils.getResultMap(false,"记录不存在！");
        }else if(maps.size()==1){
            template.put("records",maps);
            return null;
        }else{
            return ResultUtils.getResultMap(false,"只能更新一条记录。目前："+maps.size());
        }


    }


    private Map<String,Object> getUpdateSQL(JSONObject template){
        List<String> fields = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        List<String> tmp = new ArrayList<>();
        StringBuilder sql = new StringBuilder("update "+template.getString("table_name")+" set ");
        JSONArray fields_values = template.getJSONArray("default_fields");
        //拼接更新字段
        for(int i =0;i<fields_values.size();i++){
            JSONObject jsonObject = fields_values.getJSONObject(i);
            tmp.add("  "+jsonObject.getString("field_1")+" = ? ");
            values.add(jsonObject.getString("field_5"));
        }
        sql.append(StringJoinUtils.join(tmp,","));
        JSONArray filters = template.getJSONArray("filters_values");

        //拼接where 条件
        sql.append(" where ");
        List<String> filters_tmp = new ArrayList<>();
        for(int i =0;i<filters.size();i++){
            JSONObject jsonObject = filters.getJSONObject(i);
            filters_tmp.add("  "+jsonObject.getString("field_1")+"= ? ");
            values.add(jsonObject.getString("field_5"));
        }

        sql.append(StringJoinUtils.join(filters_tmp," and "));
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
