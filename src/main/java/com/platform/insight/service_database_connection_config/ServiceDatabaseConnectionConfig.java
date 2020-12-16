package com.platform.insight.service_database_connection_config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platform.insight.data_source.DruidConfiguration;
import com.platform.insight.factory.database.DatabaseFactory;
import com.platform.insight.model.ApiModel;
import com.platform.insight.service.ResultBaseService;

import com.platform.insight.utils.SpringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

@Service(ServiceDatabaseConnectionConfig.ID)
public class ServiceDatabaseConnectionConfig extends ResultBaseService implements ApiService {
    public static final String ID = "service_database_connection_config";
    @Override
    public Map<String, Object> result(Map<String, Object> actual) {
        String service = (String) actual.get("service");
        ApiModel apiModel = getApiModel(service);
        if(apiModel == null){
            return getResult(false,"数据库配置不存在");
        }

        String template_final = apiModel.getTemplate();
        JSONObject final_template = JSON.parseObject(template_final);
        String class_name = final_template.getString("class_name");
        if(StringUtils.isEmpty(class_name)){
            return getResult(false,"数据库驱动不能为空");
        }

        String database_type = final_template.getString("database_type");
        DatabaseFactory databaseFactory = null;
        try{
             databaseFactory = SpringUtils.getBean(class_name);
        }catch (Exception e){

        }
        if (databaseFactory == null){
            return getResult(false,"暂不支持数据库类型:"+database_type);
        }

        //将fields转成字典
        databaseFactory.changeField(actual,final_template);
        //将property转成字典
        databaseFactory.changeProperty(final_template);
        databaseFactory.changeUrl(final_template);
        updateConnection((Map<String, Object>) actual.get("fields"),final_template);





        Map<String,Object> map= new HashMap<>();
        map.put("data",final_template);
        map.put("success",true);
        map.put("msg","查询成功");
       // DatabaseConnectionConfigUtils.ConfigOne(map);
        return map;
    }

    // 获得连接对象
    private  synchronized void testConn(JSONObject template ){

            try {
                String driver = template.getString("class_name");
                String url = template.getString("url");
                String username = template.getString("username");
                String password = template.getString("password");
                Class.forName(driver);
                Connection connection = DriverManager.getConnection(url, username, password);
                connection.close();
                template.put("msg","连接成功");
                template.put("success",true);
            } catch (Exception e) {
                e.printStackTrace();
                template.put("msg",e.toString());
                template.put("success",false);
            }


    }







    private  void updateConnection(Map<String, Object> actual,JSONObject final_template){

        Map<String,Object> fields = (Map<String, Object>) final_template.get("fields");
        String connection_update = (String) fields.get("connection_update");
        if(actual == null){
            actual = new HashMap<>();
        }
        if(!actual.containsKey("connection_update")){
            actual.put("connection_update",connection_update);
        }
        //如果更新数据库连接
        if("true".equals(actual.get("connection_update"))){
            testConn(final_template);
            Boolean success = final_template.getBoolean("success");
            if(!success){
                return ;
            }
            DruidConfiguration druidConfiguration = SpringUtils.getBean(DruidConfiguration.ID);
            DataSource dataSource = druidConfiguration.getDataSource(final_template);
            try {
                boolean closed = dataSource.getConnection().isClosed();
                if(closed){
                    dataSource.getConnection().close();
                    final_template.put("msg","连接失败");
                    return;
                }
            } catch (Exception e) {
                final_template.put("msg","连接失败");
                e.printStackTrace();
                return ;
            }
            String service = final_template.getString("service");
            //service 名称必须和数据连接名称相同
            JdbcTemplate jdbcTemplate = null;
            try{
                 jdbcTemplate = SpringUtils.getBean(service);
            }catch (Exception e){

            }

            if(jdbcTemplate !=null){
                try {
                    jdbcTemplate.getDataSource().getConnection().close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                jdbcTemplate.setDataSource(dataSource);
                final_template.put("msg","替换成功");
            }
        }
    }
}
