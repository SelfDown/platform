package com.platform.insight.service_page_info;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.platform.insight.model.ApiModel;
import com.platform.insight.service.ResultBaseService;
import com.platform.insight.utils.ApiCacheUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service(ServicePageInfo.ID)
public class ServicePageInfo extends ResultBaseService implements  ApiService  {
    public static final String ID = "service_page_info";

    @Override
    public Map<String, Object> result(Map<String, Object> actual) {
        Map<String,Object> data = new HashMap<>();
        String service = (String) actual.get("service");
        Object serviceResult = ApiCacheUtils.getServiceResult(service);
        if(serviceResult != null){
            return (Map<String, Object>) serviceResult;
        }
        ApiModel apiModel = getApiModel(service);
        String template_final = apiModel.getTemplate();
        JSONObject final_template = JSON.parseObject(template_final);
        JSONArray fields_rules = final_template.getJSONArray("fields_rules");
        for(int i =0;i<fields_rules.size();i++){
            JSONObject field = fields_rules.getJSONObject(i);
            JSONObject rule = JSON.parseObject(field.getString("field_3"));
            String rule_case = rule.getString("rule");
            String field_1 = field.getString("field_1");
            switch (rule_case){
                case "simple_fields"://简单字段
                    data.put(field_1,final_template.getString(field_1));
                    break;
                case "form"://表单类型
                    Map<String, Object> form = getForm(final_template.getJSONArray(field_1));
                    data.put(rule.getString("form_field"),form.get("form"));
                    data.put(rule.getString("items_field"),form.get("items"));
                    data.put(rule.getString("rules_field"),form.get("rules"));
                    break;
                case "simple_list2array"://字段1转为数组
                    data.put(field_1,getFilters(final_template.getJSONArray(field_1)));
                    break;
                case "simple_str2Array"://字符串转数组
                    data.put(field_1,simple2Array(final_template.getString(field_1)));
                    break;
                case "field_3_2_json_list"://字段3转json列表
                    data.put(field_1,getField_3_JSON_List(final_template.getJSONArray(field_1)));
                    break;
                case "field_5_2_obj"://字段5转json 对象
                    data.put(field_1,getField_5_Obj(final_template.getJSONArray(field_1)));
                    break;
            }
        }



        ApiCacheUtils.setServiceMap(service,data);
        return data;
    }

    private List<Integer> simple2Array(String page_size_array){
        List<Integer> list = new ArrayList<>();
        String[] split = page_size_array.split(",");
        for(int i=0;i<split.length;i++){
            list.add(Integer.parseInt(split[i]));
        }
        return list;
    }
    private List<Object> getField_3_JSON_List(JSONArray page_title){
        List<Object> list = new ArrayList<>();
        for(int i=0;i<page_title.size();i++){
            String field_3 = page_title.getJSONObject(i).getString("field_3");
            list.add(JSON.parseObject(field_3));
        }
        return list;
    }

    private Map<String,Object> getField_5_Obj(JSONArray form){
        Map<String,Object> data = new HashMap<>();
        for(int i=0;i<form.size();i++){
            JSONObject field = form.getJSONObject(i);
            String field_5 = field.getString("field_5");
            String field_3 = field.getString("field_3");
            String field_1 =field.getString("field_1");
            if(!StringUtils.isEmpty(field_3)){
                JSONObject rule = JSON.parseObject(field_3);
                String data_type = rule.getString("data_type");
                if(StringUtils.isEmpty(data_type)){
                    data.put(field_1,field_5);
                    continue;
                }else if("I".equals(data_type)){
                    data.put(field_1,Integer.parseInt(field_5));
                }
            }else{
                data.put(field_1,field_5);
            }





        }
        return data;
    }

    private List<String> getFilters(JSONArray filters){

        List<String> filtersList = new ArrayList<>();
        for(int i=0;i<filters.size();i++){
            JSONObject filter = filters.getJSONObject(i);
            filtersList.add(filter.getString("field_1"));
        }
        return filtersList;
    }

    private Map<String,Object> getForm( JSONArray items_arr){
        Map<String,Object> map = new HashMap<>();

        //默认值
        Map<String,Object> form = new HashMap<>();
        //数据项
        List<Object> items = new ArrayList<>();
        //验证规则
        Map<String,Object> form_rules = new HashMap<>();
        for(int i=0;i<items_arr.size();i++){
            JSONObject item = items_arr.getJSONObject(i);
            String field_1 = item.getString("field_1");
            form.put(field_1,item.getString("field_5"));
            items.add(JSON.parseObject(item.getString("field_3")));
            JSONArray field_4 = JSON.parseArray(item.getString("field_4"));
            if(field_4!=null && field_4.size()>0){
                form_rules.put(field_1,field_4);
            }
        }
        map.put("form",form);
        map.put("items",items);
        map.put("rules",form_rules);
        return map;
    }
}
