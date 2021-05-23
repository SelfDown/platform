package com.platform.insight.service_platform_query_template;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.platform.insight.factory.database.DatabaseFactory;
import com.platform.insight.model.PlatformTemplate;
import com.platform.insight.service_platform_query.ServicePlatformQuery;
import com.platform.insight.utils.Const;
import com.platform.insight.utils.EnvUtils;
import com.platform.insight.utils.ResultUtils;
import com.platform.insight.utils.StringTemplateUtils;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.tags.EditorAwareTag;

import java.io.IOException;
import java.util.*;


@Service(ServicePlatformQueryTemplate.ID)
public class ServicePlatformQueryTemplate extends ServicePlatformQuery {
    public static final String ID = "service_platform_query_template";

    @Override
    public Map<String, Object> check(Map<String, Object> actual, PlatformTemplate final_template, boolean is_http) {
        Map<String, Object> r = super.check(actual, final_template, is_http);
        return r;
    }


    @Override
    public Map<String, Object> result(Map<String, Object> actual) {

        //获取数据模板，已经处理过的
        PlatformTemplate finalTemplate = getFinalTemplate();
        //获取数据库工厂
        Map<String, Object> databaseFactoryInfo = getDatabaseFactory();

        //检查是否支持，改数据库类型

        if (!isSuccess(databaseFactoryInfo)) {
            return databaseFactoryInfo;
        }
        //获取对应的数据库工厂
        DatabaseFactory databaseFactory = (DatabaseFactory) databaseFactoryInfo.get(Const.DATA);
        Map<String, Object> req_params = finalTemplate.getDefaultFieldsDict();
        finalTemplate.log(req_params);

        Map<String, Object> result = getResult(true, "查询成功");

//

        String data_sql = finalTemplate.getDataSQL();
        Map<String, Object> sqlBuilder = buildSQL(data_sql, req_params);


        String sql = (String) sqlBuilder.get(Const.SQL);
        List<Object> params = (List<Object>) sqlBuilder.get(Const.DATA);

        if (EnvUtils.isDebugger()) {
            finalTemplate.log(sqlBuilder);
        }
        List<Map<String, Object>> listResult = databaseFactory.getListResult(sql, params);

        return ResultUtils.getResultMap(true,"查询成功",listResult);
        //如果是预览sql

//        String preview = final_template.getString("preview");
//        if ("true".equals(preview)) {
//            String msg = "查询成功";
//            sqlBuilder.put("msg", msg);
//            sqlBuilder.put("success", true);
//            return sqlBuilder;
//
//
//        }
        //查询结果
//        try {
//            BeetlString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        result = databaseFactory.getSQLResult(sqlBuilder);
//        return result;
    }


    public String beetlString(String blSql, Map<String, Object> data, String key_type) {
        //new一个模板资源加载器
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        /* 使用Beetl默认的配置。
         * Beetl可以使用配置文件的方式去配置，但由于此处是直接上手的例子，
         * 我们不去管配置的问题，只需要基本的默认配置就可以了。
         */
        Configuration config = null;
        try {
            config = Configuration.defaultConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Beetl的核心GroupTemplate
        GroupTemplate groupTemplate = new GroupTemplate(resourceLoader, config);
        //我们自定义的模板，其中${title}就Beetl默认的占位符
        Template template = groupTemplate.getTemplate(blSql);
        //渲染字符串
        for (String key : data.keySet()) {
            if (key_type == SQLConst.KEY) {
                String value = data.get(key).toString();

                if (!StringUtils.isEmpty(value)) {
                    template.binding(key, "${" + key + "}");
                }

            } else if ((key_type == SQLConst.PARAM)) {
                template.binding(key, "?");
            }


        }
        String str = template.render();
        return str;
    }

    //    private String toKeySql(String blSql,Map<String,Object> data){
//        return beetlString(blSql,data);
//    }
    private class SQLConst {
        static final String KEY = "KEY";
        static final String PARAM = "PARAM";
    }

    private Object getOperationValue(Object value, String operation) {
        if (StringUtils.isEmpty(operation)) {
            return value;
        }
        List<String> ops = Arrays.asList("=");
        if (ops.contains(operation)) {
            return value;
        }
        //去掉空
        if (value != null) {
            value = value.toString().trim();
        }
        if (StringUtils.isEmpty(value)) {
            return value;
        }

        switch (operation) {
            case "like":
                return "%" + value + "%";

        }
        return value;

    }

    private Map<String, Object> buildSQL(String sqlTemplate, Map<String, Object> data) {
        Map<String, Object> build = new HashMap<>();

        String keySQL = beetlString(sqlTemplate, data, SQLConst.KEY);
        String sql = beetlString(keySQL, data, SQLConst.PARAM);
        List<Object> data_list = StringTemplateUtils.getOrderParams(keySQL, data);


        build.put(Const.SQL, sql);
        build.put(Const.DATA, data_list);

        return build;

    }


}
