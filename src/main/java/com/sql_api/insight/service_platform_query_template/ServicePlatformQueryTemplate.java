package com.sql_api.insight.service_platform_query_template;

import com.alibaba.fastjson.JSONObject;
import com.sql_api.insight.factory.database.DatabaseFactory;
import com.sql_api.insight.service_platform_query.ServicePlatformQuery;
import com.sql_api.insight.utils.EnvUtils;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Service(ServicePlatformQueryTemplate.ID)
public class ServicePlatformQueryTemplate extends ServicePlatformQuery {
    public static final String ID = "service_platform_query_template";

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

        Map<String,Object> result = getResult(true, "查询成功");


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
        Map<String,Object> sqlBuilder = buildSQL(final_template);
        //如果是预览sql

        String preview = final_template.getString("preview");
        if("true".equals(preview)){
            String msg = "查询成功";
            sqlBuilder.put("msg",msg);
            sqlBuilder.put("success",true);
            return sqlBuilder;


        }
        //查询结果
        try {
            BeetlString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        result = databaseFactory.getSQLResult(sqlBuilder);
        return result;
    }
    public void BeetlString() throws Exception {
        //new一个模板资源加载器
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        /* 使用Beetl默认的配置。
         * Beetl可以使用配置文件的方式去配置，但由于此处是直接上手的例子，
         * 我们不去管配置的问题，只需要基本的默认配置就可以了。
         */
        Configuration config = Configuration.defaultConfiguration();
        //Beetl的核心GroupTemplate
        GroupTemplate groupTemplate = new GroupTemplate(resourceLoader, config);
        //我们自定义的模板，其中${title}就Beetl默认的占位符
        String testTemplate="<html>\n" +
                "<head>\n" +
                "\t<title>${title}</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t<h1>${name}</h1>\n" +
                "</body>\n" +
                "</html>";
        Template template = groupTemplate.getTemplate(testTemplate);
        template.binding("title","This is a test template Email.");
        template.binding("name", "beetl");
        //渲染字符串
        String str = template.render();
        System.out.println(str);
    }

    private Map<String, Object> buildSQL(JSONObject final_template) {
        Map<String,Object> build = new HashMap<>();
        String selectSQL = final_template.getString("data_sql");
        String countSQL = final_template.getString("count_sql");

        Map<String,Object> data = getDictFields(final_template,"search_fields");
        build.put("countSQL",countSQL);
        build.put("selectSQL",selectSQL);
        build.put("data",new ArrayList<Object>());
        if(EnvUtils.isDebugger()){
            System.out.println(countSQL);
            System.out.println(selectSQL);
            System.out.println(data);
        }
        return build;

    }




}
