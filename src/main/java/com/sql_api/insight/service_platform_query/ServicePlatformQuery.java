package com.sql_api.insight.service_platform_query;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sql_api.insight.model.ApiModel;
import com.sql_api.insight.service.ResultBaseService;
import com.sql_api.insight.data_source.DatabaseConnections;
import com.sql_api.insight.utils.EnvUtils;
import com.sql_api.insight.utils.StringJoinUtils;
import com.sql_api.insight.utils.TreeUtils;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service(ServicePlatformQuery.ID)
public class ServicePlatformQuery extends ResultBaseService implements  ApiService{
    public static final String ID="service_platform_query" ;
    @Override
    public Map<String, Object> result(Map<String, Object> actual) {
        Map<String,Object> result = new HashMap<>();
        String service = (String) actual.get("service");
        ApiModel apiModel = getApiModel(service);
        String template_final = apiModel.getTemplate();
        JSONObject final_template = JSON.parseObject(template_final);
        //检查类型
        if(!actual.containsKey("search_fields")){
           actual.put("search_fields",new HashMap<>());
        }

        Map<String, Object> map = checkFields((Map<String, Object>) actual.get("search_fields"), final_template.getJSONArray("search_fields"), final_template);
        if(!(boolean)map.get("success")){
            return map;
        }
        //更新搜索条件，并且加上默认条件
        updateSearchFields((Map<String, Object>) actual.get("search_fields"), final_template);

        result = queryResult(final_template);//查询结果


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

    protected boolean canTree(JSONObject template){
        String toTree = template.getString("toTree");
        if(StringUtils.isEmpty(toTree)){
            return false;
        }
        String[] split = toTree.split(",");
        if(split.length<2){
            return false;
        }
        int count=0;
        JSONArray show_fields = template.getJSONArray("show_fields");
        for(int i=0;i<show_fields.size();i++){
            JSONObject jsonObject = show_fields.getJSONObject(i);
            if(split[0].equals(jsonObject.getString("field_1"))){
                count++;
                if(count==2){
                    return true;
                }
            }
            if(split[0].equals(jsonObject.getString("field_1"))){
                count++;
                if(count==2){
                    return true;
                }
            }
        }
        return false;
    }

    public Map<String ,Object> queryResult(JSONObject template) {
        JdbcTemplate readTemplate = DatabaseConnections.getReadTemplate();
        String sql = template.getString("sql");
        //添加显示字段
        JSONArray search_fields = template.getJSONArray("search_fields");
        JSONArray show_fields = template.getJSONArray("show_fields");
        JSONArray order_fields = template.getJSONArray("order_fields");
        if(show_fields.size()<=0){
            return null;
        }
        //转换成sql
        Map<String, Object> sql_map = getSQL(sql, search_fields, show_fields,order_fields);
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


    @Override
    public JSONObject updateSearchFields(Map<String, Object> actual, JSONObject template) {
        if (actual == null) {
            return template;
        }




        JSONArray search_fields = template.getJSONArray("search_fields");
        for (int i = 0; i < search_fields.size(); i++) {
            JSONObject o = search_fields.getJSONObject(i);
            String field = o.getString("field_1");
            if (actual.containsKey(field)) {
                o.put("field_5", actual.get(field) + "");
            }
        }

        //如果存在默认字段
        if(template.containsKey("default_search_fields")){
            JSONArray default_search_fields = template.getJSONArray("default_search_fields");
            for (int i = 0; i < default_search_fields.size(); i++) {
                JSONObject o = default_search_fields.getJSONObject(i);
                Object rulesValue = getRulesValue(o, new HashMap<>());
                if(rulesValue != null){
                    o.put("field_5",rulesValue);
                }
                String field = o.getString("field_1");
                if(!hasField(field,search_fields)){
                    search_fields.add(o);
                }
            }
        }

        return template;
    }

    private boolean hasField(String field,JSONArray target){
        for(int i=0;i<target.size();i++){
            if(target.getJSONObject(i).getString("field_1").equals(field)){
                return true;
            }
        }
        return false;
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
            item.put("show",jsonObject.getString("field_3"));
            item.put("can_update",jsonObject.getString("field_4"));
            String field_5 = jsonObject.getString("field_5");
            if(StringUtils.isEmpty(field_5)){
                item.put("jsonObj",new JSONObject());
            }else{
                item.put("jsonObj",JSON.parseObject(field_5));
            }
            fields.add(item);
        }
        return fields;
    }

    @Override
    public List<Map<String, Object>> toTree(List<Map<String, Object>> list, String parent_field, String id_field,String children_field) {
        return TreeUtils.listToTree(list,parent_field,id_field,children_field);
    }

    private Map<String,Object> getSQL(String sql,JSONArray search_fields,JSONArray show_fields,JSONArray order_fields){

        List<String> collect = show_fields.stream().map(item -> ((JSONObject)item).getString("field_1")).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        StringBuilder count_sb = new StringBuilder();
        sb.append("select "+ StringJoinUtils.join(collect,",")+" from ( ");
        count_sb.append("select count(*) from (");
        sb.append( sql+" ) as t ");
        count_sb.append( sql+" ) as t ");
        sb.append(" where 1=1 ");
        count_sb.append(" where 1=1 ");
        int page = 1;
        int size = 10;
        List<Object> data = new ArrayList<>();
        for (int i = 0;i<search_fields.size();i++){
            JSONObject o = (JSONObject) search_fields.get(i);
            String name = o.getString("field_1");
            String value = o.getString("field_5");
            if(StringUtils.isEmpty(value)){
                continue;
            }
            if("page".equals(name)){
                page = Integer.parseInt(value);
                if(page<=0){
                    page=1;
                }
            }else if( "size".equals(name)){
                size = Integer.parseInt(value);
            }else{
                String operation = o.getString("field_4");
                String field_sql = "";
                //根据操作修改字段条件
                switch (operation){
                    case "in":
//                        List<String> params = Arrays.asList(value.split(",")).stream().map(item -> "?").collect(Collectors.toList());
//                        field_sql = "and "+name+" "+operation+" ("+StringJoinUtils.join(params,",")+")";
//                        break;
                    case "not in":
                        List<String> params_not_in = Arrays.asList(value.split(",")).stream().map(item -> "?").collect(Collectors.toList());
                        field_sql = "and "+name+" "+operation+" ("+StringJoinUtils.join(params_not_in,",")+")";

                        break;
                    case "between_and":
//                        field_sql =  "and ( "+name+" between ? and ? ) ";
//                        break;
                    case "date_between_and":
                        field_sql =  "and ( "+name+" between ? and ? ) ";
                        break;
                    case "=":
//                        field_sql = "and "+name+" "+operation+" ? ";
//                        break;
                    case "like":
//                        field_sql = "and "+name+" "+operation+" ? ";
//                        break;

                    default :
                        field_sql = "and "+name+" "+operation+" ? ";
                        break;

                }
                sb.append(field_sql);
                count_sb.append(field_sql);
                String type = o.getString("field_3");


                if(!StringUtils.isEmpty(type)&& type=="I"){
                    data.add(Integer.parseInt(value));
                }else{

                    //根据操作修改value
                    switch (operation){
                        case "=":
                            data.add(value);
                            break;
                        case "like":
                            data.add("%"+value+"%");
                            break;
                        case "in":
//                            data.addAll(Arrays.asList(value.split(",")));
//                            break;
                        case "not in":
                            data.addAll(Arrays.asList(value.split(",")));
                            break;
                        case "between_and":
                            String[] between_and = value.split(",");
                            data.add(between_and[0]);
                            if(between_and.length<=1){
                                data.add(between_and[0]);
                            }else{
                                data.add(between_and[1]);
                            }
                            break;
                        case "date_between_and":
                            String[] date_between_and = value.split(",");
                            data.add(date_between_and[0]+" 00:00:00");
                            if(date_between_and.length<=1){
                                data.add(date_between_and[0]+" 00:00:00");
                            }else{
                                data.add(date_between_and[1]+" 23:59:59");
                            }
                            break;
                         default:
                             data.add(value);
                             break;
                    }

                }

            }

        }

        boolean has_order_by = false;
        List<String> orderBy = new ArrayList<>();
        for(int i =0;i<order_fields.size();i++){
            JSONObject order = order_fields.getJSONObject(i);
            String field = order.getString("field_1");
            String value = order.getString("field_5");
            if(!StringUtils.isEmpty(field) && !StringUtils.isEmpty(value)){
                orderBy.add(" "+field+" "+value+" ");
            }
        }

        if(orderBy.size()>0){
            sb.append(" order by ");
            sb.append(" "+StringJoinUtils.join(orderBy,", "));
        }

        int start = (page-1)*size;
        sb.append(" limit "+start+" , "+size);
        Map<String,Object> map = new HashMap<>();
        map.put("data_sql",sb.toString());
        map.put("count_sql",count_sb.toString());
        map.put("sql_data",data);
        if (EnvUtils.isDebugger()){
            System.out.println(sb.toString());
            System.out.println(data);
        }

        return map;
    }


}
