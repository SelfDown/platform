package com.sql_api.insight.data_source;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import com.sql_api.insight.utils.DatabaseConnectionConfigUtils;
import com.sql_api.insight.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Configuration(DruidConfiguration.ID)
public class DruidConfiguration {
    public static final String ID = "DruidConfiguration";

    public static final String READ_SOURCE="READ_SOURCE";
    public static final String WRITE_SOURCE="WRITE_TEMPLATE";

    public static final String READ_TEMPLATE="service_read_source";
    public static final String WRITE_TEMPLATE="service_write_source";


    public  DataSource getDataSource(JSONObject config){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(config.getString("url"));
        String username = config.getString("username");
        if (!StringUtils.isEmpty(username)){
            dataSource.setUsername(config.getString("username"));
        }
        String password = config.getString("password");
        if(!StringUtils.isEmpty(password)){
            dataSource.setPassword(config.getString("password"));
        }


        dataSource.setDriverClassName(config.getString("class_name"));
        Map<String,Object> property = (Map<String, Object>) config.get("property");
        Set<Map.Entry<String, Object>> entries = property.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
        while(iterator.hasNext()){
            Map.Entry<String, Object> next = iterator.next();
            dataSource.addConnectionProperty(next.getKey(),next.getValue()+"");
        }
        try {
            dataSource.init();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return dataSource;
    }

    @Bean(DruidConfiguration.READ_SOURCE)
    @Primary
    public DataSource dataReadSource(){


        Map<String,Object> field = new HashMap<>();
        field.put("connection_update","false");
        field.put("table_update","false");
        Map<String, Object> result = DatabaseConnectionConfigUtils.result("service_read_source", field);
        if(!(boolean)result.get("success")){
            return null;
        }
        JSONObject config = DatabaseConnectionConfigUtils.ConfigOne(result);
        return getDataSource(config);

    }


    @Bean(DruidConfiguration.WRITE_SOURCE)
    public DataSource dataWriteSource(){
        Map<String,Object> field = new HashMap<>();
        field.put("connection_update","false");
        field.put("table_update","false");
        Map<String, Object> result = DatabaseConnectionConfigUtils.result("service_write_source", field);
        if(!(boolean)result.get("success")){
            return null;
        }
        JSONObject config = DatabaseConnectionConfigUtils.ConfigOne(result);
        return getDataSource(config);
    }


    @Bean(DruidConfiguration.READ_TEMPLATE)
    @Primary
    public  JdbcTemplate getReadTemplate(@Qualifier(DruidConfiguration.READ_SOURCE)  DataSource dataSource){

        if(dataSource == null){
            return null;
        }
        JdbcTemplate readTemplate = new JdbcTemplate();
        readTemplate.setDataSource(dataSource);




        return readTemplate;
    }

    @Bean(DruidConfiguration.WRITE_TEMPLATE)
    public JdbcTemplate getWriteTemplate(@Qualifier(DruidConfiguration.WRITE_SOURCE)  DataSource dataSource){
        if(dataSource == null){
            return null;
        }
        JdbcTemplate writeTemplate = new JdbcTemplate();
        writeTemplate.setDataSource(dataSource);


        return writeTemplate;
    }
}
